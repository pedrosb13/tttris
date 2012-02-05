package com.seavenois.tetris;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;

public class Donate extends Activity{
        WebView webview;
        @Override 
        public void onCreate(Bundle savedInstanceState) { 
                super.onCreate(savedInstanceState); 
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.donate); 
                ImageButton btn = (ImageButton)findViewById(R.id.donateButton);
                Uri uriUrl = Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=9GDLVHX2357DQ");
                final Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl); 
                btn.setOnClickListener(new OnClickListener(){
                public void onClick(View v){
                        startActivity(launchBrowser);  
                }
            });
        }
}