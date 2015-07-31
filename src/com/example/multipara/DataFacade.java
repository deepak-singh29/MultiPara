package com.example.multipara;

import java.io.IOException;

import android.util.Log;

public class DataFacade {
	
	// Debugging
    private static final String TAG = "DataFacade";
    private static final boolean D = false;
    
	private int leadData[][];
	String[] tmpArray = null;
	// statistics counter
	private double validLineCounter = 0;
	private double invalidLineCounter = 0;
	private double correctLineCounter = 0;
	
	// Heart Beat Rate
	// for detailed documentation have a look at www.ti.com/lit/an/slaa280a/slaa280a.pdf
	
	// hrIndex for counting initial 2 seconds
	private int hrIndex = 0;
	// hrPmax is used for calculating threshold - maximum value of first derivative 
	private int hrPmax = 0;
	// maximum value of QRS peak
	private int hrRawMax = 0;
	// temporary variable for first derivative of each sample
	private int hrP = 0;
	// hrThreshold is a limit value for detecting QRS- Peaks
	private float hrThreshold=0;
	// 6 indexes for 5 RR intervals
	private int[] hrPeakIndexArray = { 0, 0, 0, 0, 0, 0 };
	// index from 0 to 5 for hrPeakIndexArray --> peak found --> increased by 1 
	private int hrPeakIndex = 0;
	// counter for detecting QRS max --> runs from 0 to hrMaximaSearchWindow
	// -1 is used for skipping algorithm parts
	private int hrMaxSearch = -1;
	// hrSkip hold-down timer after QRS peak is detected to take care of the minimum RR interval
	// -1 is used for skipping algorithm parts
	private int hrSkip = -1;
	// Average RR interval
	private float hrAvgRR = 0;
	
	// window for detecting maximum of QRS --> documentation
	private static final int hrMaximaSearchWindow = 40;
	// window for hold-down timer to take care of the minimum RR interval --> documentation
	private static final int hrSkipWindow = 50;
	
	// Get Method
	public int[][] LeadData()
	{
		return this.leadData;
	}
	
	// Constructor leadCount = how many Leads exists(3/6), seconds to display
	public DataFacade()
	{
		// allocate memory for leadData
		this.leadData = new int[AppSettings.paintLeadCount][AppSettings.seconds * AppSettings.frequency];
		// initialize leadData (original values) 
		for (int i=0;i<(AppSettings.leadCount);i++)
			for (int j=0; j<(AppSettings.seconds*AppSettings.frequency); j++)
				this.leadData[i][j] = 0;
	}
	
