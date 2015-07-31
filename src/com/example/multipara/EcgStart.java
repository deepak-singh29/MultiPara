package com.example.multipara;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.multipara.R;

public class EcgStart extends ActionBarActivity {
	private BluetoothSocket btSocket;
	private static final String TAG = "EcgStart";
	boolean isReading = false;
	long startTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecg_start);
		setTitle("ECG");
		btSocket = AppSettings.BtSocket;
	}
	
	public void btnClick_EcgStart(View view)
	{
		Log.d(TAG, "On click Start");
		try{
		Intent ecgIntent = new Intent(EcgStart.this,Visualization.class);
		startActivity(ecgIntent);
		getStreamData();
		}catch(Exception e){
			e.printStackTrace();
			Log.e(TAG, "Exception while loading Visualiztion Activity");
		}
		
	}
	private void getStreamData() {
		// TODO Auto-generated method stub
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
				Log.e(TAG,"Writing E");
            	try {
					tmpBtOutputStream.write((int)'E');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	AppSettings.ECGisReading = true;
	            // to  count no of samples /sec
            	AppSettings.counter = 0; 
				while (AppSettings.ECGisReading) {
		            try {           	            	
		                // to  count no of samples /sec
		            	if(AppSettings.counter == 0){
		            		startTime = System.currentTimeMillis();
		            		AppSettings.counter++;
		            	}else{
		            		if((System.currentTimeMillis() - startTime) < 1000)
		            			AppSettings.counter++;
		            	}
		            	// Read from the InputStream
		                Log.e(TAG,"Hey");
		                final String receivedData = bufferedReader.readLine().trim();
		                if(AppSettings.dataFacade!=null)
		                	   AppSettings.dataFacade.splitData(receivedData);	              	              
			        
		            } catch (IOException e) {
		                Log.e(TAG, "disconnected", e);
		               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
		                break;
		            }
		        }
			}
		},"ECG Reading Thread").start();
	}

	// code for Clickable Help textview for ECG
	public void textClick_EcgHelp(View view)
	{
		Log.d(TAG, "On click text Help");
		try {
			Intent ecghelpIntent = new Intent(EcgStart.this, Help.class);
			ecghelpIntent.putExtra("HelpResolver", 'e');
			startActivity(ecghelpIntent);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG,"Exception while loading Help Class/Activity");
		}
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ecg_start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		AppSettings.ECGisReading = false;
		new Thread(new Runnable(){

			@Override
			public void run() {
				OutputStream tmpBtOutputStream  = null;
				try {
	             	 tmpBtOutputStream = btSocket.getOutputStream();           	               
	                 Log.e(TAG, "bluetooth sockets success");
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
	}
	/*public void onBackPressed(){
		new Thread(new Runnable(){

			@Override
			public void run() {
				OutputStream tmpBtOutputStream  = null;
				try {
	             	 tmpBtOutputStream = btSocket.getOutputStream();           	               
	                 Log.e(TAG, "bluetooth sockets success");
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
		this.onDestroy();
	}*/
}
