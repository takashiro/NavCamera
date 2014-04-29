package com.nmlzju.navcamera;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gallery);

		// 获得Gallery对象
		gallery = (LinearLayout) findViewById(R.id.gallery);
		textButton = (Button)findViewById(R.id.text);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int imageSize = dm.heightPixels - 40;
		
		Intent intent = getIntent();
		hotspot_id = intent.getStringExtra("hotspot_id");

		File[] images = new Hotspot(hotspot_id).getGalleryFiles();
		if(images != null){
			
			for(int i = 0; i < images.length; i++){
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(images[i].getPath(), options);
				options.inJustDecodeBounds = false;
				options.inSampleSize = BitmapUtil.calculateInSampleSize(options, imageSize, imageSize);
				Bitmap bitmap = BitmapFactory.decodeFile(images[i].getPath(), options);
				bitmap = BitmapUtil.createThumbnail(bitmap, imageSize);
				
				ImageView view = new ImageView(gallery.getContext());
				if(bitmap != null){
					view.setImageBitmap(bitmap);
				}

				//设置缩略图间隔
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.leftMargin = 10;
				lp.rightMargin = 10;
				view.setLayoutParams(lp);
				
				view.setTag(images[i].getPath());
				view.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View view) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						Uri uri = Uri.parse("file:" + view.getTag());
						intent.setDataAndType(uri, "image/*");
						startActivity(intent);
					}
					
				});
				
				gallery.addView(view);
			}
		}
		
		textButton.setOnClickListener(new TextListener());

		ActivityManager.add(this);  
	}
	
	class TextListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(GalleryActivity.this, TextActivity.class);
			intent.putExtra("hotspot_id", hotspot_id);
			startActivity(intent);
		}	
	}
}
