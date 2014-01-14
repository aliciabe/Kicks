package com.kicks;


import java.text.DateFormat;
import java.util.Date;

import com.kicks.ChronometerService.LocalBinder;

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

public class KicksCount extends Activity {
	public int numberOfKicks=0;
	public static int _kicksGoal=10;
	
	//timeLimit is the timer's limit in milliseconds
	//7200000 = 2 hours
	public static long _timeLimit = 7200000;
	Chronometer time;
	private DBHandler db;
	private Context kc;
	public final String FIRST = "first_try";
	ChronometerServiceInterface csInterface;
	ServiceConnection mConnection;
	Intent myService;
	boolean stopService = false;
	
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.kickcounter);

    	
    	mConnection = new ServiceConnection()
        {

			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				csInterface = ChronometerServiceInterface.Stub.asInterface((IBinder)service);
				
		
				try {
					//Initialize the variables in the service and the variables dependent on the service
					csInterface.setIsFirst(true);
					csInterface.enableNotification();
					time.start();
					if(time.getBase()-csInterface.getStartTime()>_timeLimit){
						
						//some logging to the console
						System.out.println("base time: "+time.getBase()+" | start time: "+csInterface.getStartTime());
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

    	myService = new Intent(KicksCount.this, ChronometerService.class);
    	startService(myService);
    	this.bindService(myService, mConnection, Context.BIND_AUTO_CREATE);
    	
	//		System.out.println("-----BASE TIME: "+csInterface.getStartTime());
		
    	
    	kc = this;
    	time = (Chronometer) findViewById(R.id.chronometer);
    	//long base = csInterface.getStartTime();
		//String cs = csInterface.toString();
		
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
					
					db.deleteAll(FIRST);

					/*Sample data
					Date d1 = new Date();
					d1.setDate(16);
					String date1 = DateFormat.getDateInstance().format(d1);
					
					db.createRecord(date1, 110, FIRST);
					
					d1.setDate(17);
					date1 = DateFormat.getDateInstance().format(d1);
					
					db.createRecord(date1, 89, FIRST);
					
					d1 = new Date();
					d1.setDate(18);
					date1 = DateFormat.getDateInstance().format(d1);
					db.createRecord(date1, 20, FIRST);
					
					
					d1 = new Date();
					d1.setDate(19);
					date1 = DateFormat.getDateInstance().format(d1);
					db.createRecord(date1, 30, FIRST);
					d1 = new Date();
					d1.setDate(20);
					date1 = DateFormat.getDateInstance().format(d1);
					db.createRecord(date1, 40, FIRST);
					
					d1 = new Date();
					d1.setDate(21);
					date1 = DateFormat.getDateInstance().format(d1);
					
					db.createRecord(date1, 40, FIRST);
					
	*/
					
					Date d = new Date();
					String date = DateFormat.getDateInstance().format(d);
					
					long id = db.createRecord(date, stopTime, FIRST);

					db.close();
					
					System.out.println("INSERTED RECORD "+id+": "+date+", "+stopTime+", "+FIRST);
					
				}
				
				if((SystemClock.elapsedRealtime()-time.getBase())>_timeLimit && numberOfKicks<_kicksGoal){
					time.stop();
					System.out.println("TIMES UP CALLED HERE");
					timesUp();
				}
				

                //textGoesHere is a TextView
              //  ((TextView)findViewById(R.id.)).setText(asText);
				
			}
    	});
        
        time.start();
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
             	Intent i = new Intent(KicksCount.this, Graph.class);
                 startActivity(i);
             }
         });
         alertbox.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {

             // click listener on the alert box
             public void onClick(DialogInterface arg0, int arg1) {
                 // the button was clicked
             	Intent i = new Intent(KicksCount.this, Kicks.class);
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
        alertbox.setMessage("You did not reach "+_kicksGoal+" in time.");

        // add a neutral button to the alert box and assign a click listener
        alertbox.setPositiveButton("I wasn't paying attention.", new DialogInterface.OnClickListener() {

            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
            	Intent i = new Intent(KicksCount.this, KicksCount.class);
                startActivity(i);
            }
        });
        
        alertbox.setNegativeButton("What do I do now?", new DialogInterface.OnClickListener() {

            // click listener on the alert box
            public void onClick(DialogInterface arg0, int arg1) {
                // the button was clicked
            	Intent i = new Intent(KicksCount.this, WhatNow.class);
                startActivity(i);
            }
        });
        //show it
        alertbox.show();
    }
 

}
