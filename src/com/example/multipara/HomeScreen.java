package com.example.multipara;

import java.io.IOException;

import android.support.v7.app.ActionBarActivity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.multipara.R;

public class HomeScreen extends ActionBarActivity {
	
	private static final String TAG = "Home Screen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_screen);
		
	}
	
	// code for Clickable ECG ImageButton
		public void buttonClick_Ecg(View view){
			try {
				 
				 Intent ecgIntent = new Intent(HomeScreen.this,EcgStart.class);
				 startActivity(ecgIntent);
				 
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "Exception while loading EcgStart Class/Activity");
			}
		
		}
	// code for Clickable SPO2 ImageButton
		public void buttonClick_Spo2(View view){
			try {
				 Intent spo2Intent = new Intent(HomeScreen.this,Spo2Start.class);
				 startActivity(spo2Intent);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "Exception while loading Spo2Start Class/Activity");
			}	
		}
    // code for Clickable NIBP ImageButton
		public void buttonClick_Nibp(View view){
			try {
				 Intent nibpIntent = new Intent(HomeScreen.this,Nibp.class);
				 startActivity(nibpIntent);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "Exception while loading nibpStart Class/Activity");
			}	
		}
	// code for Clickable Temprature ImageButton
		public void buttonClick_Temperature(View view){
			try {
				 Intent tempIntent = new Intent(HomeScreen.this,Temperature.class);
				 startActivity(tempIntent);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				Log.e(TAG, "Exception while loading TemperatureStart Class/Activity");
			}	
		}
	// code for Clickable Help textview
		public void textClick_Help(View view)
		{
			Log.d(TAG, "On click text Help");
			try {
				Intent helpIntent = new Intent(HomeScreen.this, Help.class);
				helpIntent.putExtra("HelpResolver", 'h');
				startActivity(helpIntent);
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG,"Exception while loading Help Class/Activity");
			}
			
			
		}
		// code for Clickable Credits textview
		public void textClick_Credits(View view)
		{
			Log.d(TAG,"on click text Credits");
			try{
				Intent creditsIntent = new Intent(HomeScreen.this,Credits.class);
				startActivity(creditsIntent);
				
			}catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG,"Exception while loading Credits Class/Activity");
			}
			
		}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_screen, menu);
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
	
	public void options(View v){
		startActivity(new Intent(getApplicationContext(),Options.class));
	}
	public void onBackPressed(){
		super.onBackPressed();
		try {
			AppSettings.BtSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
