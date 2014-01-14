package com.kicks;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager mNotificationManager;
    private int SIMPLE_NOTFICATION_ID = R.layout.alarm_settings;
    
    @Override
    public void onReceive(Context context, Intent intent) {

      mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
    Notification notifyDetails = new Notification(R.drawable.icon,"Time to Count Kicks!",System.currentTimeMillis());
    PendingIntent myIntent = PendingIntent.getActivity(context, 0, new Intent(context, Kicks.class), 0);
    notifyDetails.setLatestEventInfo(context, "Kicker Counter", "Time to Count Kicks!", myIntent);
    notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
    notifyDetails.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
    
    DBSettings db = new DBSettings(context);
    db.open();
    db.fetchAllAlarmRecords();
    Cursor c = db.fetchAllAlarmRecords();
    c.moveToFirst();
    
    if(c.getInt(3)!=0){
    long[] vibrate = {0,400,100, 400, 100, 400, 500, 400,100, 400, 100, 400};
    notifyDetails.vibrate = vibrate;
    }

    if(c.getInt(4) !=0){
    notifyDetails.defaults |= Notification.DEFAULT_SOUND;
    }
    
    
    mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
    
    c.close();
    db.close();
    

    }
}