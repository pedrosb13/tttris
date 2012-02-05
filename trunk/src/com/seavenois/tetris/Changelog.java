package com.seavenois.tetris;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Changelog extends Activity{
	TextView textView;
	public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changelog); 
        textView = (TextView) findViewById(R.id.textViewChangelog);
        textView.setText(readTxt());
    }
	
	private String readTxt(){
	     InputStream inputStream = getResources().openRawResource(R.raw.changelog);	     
	     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();	     
	     int i;
	     try{
	    	 i = inputStream.read();
	    	 while (i != -1){
	    		 byteArrayOutputStream.write(i);
	    		 i = inputStream.read();
	    	 }
	    	 inputStream.close();
	     }catch (IOException e){
	    	 e.printStackTrace();
	     }
	     return byteArrayOutputStream.toString();
	}
}
