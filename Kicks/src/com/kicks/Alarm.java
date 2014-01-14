package com.kicks;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TimePicker.OnTimeChangedListener;

public class Alarm extends Activity{
	long _day = 86400000; 
	Button setAlarm;
	CheckBox alarmOn;
	CheckBox soundOn;
	CheckBox vibrateOn;
	AlarmManager am;
	PendingIntent sender;
	TimePicker mTimePicker;
	Calendar setTime;
	int hours = 0;
	int mins = 0;
	
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
		 setContentView(R.layout.alarm_settings);
		// get a Calendar object with current time
	     
	 	alarmOn = (CheckBox)findViewById(R.id.alarm_on);
	 	soundOn = (CheckBox)findViewById(R.id.sound_on);
	 	vibrateOn = (CheckBox)findViewById(R.id.vibrate_on);
		 am = (AlarmManager) getSystemService(ALARM_SERVICE);
		 mTimePicker = (TimePicker)findViewById(R.id.alarmTime);		
		 initializeTimePicker();
		 setTime = Calendar.getInstance();

		 Intent intent = new Intent(Alarm.this, AlarmReceiver.class);
		 sender = PendingIntent.getBroadcast(this, R.id.alarmTitle, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 //Get alarm info from DB for initial setup of TimePicker
		 
		 
		 
		 alarmOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//Checked
				setAlarmRecord();
				if(buttonView.isChecked()){
					alarmOn();
				}
				
				//Unchecked
				if(!buttonView.isChecked()){
					alarmOff();
				}
			}
		 });
		 
		 vibrateOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//Checked
					setAlarmRecord();					
				}
			 });
		 
		 soundOn.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//Checked
					setAlarmRecord();					
				}
			 });
		 
		 mTimePicker.setOnTimeChangedListener(new OnTimeChangedListener(){

			@Override
			public void onTimeChanged(TimePicker time, int hour, int minute) {
				System.out.println("HOURS "+hour+"MINUTE"+minute);
				
				hours = hour;
				mins = minute;
				setTime.set(Calendar.MINUTE, minute);
				setTime.set(Calendar.HOUR, hour);
				System.out.println("cHOURS "+Calendar.HOUR);
				setAlarmRecord();
				if(alarmOn.isChecked()){
					alarmOn();
				}
				
			}			 
		 });
		 

	}

	
	
	public void alarmOff(){
		am.cancel(sender);		
	}
	
	public void alarmOn(){		
		
		Calendar set = Calendar.getInstance();
		set.set(Calendar.HOUR_OF_DAY, hours);
		set.set(Calendar.MINUTE, mins);
		
		long diff = set.getTimeInMillis()-System.currentTimeMillis();

		//If this time has already happened today, add a day to the time so the alarm will go off tomorrow
		if(diff<0){
		 diff+=_day;
		}
		if(diff>_day){
			diff-=_day;
		}
		
		System.out.println("***Milliseconds until alarm: "+diff);
		
		diff += SystemClock.elapsedRealtime();

		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, diff, _day, sender);

	}
	
	public void setAlarmRecord(){
		DBSettings db = new DBSettings(this);
		db.open();
		
		
		//put record in DB
		db.deleteAllAlarm();

		db.createAlarmRecord( hours, mins, vibrateOn.isChecked(), soundOn.isChecked(), alarmOn.isChecked());
		
		db.close();
	}
	
	public void toastAlarmOff(){
		String message = "The Alarm is Off";
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public void initializeTimePicker(){
		DBSettings db = new DBSettings(this);
		db.open();
		Cursor c = db.fetchAllAlarmRecords();
		
		//There are no stored records, so initialize it to current settings
		if(c.getCount()==0){
			c.close();
			db.close();
			if(alarmOn.isChecked()){
				alarmOn();
			}
		}
		
		//Initialize settings to what is stored in the DB
		else{
			
				c.moveToFirst();
				
				System.out.println("0: "+c.getInt(0)+"\n1: "+c.getInt(1)+"\n3: "+c.getInt(3)+" " +c.getColumnName(3)+"\n4: "+c.getInt(4) +" " +c.getColumnName(4));
				mTimePicker.setCurrentHour(c.getInt(2));
				mTimePicker.setCurrentMinute(c.getInt(1));
				
				
				
				if(c.getInt(5)==0){
					System.out.println("Alarm Is off initialization");
					toastAlarmOff();
					alarmOn.setChecked(false);					
				}
				else{
					alarmOn.setChecked(true);					
				}
				
				if(c.getInt(3)==0){
					vibrateOn.setChecked(false);
				}
				else{
					vibrateOn.setChecked(true);
				}
				
				if(c.getInt(4)==0){
					soundOn.setChecked(false);
				}
				else{
					soundOn.setChecked(true);
				}
				
				c.close();
				db.close();
		}
	}

}
