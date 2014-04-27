package com.nmlzju.navcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GalleryActivity extends Activity{

	LinearLayout gallery;
	Button textButton;
	String hotspot_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		Intent intent = getIntent();

		// 获得Gallery对象
		gallery = (LinearLayout) findViewById(R.id.gallery);
		textButton = (Button)findViewById(R.id.text);

		hotspot_id = intent.getStringExtra("hotspot_id");

		File[] images = new Hotspot(hotspot_id).getGalleryFiles();
		if(images != null){
			for(int i = 0; i < images.length; i++){
				Log.i("gallery", "loading " + images[i].getPath());
				ImageView view = new ImageView(gallery.getContext());
				//TODO 加载过大的图片会导致崩溃
				Bitmap bitmap = BitmapFactory.decodeFile(images[i].getPath());
				if(bitmap != null){
					view.setImageBitmap(bitmap);
				}
				gallery.addView(view);
			}
		}
		
		textButton.setOnClickListener(new TextListener());

		ActivityManager.add(this);  
	}
	
	class TextListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(GalleryActivity.this, TextActivity.class);
			intent.putExtra("hotspot_id", hotspot_id);
			startActivity(intent);
		}	
	}
}
