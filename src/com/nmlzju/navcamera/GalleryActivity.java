package com.nmlzju.navcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity{

	File[] images;
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

		images = new Hotspot(hotspot_id).getGalleryFiles();
		for(int i = 0; i < images.length; i++){
			String pathName = images[i].getPath();
			ImageView view = new ImageView(gallery.getContext());
			view.setImageBitmap(BitmapFactory.decodeFile(pathName));
			gallery.addView(view);
		}
		
		textButton.setOnClickListener(new TextListener());

		ActivityManager.add(this);  
	}

	class GalleryItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			//调用系统看图程序查看大图
			if(images == null || images.length <= position || position < 0){
				return;
			}
			
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(images[position]));
			startActivity(intent);
		}
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
