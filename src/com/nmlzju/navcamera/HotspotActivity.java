package com.nmlzju.navcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nmlzju.navcamera.R;

public class HotspotActivity extends Activity {

	String name;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hotspot);
		
		Intent intent = getIntent();
		name = intent.getStringExtra("hotspot_id");
		Hotspot hotspot = new Hotspot(name);

		TextView textView = (TextView) findViewById(R.id.hotspot_name);
		textView.setText(hotspot.getName());
		
		ImageButton galleryButton = (ImageButton) findViewById(R.id.ButtonImg);
		ImageButton videoButton = (ImageButton) findViewById(R.id.ButtonVdo);

		galleryButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HotspotActivity.this, GalleryActivity.class);
				intent.putExtra("hotspot_id", name);
				startActivity(intent);
			}
		});

		videoButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String videoPath = new Hotspot(name).getVideoPath();
				File dir = new File(videoPath);
				if(dir.isDirectory()){
					File[] file = dir.listFiles();
					
					if(file != null && file.length > 0){
						Intent browser = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file[0]));
						startActivity(browser);
						return;
					}
				}
				
				Toast.makeText(HotspotActivity.this, getString(R.string.hotspot_has_no_videos), Toast.LENGTH_LONG).show();
			}
		});
		
		ActivityManager.add(this);  
	}
}
