package com.example.multipara;

import java.io.BufferedWriter;

import android.bluetooth.BluetoothSocket;

import com.example.multipara.AppSettings;
import com.example.multipara.BluetoothConnection;
import com.example.multipara.DataFacade;
import com.example.multipara.Lead;

public class AppSettings {
	
	// RegEx
    public static final String SIXLEADSREGEX = "t\\d{1,5}a\\d{1,4}b\\d{1,4}c\\d{1,4}";
	public static final String TWELVELEADSREGEX = "t\\d{1,5}a\\d{1,4}b\\d{1,4}c\\d{1,4}d\\d{1,4}e\\d{1,4}f\\d{1,4}g\\d{1,4}h\\d{1,4}i\\d{1,4}";
	public static final String NIBPREGEX = "c\\d{1,4}s\\d{1,4}d\\d{1,4}h\\{1,3}";
	public static final String SPORGEX ="g\\d{1,4}o\\d{1,3}";
    // Data Array
	public static int leadCount = 06;
	public static int paintLeadCount = 06;
	public static float leadHeightCm = 2;
	public static int seconds = 2;
	public static int frequency = 100;
	public static int adcResolution = 2048;
	public static int adcResolutionUperbound = 1024;
	public static int adcResolutionLowerbound = 0;
	
	//Heart Rate Parameters
	public static long peak1Time = 0,peak2Time = 0;
	public static int prev = 0,curr = 0,next = 0;
	public static int thresold = 500;
	public static int counter = 0;
	//Format Settings
	public static float upperPadCm = (float) 1;
	public static float lowerPadCm = (float) 1.5;
	public static float betweenLeadPadCm = (float) 0;
	public static float betweenTextLeadPadCm = (float) 0.1;
	public static float leftPadCm = (float) 0.5;
	public static float forwardSpeed = (float) 2.5;
	public static float xStep;
	public static float yStep;
	public static int gaping = 500;
	public static int formatRows = 0;
	public static int formatColumns = 0;
	
	//Running Parameters
	public static int currentPaintIndex;
	public static int currentIndex;
	
	
	//Display Parameter
	public static float screenXdpi;
	public static float screenYdpi;
	public static int displayWidth;
	public static int displayHeight;
	
	//InterProcessCommunication
	public static BluetoothConnection bluetoothConnection = null;
	public static BluetoothSocket BtSocket = null;
    public static DataFacade dataFacade = null;
    public static Boolean showConnectionLost = true;
    
    public static Boolean recording = false;
    public static String directory = "mnt/sdcard/MultiPara/";
    public static BufferedWriter recordBufferedWriter = null;
    public static String lastRecord = "";
    public static String lastImage ="";
    
    //lead data
    public static Lead[] leads=null;
    public static String[] leadDescription={"AI","AII","AIII","AVR","AVL","AVF","C1","C2","C3","C4","C5","C6"};    
    public static float scalefactor=1;
    public static int heartRate = 0;
    
    // Bluetooth auto-reconnect
 	public static boolean autoReconnect = false;
	protected static boolean ECGisReading = false;
 	public static final String PREFERENCES_BT_AUTORECONNECT = null;
 	public final static String AUTORECONNECT_KEY = "AUTRECONNECT";
 	public final static String BT_AUTORECONNECT_ADDRESS_KEY = "AUTORECONNECT_ADDRESS";
    
    public static float convertCm2PxX(float cm)
	{
		return (float) (cm*((AppSettings.screenXdpi/2.54)));
	}
	
	public static float convertCm2PxY(float cm)
	{
		return (float) (cm*((AppSettings.screenYdpi/2.54)));
	}	
	
}
