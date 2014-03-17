package com.ygy;

import java.util.ArrayList;
import java.util.List;

import com.ygy.WaitActivity.MySurfaceView.MyThread;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;

public class FirstActivity extends Activity {
	/** Called when the activity is first created. */

	 private Handler mHandler = new Handler() { 
         
         public void handleMessage(android.os.Message msg) { 
     		Intent intent = new Intent(FirstActivity.this, SlidePageActivity.class);
    		startActivity(intent);
                 finish();
               } 
             
         };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);

		new Thread(new Runnable()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(500);
				        mHandler.sendEmptyMessage(0); 
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}).start();
		

	}

}