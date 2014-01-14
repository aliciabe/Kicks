package com.kicks;


import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;

public class KicksCountAgain extends Activity {
	public int numberOfKicks=0;
	public static int _kicksGoal=10;
	public static long _timeLimit = 10000;
	Chronometer time;
	private DBHandler db;
	private Context kc;
	public final String SECOND = "second_try";
	ChronometerServiceInterface csInterface;
	ServiceConnection mConnection;
	Intent myService;
	boolean stopService = false;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
    	super.onCreate(savedInstanceState);
       
    	mConnection = new ServiceConnection()
        {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				csInterface = ChronometerServiceInterface.Stub.asInterface((IBinder)service);

				try {
					csInterface.setIsFirst(false);
					csInterface.enableNotification();
					time.start();
					if(time.getBase()-csInterface.getStartTime()>_timeLimit){
						time.setBase(time.getBase()-_timeLimit);
						System.out.println("OVER TIME");
					}
					else{
					time.setBase(csInterface.getStartTime());
					}
					
					numberOfKicks = csInterface.getNumberOfKicks();
					
					TextView numKicks = (TextView) findViewById(R.id.numberOfKicks);
			    	numKicks.setText(""+numberOfKicks, TextView.BufferType.EDITABLE);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				try {
					csInterface.setKicks(numberOfKicks);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				csInterface = null;
				System.out.println("SERVICE DISCONNECTED HERE");
				
			}
        };
        

    	myService = new Intent(KicksCountAgain.this, ChronometerService.class);
    	startService(myService);
    	this.bindService(new Intent(KicksCountAgain.this, ChronometerService.class), mConnection, Context.BIND_AUTO_CREATE);

    	
    	kc = this;
    	
    	setContentView(R.layout.kickcounter);

    	time = (Chronometer) findViewById(R.id.chronometer);
    	//time.setBase(0);
    	
    	time.setOnChronometerTickListener(new OnChronometerTickListener(){
    		;
			@Override
			public void onChronometerTick(Chronometer chronometer) {
				if(numberOfKicks>=_kicksGoal){
					time.stop();
					kicksDone();
					
					//Get total time in minutes
					int stopTime = (int) ((SystemClock.elapsedRealtime()-time.getBase())/60000);
					
					db = new DBHandler(kc);
					db.open();
					
					db.deleteAll(SECOND);

					
					Date d1 = new Date();
					d1.setDate(17);
					String date1 = DateFormat.getDateInstance().format(d1);
					
					db.createRecord(date1, 89, SECOND);
					
					d1 = new Date();
					d1.setDate(21);
					date1 = DateFormat.getDateInstance().format(d1);
					
					db.createRecord(date1, 40, SECOND);

					Date d = new Date();
					String date = DateFormat.getDateInstance().format(d);
					
					//long id = db.createRecord(date, stopTime, SECOND);
					
					//db.createRecord(date, 40, SECOND);
					db.close();
					
					//System.out.println("INSERTED RECORD "+id+": "+date+", "+stopTime+", "+FIRST);
					
				}
				
				if((SystemClock.elapsedRealtime()-time.getBase())>_timeLimit && numberOfKicks<_kicksGoal){
					time.stop();
					timesUp();
				}
				

                //textGoesHere is a TextView
              //  ((TextView)findViewById(R.id.)).setText(asText);
				
			}
    	});
        
        time.start();
    }
    
    @Override
    public void onBackPressed() {
    	Intent i = new Intent(KicksCountAgain.this, Kicks.class);
        startActivity(i);
       return;
    }
    
    @Override
    public void onStop(){
    	System.out.println("Activity stopped");
    	
    	if(!stopService){
    	try {
			csInterface.setKicks(numberOfKicks);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    	
    	 this.unbindService(mConnection);
    	} 
    	
    	super.onStop();
    }
    
    public void kick(View v){
    	if(numberOfKicks<_kicksGoal){
    	numberOfKicks++;
    	}

    	TextView numKicks = (TextView) findViewById(R.id.numberOfKicks);
    	numKicks.setText(""+numberOfKicks, TextView.BufferType.EDITABLE);
    }
    
    
    public void kicksDone(){
    	this.unbindService(mConnection);
    	stopService = true;
    	stopService(myService);
    	 AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
         
         // set the message to display
         alertbox.setMessage("You have reached "+_kicksGoal+" kicks!");

         // add a neutral button to the alert box and assign a click listener
         alertbox.setNeutralButton("Stats", new DialogInterface.OnClickListener() {

             // click listener on the alert box
             public void onClick(DialogInterface arg0, int arg1) {
                 // the button was clicked
             	Intent i = new Intent(KicksCountAgain.this, Graph.class);
                 startActivity(i);
             }
         });
         alertbox.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {

             // click listener on the alert box
             public void onClick(DialogInterface arg0, int arg1) {
                 // the button was clicked
             	Intent i = new Intent(KicksCountAgain.this, Kicks.class);
                 startActivity(i);
             }
         });

         // show it
         alertbox.show();
    }
    
    public void timesUp(){
    	//Close all connections to the service
    	this.unbindService(mConnection);
    	stopService(myService);
    	stopService = true;
    	
    	AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        
        // set the message to display
        alertbox.setMessage("You did not reach "+_kicksGoal+" in time again. Please seek medical attention immediately.");

       
        //show it
        alertbox.show();
    }
 

}
