package com.ygy;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;

public class JumpActivity extends Activity {

	ImageButton Bimg, Bvdo;
	String name;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jump);
		Resources res = getResources();
		Intent gIntent; // Reusable Intent for each tab
		// Create an Intent to launch an Activity for the tab (to be reused)
		gIntent = getIntent();
		name = gIntent.getStringExtra("name");

		Bimg = (ImageButton) findViewById(R.id.ButtonImg);
		Bvdo = (ImageButton) findViewById(R.id.ButtonVdo);

		Bimg.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JumpActivity.this, ImgActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});

		Bvdo.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JumpActivity.this, LocalVideoActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
		
		ActivityStackControlUtil.add(this);  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Log.i("ygy", "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent it = new Intent(JumpActivity.this, MyCameraActivity.class);
			startActivity(it);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}