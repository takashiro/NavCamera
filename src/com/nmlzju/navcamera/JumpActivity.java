package com.nmlzju.navcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.nmlzju.navcamera.R;

public class JumpActivity extends Activity {

	ImageButton galleryButton, videoButton;
	String name;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jump);
		
		Intent intent = getIntent();
		name = intent.getStringExtra("hotspot_id");

		galleryButton = (ImageButton) findViewById(R.id.ButtonImg);
		videoButton = (ImageButton) findViewById(R.id.ButtonVdo);

		galleryButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JumpActivity.this, ImgActivity.class);
				intent.putExtra("hotspot_id", name);
				startActivity(intent);
			}
		});

		videoButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String videoPath = HotspotManager.getHotspotVideoPath(name);
				File dir = new File(videoPath);
				if(dir.isDirectory()){
					File[] file = dir.listFiles();
					
					if(file != null && file.length > 0){
						Intent browser = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file[0]));
						startActivity(browser);
						return;
					}
				}
				
				Toast.makeText(JumpActivity.this, "没有相关视频！", Toast.LENGTH_LONG).show();
			}
		});
		
		ActivityManager.add(this);  
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Log.i("ygy", "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent it = new Intent(JumpActivity.this, CameraActivity.class);
			startActivity(it);
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}