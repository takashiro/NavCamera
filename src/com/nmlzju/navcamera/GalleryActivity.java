package com.nmlzju.navcamera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryActivity extends Activity{

	TextView title;
	Bitmap bmImg;
	VideoView viView;

	List<String> imgpath, audpath;
	List<String> nowimgpath;
	long start, end;
	String imgdes, auddes;
	SystemClock sc;
	String imgfn, audfn;
	LinearLayout g;
	Button textButton;
	String hotspot_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		Intent intent = getIntent();

		// 获得Gallery对象
		g = (LinearLayout) findViewById(R.id.gallery);
		textButton = (Button)findViewById(R.id.text);

		hotspot_id = intent.getStringExtra("hotspot_id");

		nowimgpath = new ArrayList<String>();
		
		String galleryPath = HotspotManager.getHotspotGalleryPath(hotspot_id);
		File gallery = new File(galleryPath);
		if(gallery.exists() && gallery.isDirectory()){
			File[] images = gallery.listFiles();
			for(int i = 0; i < images.length; i++){
				String pathName = images[i].getPath();
				nowimgpath.add(pathName);
				ImageView view = new ImageView(g.getContext());
				view.setImageBitmap(BitmapFactory.decodeFile(pathName));
				g.addView(view);
			}
		}
		
		textButton.setOnClickListener(new TextListener());

		ActivityManager.add(this);  
	}

	class GalleryItemListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			//调用系统看图程序查看大图
			Intent intent = new Intent(Intent.ACTION_VIEW);
			Uri mUri = Uri.parse("file:" + nowimgpath.get(position));
			intent.setDataAndType(mUri, "image/*");
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
