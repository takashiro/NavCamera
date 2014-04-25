package com.nmlzju.navcamera;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;
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
	String atext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.img);
		Intent intent = getIntent();

		Log.i("begin", "end begin text" + System.currentTimeMillis());

		// 获得Gallery对象
		g = (UGallery) findViewById(R.id.ImgGallery);
		textButton = (Button)findViewById(R.id.text);

		atext = intent.getStringExtra("name");
		String id = atext.substring(0, atext.indexOf("*"));
		String name = atext.substring(atext.indexOf("*") + 1);


		eh = new EventHandler();
		eh.setOnDownloadCompleteListener(this);
		eh.setOnDownloadUpdateListener(this);
		DownloadManage picmanager = new DownloadManage(eh);

		imgpath = queryImage(id);

		nowimgpath = new ArrayList<String>();
		for (int i = 0; i < (imgpath.size() < 3 ? imgpath.size() : 3); i++) {
			String qimg = imgpath.get(i);
			imgfn = qimg.substring(qimg.lastIndexOf("/") + 1, qimg.length());
			imgdes = TextActivity.TMPFILE + imgfn;
			picmanager.download(HttpUtil.BASE_URL + qimg, imgdes);
			nowimgpath.add(imgdes);
		}

		// 添加ImageAdapter给Gallery对象 注意哦Gallery类并没有setAdapter这个方法
		// 这个方法是从AbsSpinner类继承的
		g.setAdapter(new ImgAdapter(this, nowimgpath, imgpath, this));


		// 设置Gallery的事件监听
		g.setOnItemClickListener(new GalleryItemListener());
		
		textButton.setOnClickListener(new TextListener());

		Log.i("end", "end image text" + System.currentTimeMillis());
		ActivityStackControlUtil.add(this);  
	}

	
	private boolean getImageFile(String fName) {
		boolean re;
		String ends = fName.substring(fName.lastIndexOf(".") + 1).toLowerCase();
		if (ends.equals("jpg") || ends.equals("gif") || ends.equals("png")
				|| ends.equals("jpeg") || ends.equals("bmp"))
			re = true;
		else
			re = false;
		return re;
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
			intent.putExtra("name", atext);
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

	private String queryWord(String id) {
		// 查询参数
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpWord?" + queryString;
		// 查询返回结果
		return HttpUtil.queryStringForPost(url);
	}

	private List<String> queryImage(String id) {
		// 查询参数
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpImage?" + queryString;
		// 查询返回结果
		String[] rout = HttpUtil.queryStringForGet(url).split("&");
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rout.length; i++)
			result.add(rout[i]);
		return result;
	}
}
