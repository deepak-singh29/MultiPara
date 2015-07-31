package com.example.multipara;

import android.util.Log;

public class Lead {
	private static final String TAG = null;
	private String description="";
	private int leadNumber;
	private float[] paintArray = null;
	private float leftPadPx;
	private float upperPadPx;
	private float lowerPadPx;
	private int seconds;

	
	public Lead(int leadNumber, int seconds)
	{
		Log.i(TAG, "lead constructer");
		this.leadNumber = leadNumber;
		this.description = AppSettings.leadDescription[this.leadNumber];
		this.seconds = seconds;
		this.paintArray = new float[AppSettings.seconds*AppSettings.frequency*4];
		this.setLeftPadPx();
		this.setUpperPadPx();
		this.generateXvaluesDrawArray();
		this.generateYvaluesDrawArray();
		Log.i(TAG, "end of lead constructer");
	}
	
	private void setLeftPadPx()
	{
		if(AppSettings.formatColumns==1)
		{
			//Log.i(TAG, "AppSettings.formatColumns==1");
			
			this.leftPadPx = AppSettings.convertCm2PxX(AppSettings.leftPadCm);
			
			//Log.i(TAG, "end of AppSettings.formatColumns==1");
		}
		else
		{
			//Log.i(TAG, "else of AppSettings.formatColumns==1");
			if(this.leadNumber < AppSettings.paintLeadCount/2)
			{
				Log.i(TAG, "lead2");
				this.leftPadPx = AppSettings.convertCm2PxX(AppSettings.leftPadCm); 
				
			}
			else
			{
				Log.i(TAG, "lead3");
				this.leftPadPx = AppSettings.convertCm2PxX(AppSettings.leftPadCm * 3) + AppSettings.convertCm2PxX(this.seconds * AppSettings.forwardSpeed);
				
			}
			//Log.i(TAG, "end of else of AppSettings.formatColumns==1");
		}
	}
	
	private void setUpperPadPx()
	{
		//Log.i(TAG, "lead setUpperPadPx");
		if(AppSettings.formatRows==AppSettings.paintLeadCount)
		{
			this.upperPadPx = AppSettings.convertCm2PxY(AppSettings.upperPadCm) + AppSettings.convertCm2PxY(AppSettings.betweenLeadPadCm + AppSettings.leadHeightCm) * this.leadNumber ;
		}
		else
		{
			if(this.leadNumber < AppSettings.formatRows)
			{
				this.upperPadPx = AppSettings.convertCm2PxY(AppSettings.upperPadCm) + AppSettings.convertCm2PxY(AppSettings.betweenLeadPadCm + AppSettings.leadHeightCm) * this.leadNumber ;
			}
			else
			{
				this.upperPadPx = AppSettings.convertCm2PxY(AppSettings.upperPadCm) + AppSettings.convertCm2PxY(AppSettings.betweenLeadPadCm + AppSettings.leadHeightCm) * (this.leadNumber-AppSettings.paintLeadCount/2) ;
			}
		}
		
		lowerPadPx = this.upperPadPx + AppSettings.convertCm2PxY(AppSettings.leadHeightCm);
		//Log.i(TAG, "end of lead setUpperPadPx");
	}
	
	private void generateXvaluesDrawArray()
	{
		//Log.i(TAG, "lead generatexvalues");
		int j=1;
		this.paintArray[0]=this.leftPadPx;
				
		for(int i=2;i<this.paintArray.length-2;i+=4)
		{
			this.paintArray[i] = this.leftPadPx + j*AppSettings.xStep;
			this.paintArray[i+2] = this.leftPadPx + j*AppSettings.xStep;
			
			j++;
		}	
		this.paintArray[this.paintArray.length-2] = this.leftPadPx+j*AppSettings.xStep;
		//Log.i(TAG, "end of lead generatexvalues");
	}
	
	private void generateYvaluesDrawArray()
	{
		//Log.i(TAG, "lead generateYvalues");
		this.paintArray[1] = this.upperPadPx+AppSettings.convertCm2PxY(AppSettings.leadHeightCm/2);
				
		for(int i=3;i<this.paintArray.length-2;i+=4)
		{
			this.paintArray[i] = this.upperPadPx+AppSettings.convertCm2PxY(AppSettings.leadHeightCm/2);
			this.paintArray[i+2] = this.upperPadPx+AppSettings.convertCm2PxY(AppSettings.leadHeightCm/2);
		}	
		this.paintArray[this.paintArray.length-1]=this.upperPadPx+AppSettings.convertCm2PxY(AppSettings.leadHeightCm/2 );
		Log.i(TAG, "end of lead generateYvalues");
	}
	
	public void insertYinPaintArray(int value) 
	{
		Log.i(TAG, "lead insertYinPaintArray");
		/*
		 ^--------------------------------1023---------------------------------------
		 |
		 |
		 2cm ------------------------------512---------------------------------------
		 |
		 |
		 v----------------------------------0----------------------------------------
		 */		
		
		if (AppSettings.currentIndex == 0)
			this.paintArray[1] = this.lowerPadPx - value*AppSettings.yStep * AppSettings.scalefactor;
		else
		{
			this.paintArray[2 + AppSettings.currentIndex+(AppSettings.currentIndex-1)*3] = this.lowerPadPx - value*AppSettings.yStep * AppSettings.scalefactor;
			if((2 + AppSettings.currentIndex+(AppSettings.currentIndex-1)*3)!=this.paintArray.length-1)
				this.paintArray[4 + AppSettings.currentIndex+(AppSettings.currentIndex-1)*3] = this.lowerPadPx - value*AppSettings.yStep * AppSettings.scalefactor;
		}
		//Log.i(TAG, "end of lead insertYinPaintArray");
	}
	

	
	public String getDescription()
	{
		Log.i(TAG, "lead getDescription");
		return this.description;
	}
	
	public int getLeadNumber()
	{
		//Log.i(TAG, "leadgetLeadNumber");
		return this.leadNumber;
	}
	
	public float[] getPaintArray()
	{
		//Log.i(TAG, "leadgetPaintArray");
		return this.paintArray;
	}
	
	public float getLeftPadPx()
	{
		//Log.i(TAG, "leadgetLeftPadPx");
		return this.leftPadPx;
	}
	
	public float getUpperPad()  
	{
		//Log.i(TAG, "lead getUpperPad");
		return this.upperPadPx;
	}
}
