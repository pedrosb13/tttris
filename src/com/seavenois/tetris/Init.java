package com.seavenois.tetris;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Init extends Activity {
	
	private Button btnNewGame;
	
    /** Called when the activity is first created. */    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Assign layouts
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init);
        //Find and assign buttons
        btnNewGame = (Button) findViewById(R.id.buttonNewGame);
        btnNewGame.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
        		startActivity(intent);
			}
        });
    }
}