	// split Data is used to check for a valid DataString and extract the Data from it
	public void splitData(String dataString)
	{
		//remove whitespace like \r\n
		dataString = dataString.trim();
		//Remove � from dataString ........................sometimes � occurs with double digit lead data
		
		dataString = dataString.replaceAll("�","");
		//.............................................
		
		tmpArray = dataString.split("[tabc]");
		// recording into text file
		if(AppSettings.recording)
		{
			try {
				
				AppSettings.recordBufferedWriter.write(dataString + "\n");
			} catch (IOException e) {
				if(D) Log.e(TAG,"Could not safe ECG line!");
				e.printStackTrace();
			}
		}
		
		//check if the received String is valid
		if(dataString.matches(AppSettings.TWELVELEADSREGEX))//AppSettings.TWELVELEADSREGEX as aragument
		{
			// valid
			this.validLineCounter++;
			this.correctLineCounter++;
			
			try
			{
				if(D) Log.i(TAG, "fill tmp array");
				// split the data
				tmpArray = dataString.split("[tabcdefghi]");
				//tmpArray = dataString.split("[tabcd]");
				// [0] is everything before t --> empty String as t is the first character in dataline
				// to extract the data start at index 1 as timestamp
				
				if(D) Log.i(TAG, "calc current index");
				AppSettings.currentIndex %= (AppSettings.seconds * AppSettings.frequency);
				
				//AppSettings.currentIndex = (Integer.parseInt(tmpArray[1])) % (AppSettings.seconds * AppSettings.frequency);
				AppSettings.currentPaintIndex = AppSettings.currentIndex*4;
				
				if(D) Log.i(TAG, "split the data");
				
				leadData [0] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[2]) - AppSettings.adcResolution / 2; //-1024 < x < 1024				
				leadData [1] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[3]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;
				leadData [2] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[4]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;				
				leadData [3] [AppSettings.currentIndex] = (leadData [0] [AppSettings.currentIndex] - leadData [2] [AppSettings.currentIndex]) / 2;				
				leadData [4] [AppSettings.currentIndex] = (-leadData [0] [AppSettings.currentIndex] - leadData [1] [AppSettings.currentIndex]) / 2;				
				leadData [5] [AppSettings.currentIndex] = (leadData [1] [AppSettings.currentIndex] + leadData [2] [AppSettings.currentIndex]) / 2;
				
				if(D) Log.i(TAG, "next six leads");
				leadData [6] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[5]) - AppSettings.adcResolution / 2;
				leadData [7] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[6]) - AppSettings.adcResolution / 2;
				leadData [8] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[7]) - AppSettings.adcResolution / 2;
				leadData [9] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[8]) - AppSettings.adcResolution / 2;
				leadData [10] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[9]) - AppSettings.adcResolution / 2;
				leadData [11] [AppSettings.currentIndex] = AppSettings.adcResolution - Integer.parseInt(tmpArray[10]) - AppSettings.adcResolution / 2;
				
				if(D) Log.i(TAG, "insert into draw");
				for(int i=0;i<AppSettings.paintLeadCount;i++)
				{
					AppSettings.leads[i].insertYinPaintArray(leadData [i] [AppSettings.currentIndex]+512);
				}
				heartrate();
				AppSettings.currentIndex++;
				
				//Log.i(TAG,"t: "+tmpArray[1]+" a: "+tmpArray[2]+" b: "+tmpArray[3]+" c: "+tmpArray[4]);
				
				//if(D) Log.i(TAG,"t: "+tmpArray[1]+" a: "+tmpArray[2]+" b: "+tmpArray[3]+" c: "+tmpArray[4]);
				
				/*if (D) Log.i(TAG, "a: " + String.valueOf(leadData[0][(Integer.parseInt(tmpArray[1])) % (seconds * frequency)]) + 
						" b: " + String.valueOf(leadData[1][(Integer.parseInt(tmpArray[1])) % (seconds * frequency)]) +
						" c: " + String.valueOf(leadData[2][(Integer.parseInt(tmpArray[1])) % (seconds * frequency)])); */
			}
			catch ( Exception e ) { 
				if(D) Log.e(TAG, "Twelve Leads Index Error at: " + (Integer.parseInt(tmpArray[1]) % (AppSettings.seconds * AppSettings.frequency))); 
			}
		}
		
			if(dataString.matches(AppSettings.SIXLEADSREGEX))
			{
				AppSettings.leadCount=6;
				AppSettings.paintLeadCount=6;
				
				// valid
				this.validLineCounter++;
				this.correctLineCounter++;
				
				try
				{
					if(D) Log.i(TAG, "before separation");
					// split the data
					tmpArray = dataString.split("[tabc]");
					// [0] is everything before t --> empty String as t is the first character in dataline
					// to extract the data start at index 1 as timestamp
					if(D) Log.i(TAG, "calc current index");
					AppSettings.currentIndex %= (AppSettings.seconds * AppSettings.frequency);
					
					//AppSettings.currentIndex = (Integer.parseInt(tmpArray[1])) % (AppSettings.seconds * AppSettings.frequency);
					AppSettings.currentPaintIndex=AppSettings.currentIndex*4;
					if(D) Log.i(TAG, "split the data");
					
//					leadData [0] [AppSettings.currentIndex] = AppSettings.adcResolution - (Integer.parseInt(tmpArray[2])*2) - AppSettings.adcResolution / 2; //-1024 < x < 1024				
//					leadData [1] [AppSettings.currentIndex] = AppSettings.adcResolution - (Integer.parseInt(tmpArray[3])*2) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;
//					leadData [2] [AppSettings.currentIndex] = AppSettings.adcResolution - (Integer.parseInt(tmpArray[4])*2) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;	
					
					leadData [0] [AppSettings.currentIndex] =(Integer.parseInt(tmpArray[2]));
					leadData [1] [AppSettings.currentIndex] = (Integer.parseInt(tmpArray[3]));
					leadData [2] [AppSettings.currentIndex] = (Integer.parseInt(tmpArray[4]));
					
					leadData [3] [AppSettings.currentIndex] = (leadData [0] [AppSettings.currentIndex] - leadData [2] [AppSettings.currentIndex]) / 2;					
					leadData [4] [AppSettings.currentIndex] = (-leadData [0] [AppSettings.currentIndex] - leadData [1] [AppSettings.currentIndex]) / 2;					
					leadData [5] [AppSettings.currentIndex] = (leadData [1] [AppSettings.currentIndex] + leadData [2] [AppSettings.currentIndex]) / 2;
					
					if(D) Log.i(TAG, "insert into draw");
					for(int i=0;i<AppSettings.paintLeadCount;i++)
					{
						AppSettings.leads[i].insertYinPaintArray(leadData [i] [AppSettings.currentIndex]+512);
						
					}
					
//					modified_heartrate();
					heartrate();
					AppSettings.currentIndex++;
				}
				catch ( Exception e ) { 
					if(D) Log.e(TAG, "Six Index Error at: " + (Integer.parseInt(tmpArray[1]) % (AppSettings.seconds * AppSettings.frequency))); 
				}
			}
			else
			{
				// invalid
				this.invalidLineCounter++;
				if(D) Log.e(TAG, "Correct Lines: " + Double.toString(this.correctLineCounter) + " Valid: " + Double.toString(this.validLineCounter) + " Invalid: " + Double.toString(this.invalidLineCounter));
				if(D) Log.e(TAG, dataString);
				this.correctLineCounter = 0;
			}	
		}
	
