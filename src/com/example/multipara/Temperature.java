package com.example.multipara;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multipara.R;

public class Temperature extends ActionBarActivity {
	//Reading and correcting the month-value
    final Calendar c = Calendar.getInstance();
    int month = c.get(Calendar.MONTH)+1;
	private BluetoothSocket btSocket;
	private static final String TAG = "Temperature";
	TextView tv,tvC,tvF,tvStatus; 
	float degC,degF;
	int skipPackets = 0;
	boolean isReading = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.temperature);
		btSocket = AppSettings.BtSocket;
		tv = (TextView)findViewById(R.id.tvPacket);
		tvC = (TextView)findViewById(R.id.tvC);
		tvF = (TextView)findViewById(R.id.tvF);
		tvStatus = (TextView)findViewById(R.id.tvStatus);
		getStreamData();
//		deleted start button
		/*Button btnStart = (Button)findViewById(R.id.btnStartTemp);
		btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				v.setVisibility(View.GONE);
			}
		
		});*/
	}
	private void getStreamData() {
		
		// TODO Auto-generated method stub
		// Get the BluetoothSocket input streams
       
        
     // Keep listening to the InputStream while connected
        new Thread(new Runnable() {
        	BufferedReader tmpBuff = null;
    		BufferedReader bufferedReader;
    		BufferedWriter bufferedWriter;
			@Override
			
			public void run() {
				// TODO Auto-generated method stub
				OutputStream tmpBtOutputStream  = null;
				try {
	             	 tmpBtOutputStream = btSocket.getOutputStream();
//	             	bufferedWriter = new BufferedWriter(new OutputStreamWriter(tmpBtOutputStream));
	             	InputStream tmpBtInputStream = btSocket.getInputStream();
	             	bufferedReader = new BufferedReader(new InputStreamReader(tmpBtInputStream));
//	             	bufferedWriter.write('B');
	                
	                 Log.e(TAG, "bluetooth sockets success");
	             } catch (IOException e) {
	                 Log.e(TAG, "bluetooth sockets (connectedThread) not created", e);
	             }
				Log.e(TAG,"Writing T");
            	try {
					tmpBtOutputStream.write((int)'T');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	isReading = true;
            	String receivedData;  
				while (isReading) {
		            try {
		            	
		            	
		            	//bufferedWriter.write('B');
		            	
		                // Read from the InputStream

		                Log.e(TAG,"Hey");
		                receivedData = bufferedReader.readLine().trim();	                       		
		                Log.e(TAG,receivedData);
		                try {
		                	String[] temp = receivedData.split("[T]");
			                degC = Float.parseFloat(temp[1]);
						} catch (ArrayIndexOutOfBoundsException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
		                
		                if(degC > 300)
		                	degC=0;
		                degF = ((degC *9)/5)+32;
		                
//		   skipping 5 packets for convenient output             
		             if(skipPackets > 5)
		             {
//		                writing to text fields from thread
		                /*tv.post(new Runnable() {					
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tv.setText(receivedData);
							}
						});*/
		                
		                tvC.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tvC.setText(Float.toString(degC));
							}
						});
		                
		                tvF.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tvF.setText(Float.toString(degF));
							}
						});
		                
		                	tvStatus.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(degC > 36.5 & degC<37.5)
									tvStatus.setText("Normal");
								if(degC > 37.5)
									tvStatus.setText("Fever");
								if(degC < 36.5)
									tvStatus.setText("Below Normal");
								if(degC == 0)
									tvStatus.setText("Device Error");
							}
						});
		                skipPackets = 0;
		                
		             }
//		                -------------------------
		              
			           skipPackets++;     
		            } catch (IOException e) {
		                Log.e(TAG, "IO exception", e);
		               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
		                break;
		            }
		             catch (NumberFormatException e){
		            	 Log.e(TAG, "Number format exception", e);
			               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
			                break;
		             }
		            catch (ArrayIndexOutOfBoundsException e){
		            	 
		            	 Log.e(TAG, "ArrayIndexOutOfBoundsException", e);
			               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
			                receivedData = "T00.00";
		             }
		            
		        }
			}
		},"Temp. Reading Thread").start();
        
        
	}
	 
	// code for Clickable Help textview for Temperature
				public void textClick_TemperatureHelp(View view)
				{
					Log.d(TAG, "On click temp.. Help");
					try {
						Intent temphelpIntent = new Intent(Temperature.this, Help.class);
						temphelpIntent.putExtra("HelpResolver", 't');
						startActivity(temphelpIntent);
					} catch (Exception e) {
						e.printStackTrace();
						Log.e(TAG,"Exception while loading Help Class/Activity");
					}
					
					
				}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.common_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
switch (item.getItemId()) {
		
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
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		isReading = false;
		new Thread(new Runnable(){

			@Override
			public void run() {
				OutputStream tmpBtOutputStream  = null;
				try {
	             	 tmpBtOutputStream = btSocket.getOutputStream();           	               
	                 Log.e(TAG, "bluetooth sockets on back pressed success");
	             } catch (IOException e) {
	                 Log.e(TAG, "bluetooth sockets (connectedThread) not created", e);
	             }
				Log.e(TAG,"Writing H");
            	try {
					tmpBtOutputStream.write((int)'H');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// TODO Auto-generated method stub
				
			}
			
		},"Thread writing H ").start();
		//finish();
	}
//  methods for screenshot
  public Bitmap takeScreenshot() {
		   View rootView = findViewById(android.R.id.content).getRootView();
		   rootView.setDrawingCacheEnabled(true);
		   return rootView.getDrawingCache();
		}
//methods for screenshot
	public void saveBitmap(Bitmap bitmap) {
		        
      File imgDirectory = new File(AppSettings.directory);
  	imgDirectory.mkdirs();
      AppSettings.lastImage = AppSettings.directory + "temperature_screenshot_" + c.get(Calendar.DAY_OF_MONTH)+"-"+month+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".png";
		
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
	    
	}
	
}
