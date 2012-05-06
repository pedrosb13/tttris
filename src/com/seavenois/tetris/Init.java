package com.seavenois.tetris;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

/*************************************************/
/* Main menu activity ****************************/
/*************************************************/
public class Init extends Activity {
	
	//Buttons
	private Button btnNewGame, btnResumeGame, btnHighScores;
	
	/*************************************************/
	/* On create *************************************/
	/*************************************************/
	/* Sets ui elements ******************************/
	/*************************************************/ 
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	//Assign layouts
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
        btnResumeGame = (Button) findViewById(R.id.buttonResumeGame);
        btnResumeGame.setEnabled(false); //Disabled (unimplemented feature)
        btnResumeGame.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Game"));
        		//TODO: Something to load a game. First I need to develop something to save the game
        		startActivity(intent);
			}
        });
        btnHighScores = (Button) findViewById(R.id.buttonHighScores);
        btnHighScores.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
        		Intent intent = new Intent();
        		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.HighScores"));
        		startActivity(intent);
			}
        });      
    }
	
	/*************************************************/
	/* Create the options menu ***********************/
	/*************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.initmenu, menu);
        return true;
    }
    
    /*************************************************/
	/* Set actions for each menu element *************/
	/*************************************************/
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        intent = new Intent();
		switch(item.getItemId()){
                case R.id.menuItemChangelog:
                        intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Changelog"));
                        break;
                case R.id.menuItemPreferences:
                        intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Preferences"));
                        break;
                case R.id.menuItemDonate:
                		intent.setComponent(new ComponentName("com.seavenois.tetris", "com.seavenois.tetris.Donate"));
                        break;
        }
		startActivity(intent);
        return true;
    }
}