//	modified method for heartrate
	private void modified_heartrate(){
		AppSettings.prev = AppSettings.curr;
		AppSettings.curr = AppSettings.next;
		AppSettings.next = Integer.parseInt(tmpArray[3]);
		
		if(AppSettings.curr > AppSettings.thresold && AppSettings.curr > AppSettings.prev && AppSettings.curr > AppSettings.next){
			AppSettings.peak2Time = System.currentTimeMillis();
			
		}
		if(AppSettings.peak1Time > 0 && AppSettings.peak2Time >0){
			long timeBetPeaks = (AppSettings.peak2Time - AppSettings.peak1Time)/1000;
			AppSettings.heartRate = (int)(60/timeBetPeaks);
		}
		AppSettings.peak1Time = AppSettings.peak2Time;
		Log.e(TAG, "Heartrate :"+AppSettings.heartRate);
	}
	// calculates heart rate  
	// for detailed documentation have a look at www.ti.com/lit/an/slaa280a/slaa280a.pdf 
	private void heartrate()
	{
		Log.e(TAG,"start Heartbeat");
		// run initial 2 seconds
		if (hrIndex < AppSettings.frequency*2)
		{
			// for the calculation of the derivatives 3 values are required
			if (hrIndex > 1)
			{				
				// calculate first derivative: y0(n) = |x(n + 1) - x(n - 1)|
				
				// overflow handling
				if (AppSettings.currentIndex < 2)
				{
					hrP = Math.abs(leadData[1][AppSettings.currentIndex] - leadData[1][AppSettings.seconds * AppSettings.frequency-(2-AppSettings.currentIndex)]);
				}
				else
				{
					hrP = Math.abs(leadData[1][AppSettings.currentIndex] - leadData[1][AppSettings.currentIndex-2]);
				}
				
				// detect maximum derivative
				if (hrP > hrPmax)
				{
					hrPmax = hrP;
					// 70% of maximum --> see documentation
					hrThreshold = (float) (hrPmax * 0.7);
				}					
			}
			hrIndex++;
		}
		else
		{
			// runs after initial 2 seconds
			
			// check if values should be skipped after a detected QRS peak
			if ((hrSkip < hrSkipWindow) && (hrSkip != (-1)))
			{
				hrSkip++;
			}
			else
			{
				// calculate first derivative: y0(n) = |x(n + 1) - x(n - 1)|
				// overflow handling
				if (AppSettings.currentIndex < 2)
				{
					hrP = Math.abs(leadData[1][AppSettings.currentIndex] - leadData[1][AppSettings.seconds * AppSettings.frequency-(2-AppSettings.currentIndex)]);
				}
				else
				{
					hrP = Math.abs(leadData[1][AppSettings.currentIndex] - leadData[1][AppSettings.currentIndex-2]);
				}
				
				// check if the current value of the first derivative is higher as the threshold
				if (hrP > hrThreshold)
				{
					// check if already scanning for maximum peak
					if(hrMaxSearch == -1)
					{
						// if not - start detecting
						hrMaxSearch = 0;
						hrPmax = hrP;
						hrRawMax = 0;
						hrAvgRR = 0;
					}
				}
				
				// detect maximum QRS Peak
				if ((hrMaxSearch < hrMaximaSearchWindow) && (hrMaxSearch != -1))
				{
					// get maximum derivative value for threshold
					if (hrP > hrPmax)
						hrPmax = hrP;
					
					// current value > max. value? --> save index
					if (leadData[1][AppSettings.currentIndex] > hrRawMax)
					{
						hrPeakIndexArray[hrPeakIndex] = AppSettings.currentIndex;
						hrRawMax = leadData[1][AppSettings.currentIndex];
					}
				
					hrMaxSearch++;
					
					// if hrMaximaSearchWindow is scanned
					if (hrMaxSearch == hrMaximaSearchWindow)
					{
						// pass hrPeakIndexArray to calculate hrAvgRR
						for (int i = 0; i < 6; i++)
						{
							// current index is stored in hrPeakIndex as the difference is calculated between index i and i+1
							// you have to skip hrPeakIndex because it would be the difference between the current and the 
							// index five peaks ago
							if (i != hrPeakIndex) 
							{
								// overflow handling
								if (i < 5 )
								{
									// check if current value of index is higher than the next --> AppSettings.currentIndex overflow
									if (hrPeakIndexArray[i+1] < hrPeakIndexArray[i])
									{
										hrAvgRR += hrPeakIndexArray[i+1] + (AppSettings.seconds * AppSettings.frequency) - hrPeakIndexArray[i]; 
									}
									else
									{
										hrAvgRR += hrPeakIndexArray[i+1] - hrPeakIndexArray[i];
									}
								}
								else
								{
									// check if current value of index is higher than the next --> AppSettings.currentIndex overflow
									if (hrPeakIndexArray[0] < hrPeakIndexArray[i])
									{
										hrAvgRR += hrPeakIndexArray[0] + (AppSettings.seconds * AppSettings.frequency) - hrPeakIndexArray[i]; 
									}
									else
									{									
										hrAvgRR += hrPeakIndexArray[0] - hrPeakIndexArray[i];
									}
								}
							}
						}
						
						// calculate average RR interval
						hrAvgRR = hrAvgRR/5;
						// calculate heart rate
						AppSettings.heartRate = (int) ((60 * AppSettings.frequency) / hrAvgRR);
						// calculate new threshold
						hrThreshold = (float)(0.7 * hrPmax);
						
						// start skipping next hrSkipWindow values
						hrSkip = 0;
						hrMaxSearch = -1;
						hrPeakIndex++;
						
						// overflow handling
						if (hrPeakIndex == 6)
							hrPeakIndex = 0;
					}
				}
			}
		}
		Log.e(TAG," Heartbeat   "+AppSettings.heartRate);
		Log.e(TAG,"Frequency Counter  "+AppSettings.counter);
	}
	
	/*
	 * resets the parameters used for the heartrate-calculation to restart the heartrate function
	 */
	
	public void resetHeartRate()
	{
		hrIndex = 0;
		hrPmax = 0;
		hrRawMax = 0;
		hrP = 0;
		hrThreshold=0;
		hrPeakIndexArray = new int[]{ 0, 0, 0, 0, 0, 0 };
		hrPeakIndex = 0;
		hrMaxSearch = -1;
		hrSkip = -1;
		hrAvgRR = 0;	
	}
}