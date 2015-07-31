package com.example.multipara;

/****************************************************************************************************************
 * Author: Dustin Kinast																						*
 * Date: 01.2013																								*
 * Description: Changing Language																				*
 ****************************************************************************************************************/


import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import com.example.multipara.R;

public class Language extends Activity {

	public void onCreate(Bundle icicle)
	   {
	      super.onCreate(null);
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	      setContentView(R.layout.language);  
	        
	   }
	
	public void select_english(View view)
	{
		String languageToLoad = "en";
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale=locale;
		this.getBaseContext().getResources().updateConfiguration(config, null);
		
		Intent i = new Intent(Language.this, MainScreen.class);
		// clear activity back stack
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
		
	}
	
	public void select_hindi(View view)
	{
		String languageToLoad = "hi";
		Locale locale = new Locale(languageToLoad);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale=locale;
		this.getBaseContext().getResources().updateConfiguration(config, null);
		
		Intent i = new Intent(Language.this, MainScreen.class);
		// clear activity back stack
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
		
	}
}
