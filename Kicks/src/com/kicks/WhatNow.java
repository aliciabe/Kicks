package com.kicks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WhatNow extends Activity{

	@Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.what_now);
	}
	
	public void goKickAgain(View v){
    	Intent i = new Intent(WhatNow.this, KicksCountAgain.class);
        startActivity(i);
	}
	
    @Override
    public void onBackPressed() {

       return;
    }
}
