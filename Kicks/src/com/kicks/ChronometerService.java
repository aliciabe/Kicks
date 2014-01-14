package com.kicks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;


public class ChronometerService extends Service{

	private Chronometer time;
	private int kick = 0;
	private boolean isFirst = true;
	
	private NotificationManager nm;
	private static final int NOTIFY_ID = R.layout.kickcounter;
	private Context ctx;
	
	private OnChronometerTickListener tickTock = new OnChronometerTickListener(){

		@Override
		public void onChronometerTick(Chronometer arg0) {
			
			
		}
		
		
	};

	public class LocalBinder extends Binder{
		ChronometerService getService(){
			return ChronometerService.this;
		}
	}
	
	@Override
	public void onCreate() {
		
		System.out.println("NEW SERVICE CREATED");
		super.onCreate();

		ctx = this;
		time  = new Chronometer(ChronometerService.this);		
		time.start();
		System.out.println("THE SERVICE CHRONOMETER START TIME: "+time.getBase());
	}
	
	@Override
	public void onDestroy() {
		time.stop();
		kick = 0;
		nm.cancel(NOTIFY_ID);
		System.out.println("SERVICE IS DESTROYED");
	}

	
	@Override
	public IBinder onBind(Intent intent) {		
	
		return mBinder;
	}
	
	private final Binder mBinder = new ChronometerServiceInterface.Stub() {
		
		@Override
		public void setStartTime(long startTime) throws RemoteException {
			time.setBase(startTime);
			
		}
		
		@Override
		public void setKicks(int kicks) throws RemoteException {
			kick = kicks;
			
		}
		
		@Override
		public long getStartTime() throws RemoteException {
			return time.getBase();
		}
		
		@Override
		public int getNumberOfKicks() throws RemoteException {
			return kick;
		}

		@Override
		public void incrementKicks() throws RemoteException {
			kick++;
			
		}

		@Override
		public boolean getIsFirst() throws RemoteException {
			// TODO Auto-generated method stub
			return isFirst;
		}

		@Override
		public void setIsFirst(boolean isF) throws RemoteException {
			isFirst = isF;
			
		}

		@Override
		public void enableNotification() throws RemoteException {
			nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			
			int icon = R.drawable.playbackstart;
			CharSequence tickerText = "Kick Counter is active";
			long when = SystemClock.elapsedRealtime()-time.getBase();
			Notification notification = new Notification(icon, tickerText, when);

			Context context = getApplicationContext();
			CharSequence contentTitle = "Kick Counter";
			CharSequence contentText = "Keep counting kicks!";
			Intent notificationIntent;
			if(isFirst){
				notificationIntent = new Intent(ctx, KicksCount.class);
			}
			else{
				notificationIntent = new Intent(ctx, KicksCountAgain.class);
			}
			PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			
			nm.notify(NOTIFY_ID, notification);
			
		}
		
	};

		
}
