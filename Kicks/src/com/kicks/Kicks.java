package com.kicks;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Kicks extends Activity {
	public int numberOfKicks=0;
	public static int _kicksGoal=10;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
       
        
        setContentView(R.layout.main);
        
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.alarm:
        	goAlarm();
            return true;
        case R.id.notification:
          
            return true;
        case R.id.info:
            
            return true;
        case R.id.graph:
            goGraph();
            return true;            
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    public void goKick(View v){
    	Intent i = new Intent(Kicks.this, KicksCount.class);
        startActivity(i);

    }

    public void goInstructions(View v){
    	Intent i = new Intent(Kicks.this, Alarm.class);
        startActivity(i);
    }
    
    public void goGraph(View v){
    	Intent i = new Intent(Kicks.this, Graph.class);
    	startActivity(i);
    }
    
    public void goGraph(){
    	Intent i = new Intent(Kicks.this, Graph.class);
    	startActivity(i);
    }
    
    public void goAlarm(){
    	Intent i = new Intent(Kicks.this, Alarm.class);
    	startActivity(i);
    }
}