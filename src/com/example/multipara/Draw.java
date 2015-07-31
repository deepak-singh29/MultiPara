package com.example.multipara;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;

public class Draw extends View{
	
	public Draw(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    /*
     * ******Draw Methods************
     */
    
	private float convertCm2PxX(float cm)
	{
		return (float) (cm*((AppSettings.screenXdpi/2.54)));
	}
	
	private float convertCm2PxY(float cm)
	{
		return (float) (cm*((AppSettings.screenYdpi/2.54)));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		this.drawGrid(canvas);
		
		this.drawLead(canvas);		
		
		this.drawHeadline(canvas);
		
		invalidate();
	}
	
	private void drawGrid(Canvas canvas)
	{
		//0.5 cm grid
		Paint p=new Paint();
		p.setColor(Color.GRAY);
		p.setAlpha(40);
		p.setStrokeWidth(1);
		
		//draw horizontal line (0.5cm)
		float height=AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm);
		
		if(height<canvas.getHeight())
		{
			height=canvas.getHeight();
		}
		
		float fiveMmX = this.convertCm2PxX((float) 0.5);		
		for(float x=fiveMmX;x<=canvas.getWidth();x+=fiveMmX)
		{
			canvas.drawLine(x, 0, x, height, p);
		}
		
		//draw vertical line (0.5cm)
		float fiveMmY=this.convertCm2PxY((float) 0.5);		
		for(float y=fiveMmY;y<=height;y+=fiveMmY)
		{
			canvas.drawLine(0, y, canvas.getWidth(), y, p);
		}
		
		//1 cm grid
		p.setColor(Color.GRAY);
		p.setAlpha(60);
		
		//draw horizontal line (1cm)
		float tenMmX=(float) ((AppSettings.screenXdpi/2.54));
		for(float x=tenMmX;x<=canvas.getWidth();x+=tenMmX)
		{
			canvas.drawLine(x, 0, x, height, p);
		}
		
		//draw vertical line (1cm)
		float tenMmY=(float) ((AppSettings.screenYdpi/2.54));
		for(float y=tenMmY;y<=height;y+=tenMmY)
		{
			canvas.drawLine(0, y, canvas.getWidth(), y, p);
		}
	}
	
	private void drawLeadAxis(Canvas canvas, float leftPad, float upperPad, String headline, int leadIndex)
	{		
		Typeface tf = Typeface.create("Courier New",0);
		Paint p = new Paint();
		p.setColor(Color.BLACK);
		p.setStrokeWidth(1);
		p.setTextSize(15);
		p.setTypeface(tf);
		
		//draw headline
		canvas.drawText(headline, leftPad, upperPad-AppSettings.convertCm2PxY(AppSettings.betweenTextLeadPadCm), p);
		if(AppSettings.formatColumns==1)
		{
			canvas.drawLine(AppSettings.convertCm2PxX(AppSettings.leftPadCm), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), p);
			canvas.drawLine(AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-1-(float)0.5), p);
			canvas.drawLine(AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-1-(float)0.5), AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25+(float)0.5), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-1-(float)0.5), p);
			canvas.drawLine(AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25+(float)0.5), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-1-(float)0.5), AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25+(float)0.5), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), p);
			canvas.drawLine(AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25+(float)0.5), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), AppSettings.convertCm2PxX(AppSettings.leftPadCm+(float)0.25+(float)0.5+(float)0.25), AppSettings.convertCm2PxY(AppSettings.upperPadCm+AppSettings.leadHeightCm*AppSettings.paintLeadCount+AppSettings.betweenLeadPadCm*(AppSettings.paintLeadCount-1)+AppSettings.lowerPadCm-(float)0.5), p);
		}
		else
		{
			canvas.drawLine(leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2), leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds)+AppSettings.convertCm2PxX((float)0.25), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2),p);
			canvas.drawLine(leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds)+AppSettings.convertCm2PxX((float)0.25), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2),leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds)+AppSettings.convertCm2PxX((float)0.25), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2) - AppSettings.convertCm2PxY(1) ,p);
			canvas.drawLine(leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2) - AppSettings.convertCm2PxY(1), leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25)+ AppSettings.convertCm2PxX((float)0.5), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2) - AppSettings.convertCm2PxY(1),p);
			canvas.drawLine(leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25)+ AppSettings.convertCm2PxX((float)0.5), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2) - AppSettings.convertCm2PxY(1),leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25)+ AppSettings.convertCm2PxX((float)0.5), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2),p);
			canvas.drawLine(leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25)+ AppSettings.convertCm2PxX((float)0.5), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2),leftPad + this.convertCm2PxX(AppSettings.forwardSpeed*AppSettings.seconds) + AppSettings.convertCm2PxX((float)0.25)+ AppSettings.convertCm2PxX((float)0.5)+ AppSettings.convertCm2PxX((float)0.25), upperPad+this.convertCm2PxY(AppSettings.leadHeightCm/2),p);
		}
	}
	
	private void drawLead(Canvas canvas)
	{
		/*
		 ---------------------------------------------------------------------------
		 |
		 |
		 ---------------------------------------------------------------------------
		 |
		 |
		 ---------------------------------------------------------------------------
		 */
		
		for(int i=0;i<AppSettings.paintLeadCount;i++)
		{
			this.drawLeadAxis(canvas, AppSettings.leads[i].getLeftPadPx(), AppSettings.leads[i].getUpperPad(),AppSettings.leadDescription[i], AppSettings.leads[i].getLeadNumber());
		}
		
		Paint p=new Paint();
		p.setColor(Color.RED);
		p.setStrokeWidth(1);
		
		for(int i=0; i < AppSettings.paintLeadCount; i++)
		{
			canvas.drawLines(AppSettings.leads[i].getPaintArray(), 0, AppSettings.currentPaintIndex, p);
			if((AppSettings.leads[i].getPaintArray().length-1-AppSettings.currentPaintIndex-AppSettings.gaping)>0)
				canvas.drawLines(AppSettings.leads[i].getPaintArray(), AppSettings.currentPaintIndex+AppSettings.gaping, AppSettings.leads[i].getPaintArray().length-1-(AppSettings.currentPaintIndex+AppSettings.gaping), p);
		}
	}
	
	private void drawHeadline(Canvas canvas)
	{
		//headline
		Paint p=new Paint();
		p.setColor(Color.BLACK);
		p.setStrokeWidth(1);
		p.setTextSize(15);
		Typeface tf = Typeface.create("Courier New",0);
		p.setTypeface(tf);
		if(AppSettings.heartRate < 0 || AppSettings.heartRate > 240)
			canvas.drawText("Forward Speed: " + AppSettings.forwardSpeed +" cm/sec   Heart Rate: FATAL!!!   "+AppSettings.seconds+" seconds are displayed",
					AppSettings.convertCm2PxY((float) 0.5) , AppSettings.convertCm2PxY((float) 0.4), p);
		else
			canvas.drawText("Forward Speed: " + AppSettings.forwardSpeed +" cm/sec   Heart Rate: " + AppSettings.heartRate+"   "+AppSettings.seconds+" seconds are displayed",
					AppSettings.convertCm2PxY((float) 0.5) , AppSettings.convertCm2PxY((float) 0.4), p);
	}
}

