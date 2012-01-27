package com.seavenois.tetris;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class HighScores extends Activity{

	public void onCreate(Bundle savedInstanceState) {
		
		//TextViews
		TextView hs1, hs2, hs3;
		
		//Saved values
		SharedPreferences highScores;
		
    	//Assign layouts
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.highscores);
        
        //Assign textViews
        hs1 = (TextView) findViewById(R.id.textViewHighScore1);
        hs2 = (TextView) findViewById(R.id.textViewHighScore2);
        hs3 = (TextView) findViewById(R.id.textViewHighScore3);
        
        //Load high scores
        highScores = getSharedPreferences("highScores", 0);
		if (highScores.getInt("hScore1", 0) > 0)
			hs1.setText(getResources().getString(R.string.first) + ": " + Integer.toString(highScores.getInt("hScore1", 0)));
		if (highScores.getInt("hScore2", 0) > 0)
			hs2.setText(getResources().getString(R.string.first) + ": " + Integer.toString(highScores.getInt("hScore2", 0)));
		if (highScores.getInt("hScore3", 0) > 0)
			hs3.setText(getResources().getString(R.string.first) + ": " + Integer.toString(highScores.getInt("hScore3", 0)));
		
	}
}
