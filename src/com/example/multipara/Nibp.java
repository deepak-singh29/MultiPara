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
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multipara.R;

public class Nibp extends ActionBarActivity {

	 final Calendar c = Calendar.getInstance();
	 int month = c.get(Calendar.MONTH)+1;
	private static final String TAG = "Nibp";
	private BluetoothSocket btSocket;
	TextView tvCP,tvSP,tvDP,tvStatus;
	int skipPackets = 0,cp,sp,dp,hr;
	boolean isReading = false;
	String status = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nibp);
		setTitle("NIBP");
		btSocket = AppSettings.BtSocket;

		tvCP = (TextView)findViewById(R.id.tvCP);
		tvSP = (TextView)findViewById(R.id.tvSP);
		tvDP = (TextView)findViewById(R.id.tvDP);
//		tvHR = (TextView)findViewById(R.id.tvHR);
//		tvStatus = (TextView)findViewById(R.id.tvStatus);
		
		final Button btnStartSpo2 = (Button)findViewById(R.id.btnNibpStart);
		btnStartSpo2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				method called on button click Start
				v.setEnabled(false);
//				v.setVisibility(View.INVISIBLE);
				NibpStart();
				
			}

		
		});
		
		Button btnStopSpo2 = (Button)findViewById(R.id.btnNibpAbort);
		btnStopSpo2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				method called on button click Abort
				isReading = false;
				NibpAbort();
				btnStartSpo2.setEnabled(true);
//				btnStartSpo2.setVisibility(View.VISIBLE);
				
			}

			
		});
		Toast.makeText(getApplicationContext(), "Press \"start\" to aquire data", Toast.LENGTH_LONG).show();
	}
	
	private void NibpStart() {
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
				Log.e(TAG,"Writing N");
            	try {
					tmpBtOutputStream.write((int)'N');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	isReading = true;
            	String receivedData,temp [];   
				while (isReading) {
		            try {
		            	
		            	//bufferedWriter.write('B');
		            	
		                // Read from the InputStream

		                Log.e(TAG,"Hey");
		                receivedData = bufferedReader.readLine().trim();
		                Log.e(TAG, receivedData);
		                
//		                if(receivedData.matches(AppSettings.NIBPREGEX)){
		                try {
		                	temp = receivedData.split("[csd]");
			                cp = Integer.parseInt(temp[1]);
			                sp = Integer.parseInt(temp[2]);
			                dp = Integer.parseInt(temp[3]);
//			                hr = Integer.parseInt(temp[4]);
						} catch (ArrayIndexOutOfBoundsException e) {
							e.printStackTrace();
							// TODO: handle exception
						}
		                
		           //skipping 3 packets for convenient output             
		             if(skipPackets > 3)
		             {
//		                writing to text fields from thread
		                tvCP.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tvCP.setText(Integer.toString(cp));
							}
						});
		                
		                tvSP.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								// to check for error
								if(sp == -15)
									tvSP.setText("Error");
								else
								    tvSP.setText(Integer.toString(sp));
								
							}
						});
		                
		                tvDP.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								if(dp == -15)
									tvDP.setText("Error");
								else
								   tvDP.setText(Integer.toString(dp));
							}
						});
		                
		                /*tvHR.post(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								tvHR.setText(Integer.toString(hr));
							}
						});*/
//		                decision based on heart rate
		                /*if(sp == 0&&dp == 0&&hr == 0){
		                	
		                	status = "waiting for device";
		                	
		                }else{
		                	if((sp > 0&&dp > 0)||hr > 0){
		                		status = "results";
		                	}
		                }
		                
		                tvStatus.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								tvStatus.setText(status);
							}
		                	
		                });*/
		                skipPackets = 0;
		                
		             }
//		                -------------------------
		              
			           skipPackets++;  
//		            }
		            } catch (IOException e) {
		                Log.e(TAG, "disconnected", e);
		               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
		                break;
		            }
		             catch (ArrayIndexOutOfBoundsException e){
		            	 
		            	 Log.e(TAG, "ArrayIndexOutOfBoundsException", e);
			               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
			                receivedData = "c0s0d0";
		             }
		            catch (NumberFormatException e){
		            	 Log.e(TAG, "Number format exception", e);
			               // Toast.makeText(getApplicationContext(), "Connection Lost", Toast.LENGTH_LONG).show();
			         }
				}
				
			}
		},"NIBP Reading Thread").start();
	}
	
	private void NibpAbort() {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				OutputStream tmpBtOutputStream  = null;
				try {
	             	 tmpBtOutputStream = btSocket.getOutputStream();
	             	Log.e(TAG, "bluetooth sockets success for Abort");
	             } catch (IOException e) {
	                 Log.e(TAG, "bluetooth sockets (connectedThread) not created", e);
	             }
				try {
					Log.d(TAG, "writing A for abort");
					tmpBtOutputStream.write((int)'A');
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		},"NIBP Abort Thread").start();
		
	}
	// code for Clickable Help textview for Nibp
				public void textClick_NibpHelp(View view)
				{
					Log.d(TAG, "On click text Help");
					try {
						Intent nibphelpIntent = new Intent(Nibp.this, Help.class);
						nibphelpIntent.putExtra("HelpResolver", 'n');
						startActivity(nibphelpIntent);
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
		Toast.makeText(getApplicationContext(), "on Destroy", Toast.LENGTH_SHORT).show();
		isReading = false;
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
      AppSettings.lastImage = AppSettings.directory + "Nibp_screenshot_" + c.get(Calendar.DAY_OF_MONTH)+"-"+month+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".png";
		
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
