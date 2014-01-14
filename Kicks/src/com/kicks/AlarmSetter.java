package com.kicks;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;

public class AlarmSetter extends BroadcastReceiver{

	long _day = 86400000; 
	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("Alarm Setter called here- reset alarm on restart");
		
		DBSettings db = new DBSettings(context);
		db.open();
		Cursor c = db.fetchAllAlarmRecords();
		
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		
		Intent i = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, R.id.alarmTitle, i, PendingIntent.FLAG_UPDATE_CURRENT);
		
		if(c.getCount()>0){
		c.moveToFirst();
			
			
			Calendar set = Calendar.getInstance();
			set.set(Calendar.HOUR_OF_DAY, c.getInt(2));
			set.set(Calendar.MINUTE, c.getInt(1));
			
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
		c.close();
		db.close();
	}

}
