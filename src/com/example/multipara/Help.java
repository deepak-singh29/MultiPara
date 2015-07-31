package com.example.multipara;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import com.example.multipara.R;

public class Help extends Activity {

	private static final String TAG = "Help Resolver";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Log.d(TAG, "Inside Help Class");
		Intent i = getIntent();
		char c = i.getCharExtra("HelpResolver",'o');
		switch(c)
		{
		case 'h':
			Log.d(TAG, "Inside Help Homescreen ");
			setContentView(R.layout.help_homescreen);
			break;
		case 'o':
			Log.d(TAG, "Inside Help Homescreen ");
			setContentView(R.layout.help_homescreen);
			break;
		case 't':
			Log.d(TAG, "Inside Help Temp.. ");
			setContentView(R.layout.help_temperature);
			break;
		case 's':
			Log.d(TAG, "Inside Help Spo2 ");
			setContentView(R.layout.help_spo2);
			break;
		case 'e':
			Log.d(TAG, "Inside Help ECG ");
			setContentView(R.layout.help_ecg);
			break;
		case 'n':
			Log.d(TAG, "Inside Help Nibp ");
			setContentView(R.layout.help_nibp);
			break;
		default :
			Toast.makeText(getApplicationContext(), "Sorry nothing to help you ", Toast.LENGTH_SHORT).show();
			
		
		}
		
		
	}

}
