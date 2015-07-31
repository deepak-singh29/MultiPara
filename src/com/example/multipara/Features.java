package com.example.multipara;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Calendar;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Features implements Runnable{
	
	//Parameters for the grid and the curves
	float scale_ps = 1;							//Additional scalefactor for PS
	float scale_pdf = (float)0.5;				//Additional scalefactor for PDF
	float corrh = (float)0.9812472743131269;	//Horizontal correction-value
	float corrv = 1;							//Vertical correction-value
	float scalesite = 1;						//Scalefactor of the site
	float offset = 0;							//Offset from the X-axis
	float scalefactor = (float)0.4;				//Defines the height of the curves
	int fg = 30;								//Size of the grid
	
	private static final String TAG = "Features";
	private static final boolean D = true;
	
	private Context ownerContext;
	
	
	/**************************************************************/
	/* Writing a PostScript-document: Jakob Januschkowetz, 4AHITI */
	/**************************************************************/

    //write_ps method with the number of calls (1 or 2) to difference between 6 or 12 derivations
	public void write_ps(Context context)
    {  				
		try
		{
	    	int[] lead = {496,  415,  392,  423,  489,  558,  604,  618, 599,  560,  521,  493,  485,  496,  519,  543,  559,  564,  556, 544,  529,  520,  
	    			516,  521,  529,  536,  543,  545,  542,  538, 533,  529,  528,  530,  534,  534,  538,  538,  537,  536,  535, 533,  533,  534,  534,  
	    			535,  537,  536,  536,  536,  534,  533, 534,  534,  533,  534,  535,  535,  535,  534,  534,  534,  534, 534,  535,  535,  535,  535,  
	    			534,  534,  533,  534,  534,  535, 533,  534,  534,  534,  534,  533,  534,  534,  534,  534,  533, 533,  534,  533,  533,  533,  534,  
	    			534,  534,  533,  534,  533, 532,  533,  534,  534,  534,  535,  536,  536,  536,  537,  539, 540,  540,  540,  541,  542,  543,  543,  
	    			544,  544,  545,  546, 547,  548,  550,  550,  550,  551,  551,  552,  551,  550,  549, 547,  545,  544,  543,  540,  539,  537,  536,  
	    			534,  533,  533, 531,  532,  531,  531,  531,  532,  531,  530,  532,  531,  530, 530,  530,  531,  530,  530,  529,  529,  530,  530,  
	    			531,  534, 535,  538,  542,  547,  554,  560,  566,  573,  579,  582,  583, 583,  578,  571,  560,  546,  531,  516,  500,  487,  476,  
	    			468, 466,  465,  470,  478,  486,  498,  508,  518,  527,  534,  540, 541,  543,  542,  540,  537,  535,  532,  531,  529,  529,  529, 
	    			531,  532,  533,  536,  537,  537,  539,  539,  539,  538,  537, 537,  536,  537,  538,  536,  537,  538,  538,  538,  539,  539, 540,  
	    			540,  541,  540,  540,  541,  541,  541,  541,  542,  542, 542,  542,  542,  542,  543,  543,  543,  543,  543,  542,  543, 543,  543,  
	    			543,  543,  544,  543,  543,  543,  543,  543,  543, 543,  543,  544,  544,  543,  543,  543,  543,  543,  543,  541, 543,  544,  543,  
	    			542,  542,  542,  541,  542,  540,  540,  539, 540,  538,  538,  537,  537,  536,  536,  536,  535,  535,  533, 534,  534,  534,  533,  
	    			532,  532,  532,  533,  532,  532,  531, 532,  532,  532,  532,  532,  532,  532,  531,  532,  532,  534, 531,  533,  532,  532,  532,  
	    			532,  533,  534,  533,  532,  533, 532,  533,  533,  532,  532,  533,  534,  533,  532,  533,  532, 532,  533,  533,  532,  532,  532,  
	    			532,  533,  533,  534,  533, 533,  533,  534,  535,  532,  534,  533,  531,  534,  533,  534, 533,  533,  531,  533,  533,  532,  533,  
	    			533,  533,  532,  532, 532,  533,  533,  531,  534,  533,  532,  533,  531,  532,  532, 532,  532,  533,  532,  530,  532,  531,  532,  
	    			532,  532,  532, 531,  534,  530,  532,  532,  532,  532,  532,  532,  533,  534, 533,  535,  535,  535,  537,  538,  538,  539,  539,  
	    			540,  541, 541,  541,  543,  545,  544,  545,  546,  547,  548,  549,  550, 549,  548,  548,  547,  545,  543,  541,  539,  538,  537,  
	    			535, 533,  531,  531,  530,  530,  530,  529,  530,  531,  529,  530, 530,  530,  529,  530,  529,  529,  529,  528,  528,  528,  528, 
	    			528,  529,  530,  532,  534,  538,  542,  547,  553,  560,  567, 573,  579,  582,  582,  581,  575,  565,  555,  540,  524,  509, 495,  
	    			481,  471,  466,  464,  465,  471,  479,  489,  501,  510, 521,  530,  535,  540,  541,  540,  540,  540};
	    	
	    	int[][] leadData = new int [12][];
	    	
	    	//Reading and correcting the month-value
	        final Calendar c = Calendar.getInstance();
	    	
	    	for (int i = 0; i<12; i++)
	    	{
	    		leadData[i] = lead;
	    	}
            	
    		/*****************************************/
        	/* Start drawing the grid and the curves */
    		/*****************************************/
        
        	//Drawing the grid
    		
    		BufferedWriter out =  new BufferedWriter(new FileWriter("mnt/sdcard/ecg_" + c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".ps"));
			
			//Writing the PostScript-header
			out.append("%!PS\r\n");
			out.append("%%Pages: 2\r\n%%Page: 1 1\r\n");
			out.append("0.5 0.5 scale\r\n");
			
			//Set color to magenta
			out.append("0 100 0 0 setcmykcolor\r\n");
			
            //vertical grid-lines
			for (int x = 150; x <= 1500; x = x + fg)
            {   
            	out.append("newpath\r\n");
            	out.append(transv(225) +" "+ transh(x) + " moveto\r\n");
            	out.append(transv(1155) +" "+ transh(x)+ " lineto\r\n");
            	out.append("0.5 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
        
            //horizontal grid-lines
			for (int y = 225 ; y <= 1155; y = y + fg)
            {		            	
            	out.append("newpath\r\n");
            	out.append(transv(y) +" "+ transh(150) + " moveto\r\n");
            	out.append(transv(y) +" "+ transh(1500) + " lineto\r\n");
            	out.append("0.5 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
           	
			//Set color to black
            out.append("0 0 0 100 setcmykcolor\r\n");
            
            //Writing the Caption
            
            out.append("/Helvetica findfont\r\n");
            out.append(transv(345) +" "+ transh(100) + " moveto\r\n");
            out.append("24 scalefont\r\n");
            out.append("setfont\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            

        	//Caption for 6 derivations
            out.append("(I) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(495) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(II) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(645) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(III) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(795) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVR) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(945) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVL) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(1095) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVF) show\r\n");
            
            out.append("grestore\r\n");
			 
            //Writing the curves
            for (int i=0; i<6; i++){
	            int count = 0;
	            for (int x = 240; x < 2740; x++)
	            {            	       	
	            	float y1 = ((leadData[i][count] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + i*150;
	            	float y2 = ((leadData[i][count+1] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + i*150;
	            	
	            	float x1 = ((x-240)/2)+240;
	            	float x2 = (float) (x1+0.5);
	            	
	            	out.append("newpath\r\n");
	            	out.append(transv(y1)+" "+transh(x1)+" moveto\r\n");
	            	out.append(transv(y2)+" "+transh(x2)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	
	            	count = count + 1;
	            	if (count == 497)
	            	{
	            		count = 0;
	            	}	
	            }
            }
           
            //Writing verifiable jags
            for(int i=0; i<6; i++)
            {
	            out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(165)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(180)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(180)+" moveto\r\n");
            	out.append(transv(315+i*150)+" "+transh(180)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(315+i*150)+" "+transh(180)+" moveto\r\n");
            	out.append(transv(315+i*150)+" "+transh(210)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(315+i*150)+" "+transh(210)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(210)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(210)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(225)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
            
    		//Writing the header-text
    		out.append("/Helvetica findfont\r\n");
	        out.append(transv(60) +" "+ transh(370) + " moveto\r\n");
	        out.append("24 scalefont\r\n");
	        out.append("setfont\r\n");
	        out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
	        out.append("(Name:) show\r\n");
	        out.append("grestore\r\n");
	        
	        //Writing the date
            out.append(transv(110) +" "+ transh(370) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(Datum: "+c.get(Calendar.DAY_OF_MONTH)+". "+(c.get(Calendar.MONTH)+1)+". "+c.get(Calendar.YEAR)+") show\r\n");
            out.append("grestore\r\n");
            
            //Reading, correcting and writing the time-value		            
            out.append(transv(160) +" "+ transh(370) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
	        if(c.get(Calendar.MINUTE)<10)
	        	out.append("(Uhrzeit: "+c.get(Calendar.HOUR_OF_DAY)+":0"+c.get(Calendar.MINUTE)+") show\r\n");
	        else
	        	out.append("(Uhrzeit: "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+") show\r\n");
	        out.append("grestore\r\n");
            
            out.append(transv(60) +" "+ transh(770) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(Geburtsdatum:) show\r\n");
	        out.append("grestore\r\n");
            out.append(transv(110) +" "+ transh(770) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(Gr) show\r\n");
            out.append("/odieresis glyphshow\r\n");
            out.append("/germandbls glyphshow\r\n");
            out.append("(e:) show\r\n");
	        out.append("grestore\r\n");
            out.append(transv(160) +" "+ transh(770) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(Masse:) show\r\n");
	        out.append("grestore\r\n");
            out.append(transv(160) +" "+ transh(1170) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(Arzt:) show\r\n");
	        out.append("grestore\r\n");
        	
        	out.append("showpage\r\n");
        	
        	// write second page if 12 leads are printed
        	if (AppSettings.leadCount == 12)
        	{
            	out.append("%%Page: 2 2\r\n");
            	out.append("0.5 0.5 scale\r\n");
            	//Set color to magenta
    			out.append("0 100 0 0 setcmykcolor\r\n");
    			
	            //vertical grid-lines
    			for (int x = 150; x <= 1500; x = x + fg)
	            {   
	            	out.append("newpath\r\n");
	            	out.append(transv(225) +" "+ transh(x) + " moveto\r\n");
	            	out.append(transv(1155) +" "+ transh(x)+ " lineto\r\n");
	            	out.append("0.5 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
            
	            //horizontal grid-lines
    			for (int y = 225 ; y <= 1155; y = y + fg)
	            {		            	
	            	out.append("newpath\r\n");
	            	out.append(transv(y) +" "+ transh(150) + " moveto\r\n");
	            	out.append(transv(y) +" "+ transh(1500) + " lineto\r\n");
	            	out.append("0.5 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
	           	
    			//Set color to black
	            out.append("0 0 0 100 setcmykcolor\r\n");
	            
	            //Writing the Caption
	            
	            out.append("/Helvetica findfont\r\n");
	            out.append(transv(345) +" "+ transh(100) + " moveto\r\n");
	            out.append("24 scalefont\r\n");
	            out.append("setfont\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            
            	//Caption for 12 derivations
	            out.append("(C1) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(495) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C2) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(645) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C3) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(795) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C4) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(945) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C5) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(1095) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C6) show\r\n");
	            
	            out.append("grestore\r\n");
    			 
	            //Writing the curves
	            for (int i=6; i<12; i++){
		            int count = 0;
		            for (int x = 240; x < 2740; x++)
		            {            	       	
		            	float y1 = ((lead[count] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + (i-6)*150;
		            	float y2 = ((lead[count+1] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + (i-6)*150;
		            	
		            	float x1 = ((x-240)/2)+240;
		            	float x2 = (float) (x1+0.5);
		            	
		            	out.append("newpath\r\n");
		            	out.append(transv(y1)+" "+transh(x1)+" moveto\r\n");
		            	out.append(transv(y2)+" "+transh(x2)+" lineto\r\n");
		            	out.append("2 setlinewidth\r\n");
		            	out.append("stroke\r\n");
		            	
		            	count = count + 1;
		            	if (count == 497)
		            	{
		            		count = 0;
		            	}	
		            }
	            }
	           
	            //Writing verifiable jags
	            for(int i=0; i<6; i++)
	            {
		            out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(165)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(180)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(180)+" moveto\r\n");
	            	out.append(transv(315+i*150)+" "+transh(180)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(315+i*150)+" "+transh(180)+" moveto\r\n");
	            	out.append(transv(315+i*150)+" "+transh(210)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(315+i*150)+" "+transh(210)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(210)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(210)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(225)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
	           
            	out.append("showpage\r\n");
        	}
        	
        	out.close();
        	
        	//Prompt a status-text: OK
        	Toast.makeText(context, "Generieren: OK!", Toast.LENGTH_LONG).show();
	    }
		catch(Exception e)
		{	
			//Prompt a status-text: ERROR
			Toast.makeText(context, "Fehler beim Generieren!", Toast.LENGTH_LONG).show();
		}
    }
	
	/**********************************************/
	/* Correction-methods: Florian Wimmer, 4AHITI */
	/**********************************************/
	
	//transh-method for correcting the horizontal values
    public float transh(float x)
    {      	
    	return x * scale_ps * scalesite * corrh + offset;
    }
    //transv-method for correcting the vertical values
    public float transv(float x)
    {      	
    	return x * scale_ps* scalesite * corrv + offset;
    }

    public void write_ps(BufferedWriter out, int[][] leadData, int pageCount)
    {  				
		try
		{
			/*****************************************/
        	/* Start drawing the grid and the curves */
    		/*****************************************/
        
			//Writing the Page-header
			out.append("%%Page: " + pageCount + " " + pageCount + "\r\n");
			out.append("0.5 0.5 scale\r\n");
			
			//Set color to magenta
			out.append("0 100 0 0 setcmykcolor\r\n");
			
            //vertical grid-lines
			for (int x = 150; x <= 1500; x = x + fg)
            {   
            	out.append("newpath\r\n");
            	out.append(transv(225) +" "+ transh(x) + " moveto\r\n");
            	out.append(transv(1155) +" "+ transh(x)+ " lineto\r\n");
            	out.append("0.5 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
        
            //horizontal grid-lines
			for (int y = 225 ; y <= 1155; y = y + fg)
            {		            	
            	out.append("newpath\r\n");
            	out.append(transv(y) +" "+ transh(150) + " moveto\r\n");
            	out.append(transv(y) +" "+ transh(1500) + " lineto\r\n");
            	out.append("0.5 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
           	
			//Set color to black
            out.append("0 0 0 100 setcmykcolor\r\n");
            
            //Writing the Caption
            
            out.append("/Helvetica findfont\r\n");
            out.append(transv(345) +" "+ transh(100) + " moveto\r\n");
            out.append("24 scalefont\r\n");
            out.append("setfont\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            

        	//Caption for 6 derivations
            out.append("(I) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(495) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(II) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(645) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(III) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(795) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVR) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(945) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVL) show\r\n");
            out.append("grestore\r\n");
            out.append(transv(1095) +" "+ transh(100) + " moveto\r\n");
            out.append("gsave\r\n");
	        out.append("90 rotate\r\n");
            out.append("(aVF) show\r\n");
            
            out.append("grestore\r\n");
			 
            //Writing the curves
            for (int i=0; i<6; i++){
	            int count = 0;
	            for (int x = 240; x < 2740; x++)
	            {            	       	
	            	float y1 = ((leadData[i][count] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + i*150;
	            	float y2 = ((leadData[i][count+1] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + i*150;
	            	
	            	float x1 = ((x-240)/2)+240;
	            	float x2 = (float) (x1+0.5);
	            	
	            	out.append("newpath\r\n");
	            	out.append(transv(y1)+" "+transh(x1)+" moveto\r\n");
	            	out.append(transv(y2)+" "+transh(x2)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	
	            	count = count + 1;
	            	if (count == 497)
	            	{
	            		count = 0;
	            	}	
	            }
            }
           
            //Writing verifiable jags
            for(int i=0; i<6; i++)
            {
	            out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(165)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(180)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(180)+" moveto\r\n");
            	out.append(transv(315+i*150)+" "+transh(180)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(315+i*150)+" "+transh(180)+" moveto\r\n");
            	out.append(transv(315+i*150)+" "+transh(210)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(315+i*150)+" "+transh(210)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(210)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            	out.append("newpath\r\n");
            	out.append(transv(375+i*150)+" "+transh(210)+" moveto\r\n");
            	out.append(transv(375+i*150)+" "+transh(225)+" lineto\r\n");
            	out.append("2 setlinewidth\r\n");
            	out.append("stroke\r\n");
            }
            
        	out.append("showpage\r\n");
        	
        	// write second page if 12 leads are printed
        	if (AppSettings.leadCount == 12)
        	{
            	out.append("%%Page: " + (pageCount+1) + " " + (pageCount+1) + "\r\n");
            	out.append("0.5 0.5 scale\r\n");
            	//Set color to magenta
    			out.append("0 100 0 0 setcmykcolor\r\n");
    			
	            //vertical grid-lines
    			for (int x = 150; x <= 1500; x = x + fg)
	            {   
	            	out.append("newpath\r\n");
	            	out.append(transv(225) +" "+ transh(x) + " moveto\r\n");
	            	out.append(transv(1155) +" "+ transh(x)+ " lineto\r\n");
	            	out.append("0.5 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
            
	            //horizontal grid-lines
    			for (int y = 225 ; y <= 1155; y = y + fg)
	            {		            	
	            	out.append("newpath\r\n");
	            	out.append(transv(y) +" "+ transh(150) + " moveto\r\n");
	            	out.append(transv(y) +" "+ transh(1500) + " lineto\r\n");
	            	out.append("0.5 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
	           	
    			//Set color to black
	            out.append("0 0 0 100 setcmykcolor\r\n");
	            
	            //Writing the Caption
	            
	            out.append("/Helvetica findfont\r\n");
	            out.append(transv(345) +" "+ transh(100) + " moveto\r\n");
	            out.append("24 scalefont\r\n");
	            out.append("setfont\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            
            	//Caption for 12 derivations
	            out.append("(C1) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(495) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C2) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(645) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C3) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(795) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C4) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(945) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C5) show\r\n");
	            out.append("grestore\r\n");
	            out.append(transv(1095) +" "+ transh(100) + " moveto\r\n");
	            out.append("gsave\r\n");
		        out.append("90 rotate\r\n");
	            out.append("(C6) show\r\n");
	            
	            out.append("grestore\r\n");
    			 
	            //Writing the curves
	            for (int i=6; i<12; i++){
		            int count = 0;
		            for (int x = 240; x < 2740; x++)
		            {            	       	
		            	float y1 = ((leadData[i][count] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + (i-6)*150;
		            	float y2 = ((leadData[i][count+1] * scalefactor) - (512 * scalefactor + 20 * scalefactor)) + 345 + (i-6)*150;
		            	
		            	float x1 = ((x-240)/2)+240;
		            	float x2 = (float) (x1+0.5);
		            	
		            	out.append("newpath\r\n");
		            	out.append(transv(y1)+" "+transh(x1)+" moveto\r\n");
		            	out.append(transv(y2)+" "+transh(x2)+" lineto\r\n");
		            	out.append("2 setlinewidth\r\n");
		            	out.append("stroke\r\n");
		            	
		            	count = count + 1;
		            	if (count == 497)
		            	{
		            		count = 0;
		            	}	
		            }
	            }
	           
	            //Writing verifiable jags
	            for(int i=0; i<6; i++)
	            {
		            out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(165)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(180)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(180)+" moveto\r\n");
	            	out.append(transv(315+i*150)+" "+transh(180)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(315+i*150)+" "+transh(180)+" moveto\r\n");
	            	out.append(transv(315+i*150)+" "+transh(210)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(315+i*150)+" "+transh(210)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(210)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            	out.append("newpath\r\n");
	            	out.append(transv(375+i*150)+" "+transh(210)+" moveto\r\n");
	            	out.append(transv(375+i*150)+" "+transh(225)+" lineto\r\n");
	            	out.append("2 setlinewidth\r\n");
	            	out.append("stroke\r\n");
	            }
	            
            	out.append("showpage\r\n");
        	}
	    }
		catch(Exception e)
		{	
			e.printStackTrace();
		}
    }
    
	@Override
	public void run() {
		try {
			BufferedReader input = new BufferedReader(new FileReader(AppSettings.lastRecord));
			BufferedWriter output = null;
			String s = "";
			String[] tmpArray;
			int leadCount = 0;
			int indexCounter = 0;
			int pageCount = 1;
			int[][] leadData;
			int lines = 0;
			
			// count lines
			while (input.readLine() != null) lines++;
			if(D) Log.e(TAG, "Line Count of record: " + lines);
			input.reset();
			
			if(lines < 1)
			{
				input.close();
				return;
			}

			s = input.readLine();
			if(s.matches(AppSettings.SIXLEADSREGEX))
			{
				leadCount = 6;
			} else {
				if (s.matches(AppSettings.TWELVELEADSREGEX))
				{
					leadCount = 12;
				} else {
					input.close();
					return;
				}
			}
			
			// allocate memory for leadData
			leadData = new int[leadCount][AppSettings.frequency * 10];
			// initialize leadData (original values) 
			for (int i=0; i<leadData.length; i++)
				for (int j=0; j<leadData[0].length; j++)
					leadData[i][j] = 0;
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////			
			//Reading and correcting the month-value
	        final Calendar c = Calendar.getInstance();
    		
	    	output =  new BufferedWriter(new FileWriter("mnt/sdcard/ecg_" + c.get(Calendar.DAY_OF_MONTH)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.YEAR) + "_" + c.get(Calendar.HOUR_OF_DAY)+"-"+c.get(Calendar.MINUTE) + "-" + c.get(Calendar.SECOND) + ".ps"));
	    	output.append("%!PS\r\n");
			output.append("%%Pages: " + ((int)(lines/AppSettings.frequency)*(leadCount/6)) + "\r\n");
			
			//Writing the header-text
    		output.append("/Helvetica findfont\r\n");
	        output.append(transv(60) +" "+ transh(370) + " moveto\r\n");
	        output.append("24 scalefont\r\n");
	        output.append("setfont\r\n");
	        output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
	        output.append("(Name:) show\r\n");
	        output.append("grestore\r\n");
	        
	        //Writing the date
            output.append(transv(110) +" "+ transh(370) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
            output.append("(Datum: "+c.get(Calendar.DAY_OF_MONTH)+". "+(c.get(Calendar.MONTH)+1)+". "+c.get(Calendar.YEAR)+") show\r\n");
            output.append("grestore\r\n");
            
            //Reading, correcting and writing the time-value		            
            output.append(transv(160) +" "+ transh(370) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
	        if(c.get(Calendar.MINUTE)<10)
	        	output.append("(Uhrzeit: "+c.get(Calendar.HOUR_OF_DAY)+":0"+c.get(Calendar.MINUTE)+") show\r\n");
	        else
	        	output.append("(Uhrzeit: "+c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+") show\r\n");
	        output.append("grestore\r\n");
            
            output.append(transv(60) +" "+ transh(770) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
            output.append("(Geburtsdatum:) show\r\n");
	        output.append("grestore\r\n");
            output.append(transv(110) +" "+ transh(770) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
            output.append("(Gr) show\r\n");
            output.append("/odieresis glyphshow\r\n");
            output.append("/germandbls glyphshow\r\n");
            output.append("(e:) show\r\n");
	        output.append("grestore\r\n");
            output.append(transv(160) +" "+ transh(770) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
            output.append("(Masse:) show\r\n");
	        output.append("grestore\r\n");
            output.append(transv(160) +" "+ transh(1170) + " moveto\r\n");
            output.append("gsave\r\n");
	        output.append("90 rotate\r\n");
            output.append("(Arzt:) show\r\n");
	        output.append("grestore\r\n");
			
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////

			do {
				if (leadCount == 6)
				{
					try
					{
						if(D) Log.i(TAG, "fill tmp array");
						// split the data
						tmpArray = s.split("[tabc]");
						
						leadData [0] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[2]) - AppSettings.adcResolution / 2; //-1024 < x < 1024				
						leadData [1] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[3]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;
						leadData [2] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[4]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;										
						leadData [3] [indexCounter] = (leadData [0] [indexCounter] - leadData [2] [indexCounter]) / 2;					
						leadData [4] [indexCounter] = (-leadData [0] [indexCounter] - leadData [1] [indexCounter]) / 2;					
						leadData [5] [indexCounter] = (leadData [1] [indexCounter] + leadData [2] [indexCounter]) / 2;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				else
				{
					try
					{
						if(D) Log.i(TAG, "fill tmp array");
						// split the data
						tmpArray = s.split("[tabcdefghi]");
						
						leadData [0] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[2]) - AppSettings.adcResolution / 2; //-1024 < x < 1024				
						leadData [1] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[3]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;
						leadData [2] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[4]) - AppSettings.adcResolution / 2;//-AppSettings.adcResolutionLowerbound-512;				
						leadData [3] [indexCounter] = (leadData [0] [indexCounter] - leadData [2] [indexCounter]) / 2;				
						leadData [4] [indexCounter] = (-leadData [0] [indexCounter] - leadData [1] [indexCounter]) / 2;				
						leadData [5] [indexCounter] = (leadData [1] [indexCounter] + leadData [2] [indexCounter]) / 2;
						
						leadData [6] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[5]) - AppSettings.adcResolution / 2;
						leadData [7] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[6]) - AppSettings.adcResolution / 2;
						leadData [8] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[7]) - AppSettings.adcResolution / 2;
						leadData [9] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[8]) - AppSettings.adcResolution / 2;
						leadData [10] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[9]) - AppSettings.adcResolution / 2;
						leadData [11] [indexCounter] = AppSettings.adcResolution - Integer.parseInt(tmpArray[10]) - AppSettings.adcResolution / 2;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				indexCounter++;
				
				if(indexCounter == AppSettings.frequency * 10)
				{
					indexCounter = 0;
					write_ps(output, leadData, pageCount);
					pageCount++;
				}
				
			}while((s = input.readLine()) != null );
			
			input.close();
			output.close();
			
			if(D) Log.i(TAG, "created ps!");
			
		} catch (FileNotFoundException e) {
			if (D) Log.e(TAG, "Could not open record!");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 		
	}
}