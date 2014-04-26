package com.nmlzju.navcamera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.AdapterView.OnItemClickListener;

public class ImgActivity extends Activity implements
		EventHandler.OnDownloadCompleteListener,
		EventHandler.OnDownloadUpdateListener,
		EventHandler.OnDownloadErrorListener {

	TextView title;
	Bitmap bmImg;
	VideoView viView;

	List<String> imgpath, audpath;
	List<String> nowimgpath;
	EventHandler eh;
	long start, end;
	String imgdes, auddes;
	SystemClock sc;
	String imgfn, audfn;
	UGallery g;
	Button textButton;
	String hotspot_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		Intent intent = getIntent();

		// 获得Gallery对象
		g = (UGallery) findViewById(R.id.ImgGallery);
		textButton = (Button)findViewById(R.id.text);

		hotspot_id = intent.getStringExtra("hotspot_id");

		eh = new EventHandler();
		eh.setOnDownloadCompleteListener(this);
		eh.setOnDownloadUpdateListener(this);

		nowimgpath = new ArrayList<String>();
		
		String galleryPath = HotspotManager.getHotspotGalleryPath(hotspot_id);
		File gallery = new File(galleryPath);
		if(gallery.exists() && gallery.isDirectory()){
			File[] images = gallery.listFiles();
			for(int i = 0; i < images.length; i++){
				nowimgpath.add(images[i].getPath());
			}
		}

		// 添加ImageAdapter给Gallery对象 注意哦Gallery类并没有setAdapter这个方法
		// 这个方法是从AbsSpinner类继承的
		g.setAdapter(new ImgAdapter(this, nowimgpath));


		// 设置Gallery的事件监听
		g.setOnItemClickListener(new GalleryItemListener());
		
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
			Intent intent = new Intent(ImgActivity.this, TextActivity.class);
			intent.putExtra("hotspot_id", hotspot_id);
			startActivity(intent);
		}
		
	}

	public void onDownloadComplete(Object result) {

		String requesturl = result.toString();
		if (requesturl.endsWith("jpg") || requesturl.endsWith("bmp")
				|| requesturl.endsWith("png"))
			;
	}

	public void onDownloadUpdate(int percent) {

		// info.setText("下载进度：" + String.valueOf(percent) + "%");
	}

	public void onDownloadError(Exception e) {

		// info.setText("下载出错");
	}
}
