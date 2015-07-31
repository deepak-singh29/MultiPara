package com.example.multipara;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Visualization extends Activity {

	private static final String TAG = "Visualization";
	private static final boolean D = true;
	
	private Draw d=null;
	//Reading and correcting the month-value
    final Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH)+1;
    
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setTitle("Ecg");
		
		setContentView(R.layout.visualization);
		if (D) Log.e(TAG, "++ ON CREATE ++");
		
		
		
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    		
		AppSettings.screenXdpi = metrics.xdpi;
		AppSettings.screenYdpi = metrics.ydpi;
		AppSettings.displayWidth = metrics.widthPixels;
		AppSettings.displayHeight = metrics.heightPixels;
		AppSettings.xStep = AppSettings.convertCm2PxX((float) (AppSettings.forwardSpeed / AppSettings.frequency));
		AppSettings.yStep = AppSettings.convertCm2PxY(AppSettings.leadHeightCm) / (AppSettings.adcResolutionUperbound - AppSettings.adcResolutionLowerbound);
		
		//check if there is enough space to display 2 leads next to each other
		if(metrics.widthPixels/metrics.xdpi*2.54 < 16)
		{
			AppSettings.formatColumns = 1;
			AppSettings.seconds = (int) ((metrics.widthPixels-AppSettings.convertCm2PxX(AppSettings.leftPadCm))/AppSettings.convertCm2PxX(AppSettings.forwardSpeed));
			AppSettings.formatRows = AppSettings.paintLeadCount;
		}
		else
		{
			AppSettings.formatColumns = 2;
			AppSettings.seconds = (int) (((metrics.widthPixels-AppSettings.convertCm2PxX((AppSettings.leftPadCm*2)))/AppSettings.convertCm2PxX(AppSettings.forwardSpeed))/2);
			AppSettings.formatRows = AppSettings.paintLeadCount/AppSettings.formatColumns;
		}
		
		//calculate distance between leads (horizontal)
		AppSettings.betweenLeadPadCm = (float) (((metrics.heightPixels-AppSettings.formatRows * AppSettings.convertCm2PxY(AppSettings.leadHeightCm)-AppSettings.convertCm2PxY(AppSettings.upperPadCm)-AppSettings.convertCm2PxY(AppSettings.lowerPadCm))/(AppSettings.formatRows-1))/metrics.xdpi*2.54);
		AppSettings.betweenLeadPadCm -= AppSettings.betweenLeadPadCm % (float)0.5;
		
		if(AppSettings.betweenLeadPadCm<(float)0.5)
		{
			AppSettings.betweenLeadPadCm=(float)0.5;
		}
		Log.i(TAG,"before lead");
		AppSettings.leads=new Lead[AppSettings.paintLeadCount];
		for(int i=0 ; i < AppSettings.paintLeadCount ; i++)
		{
			AppSettings.leads[i]=new Lead(i,AppSettings.seconds);
		}
		
		if(D) Log.i(TAG,"Display width cm: " + metrics.widthPixels/metrics.xdpi*2.54 + "\nDisplay height cm: " + metrics.heightPixels/metrics.ydpi*2.54);
		if(D) Log.i(TAG,"FormatSettings columns: " + AppSettings.formatColumns + "\nFormatSettings rows: " + AppSettings.formatRows + "\nFormatSettings seconds: " + AppSettings.seconds);
		
		
		d = new Draw(this);
		
		RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.relativeLayoutVisualization);
		
		int layoutWidth =(int) AppSettings.convertCm2PxX(AppSettings.seconds*AppSettings.forwardSpeed*AppSettings.formatColumns + AppSettings.leftPadCm*(AppSettings.formatColumns+1));
		int layoutHeight = (int) AppSettings.convertCm2PxY(AppSettings.upperPadCm + (AppSettings.leadHeightCm*AppSettings.formatRows) + (AppSettings.betweenLeadPadCm*(AppSettings.formatRows-1) + AppSettings.lowerPadCm));
		
		if (layoutWidth < AppSettings.displayWidth)
			layoutWidth = AppSettings.displayWidth;
		
		if (layoutHeight < AppSettings.displayHeight)
			layoutHeight = AppSettings.displayHeight;
		
		rLayout.addView(d, layoutWidth, layoutHeight);
		Log.i(TAG,"after call to draw from visual");
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppSettings.dataFacade.resetHeartRate();
        Log.i(TAG, "onDestroy");
        //MainScreen.disconnect();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_visualization, menu);
		return true;
	} 
    
    public boolean onPrepareOptionsMenu(Menu menu)
    {   
        if(AppSettings.recording) 
        {           
            menu.findItem(R.id.Record).setVisible(false);
            menu.findItem(R.id.Record_stop).setVisible(true);
            menu.findItem(R.id.Print).setVisible(true);
        }
        else
        {
        	menu.findItem(R.id.Record).setVisible(true);
            menu.findItem(R.id.Record_stop).setVisible(false);
        }
        return true;
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.Record:
			//start recording
			
			Toast.makeText(getApplicationContext(), R.string.toast_record_start, Toast.LENGTH_SHORT).show();
			
            
			
	        
            try {
            	// 
            	File ecgDirectory = new File(AppSettings.directory);
            	ecgDirectory.mkdirs();
            	AppSettings.lastRecord = AppSettings.directory + "ecg_record_" + c.get(Calendar.DAY_OF_MONTH)+"-"+month+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".txt";
				AppSettings.recordBufferedWriter = new BufferedWriter(new FileWriter(AppSettings.lastRecord));
            } catch (IOException e) {
				Toast.makeText(getApplicationContext(), R.string.toast_record_file_error, Toast.LENGTH_LONG).show();
			}
            AppSettings.recording = true;
			
			break;
		case R.id.Record_stop:
			// Stop recording
			
			Toast.makeText(getApplicationContext(), R.string.toast_record_stop, Toast.LENGTH_SHORT).show();
			AppSettings.recording = false;
			try {
				AppSettings.recordBufferedWriter.flush();
				AppSettings.recordBufferedWriter.close();
			} catch (IOException e) {
				Toast.makeText(getApplicationContext(), R.string.toast_record_close_file_error, Toast.LENGTH_LONG).show();
			}

			break;
		case R.id.Print:
			Thread printThread = new Thread(new Features());
			printThread.start();
			
			break;
		case R.id.ScreenShot:
			Bitmap bitmap = takeScreenshot();
		    saveBitmap(bitmap);
		    Toast.makeText(getApplicationContext(),"Screenshot saved in"+"   \""+AppSettings.directory+"\"  ", Toast.LENGTH_LONG).show();
		    break;
		case R.id.mail:
			Intent mailIntent = new Intent(getApplicationContext(),Mail.class);
			startActivity(mailIntent);
			Toast.makeText(getApplicationContext(), "please fill details to send mail", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
		
	}
    
//    methods for screenshot
    public Bitmap takeScreenshot() {
		   View rootView = findViewById(android.R.id.content).getRootView();
		   rootView.setDrawingCacheEnabled(true);
		   return rootView.getDrawingCache();
		}
//  methods for screenshot
	public void saveBitmap(Bitmap bitmap) {
		        
        File imgDirectory = new File(AppSettings.directory);
    	imgDirectory.mkdirs();
        AppSettings.lastImage = AppSettings.directory + "ecg_screenshot_" + c.get(Calendar.DAY_OF_MONTH)+"-"+month+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".png";
		
	    File imagePath = new File(AppSettings.lastImage);
	    FileOutputStream fos;
	    try {
	        fos = new FileOutputStream(imagePath);
	        bitmap.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        Log.e("GREC", e.getMessage(), e);
	    } catch (IOException e) {
	        Log.e("GREC", e.getMessage(), e);
	    }
	    
	    
	    if(AppSettings.recording)
        {
        	AppSettings.recording = false;
	        try {
				AppSettings.recordBufferedWriter.close();
			} catch (IOException e) {
				if(D) Log.e(TAG,"Could not close ecg file.");
				e.printStackTrace();
			}
        }
	    AppSettings.ECGisReading = false;
	}
	/*@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		 Create an Intent that will start the Menu-Activity. 
        //Intent mainIntent = new Intent(Visualization.this,MainScreen.class);
        //Visualization.this.startActivity(mainIntent);
//        to close bluetooth connection
//        AppSettings.bluetoothConnection.stop();

       
        
        finish();
	}*/
	
	
	
}
