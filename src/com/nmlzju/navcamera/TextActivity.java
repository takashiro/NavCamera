package com.nmlzju.navcamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import com.nmlzju.navcamera.R;

public class TextActivity extends Activity {

	TextView title;
	TextView info;

	List<String> nowimgpath, imgpath;
	String imgfn, imgdes;

	public final static String TMPFILE = "/sdcard/DunHuang/Images/";

	SystemClock sc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.text);
		Intent intent = getIntent();

		long begin = System.currentTimeMillis();
		Log.i("text", "text begin" + begin);

		title = (TextView) findViewById(R.id.Name);
		info = (TextView) findViewById(R.id.Info);
		// soundplay = (Button) findViewById(R.id.soundplay);
		// soundpause = (Button) findViewById(R.id.soundpause);
		// soundstop = (Button) findViewById(R.id.soundstop);

		String atext = intent.getStringExtra("name");
		String id = atext.substring(0, atext.indexOf("*"));
		String name = atext.substring(atext.indexOf("*") + 1);
		title.setText(name);
		info.setText(queryWord(id));
		// audpath = queryAudio(id);
		// String qaudio = audpath.get(0);
		// audfn = qaudio.substring(qaudio.lastIndexOf("/") + 1,
		// qaudio.length());
		//
		// auddes = TextActivity.TMPFILE + audfn;
		// soundcontrol();

		imgpath = queryImage(id);
		nowimgpath = new ArrayList<String>();
		for (int i = 0; i < (imgpath.size() < 3 ? imgpath.size() : 3); i++) {
			String qimg = imgpath.get(i);
			imgfn = qimg.substring(qimg.lastIndexOf("/") + 1, qimg.length());
			imgdes = TextActivity.TMPFILE + imgfn;
			download(HttpUtil.BASE_URL + qimg, imgdes);
			nowimgpath.add(imgdes);
		}

		Log.i("text", "text end capture" + System.currentTimeMillis());
		Log.i("text", "text end" + (System.currentTimeMillis() - begin));
		
		ActivityStackControlUtil.add(this);  
	}

	private String queryWord(String id) {
		// ��ѯ����
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpWord?" + queryString;
		// ��ѯ���ؽ��
		return HttpUtil.queryStringForPost(url);
	}

	private List<String> queryImage(String id) {
		// ��ѯ����
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpImage?" + queryString;
		// ��ѯ���ؽ��
		String[] rout = HttpUtil.queryStringForGet(url).split("&");
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rout.length; i++)
			result.add(rout[i]);
		return result;
	}

	public void download(final String url, final String savePath) {
		new Thread(new Runnable() {
			public void run() {
				try {
					URL sourceUrl = new URL(url);
					URLConnection conn = sourceUrl.openConnection();
					InputStream inputStream = conn.getInputStream();

					int fileSize = conn.getContentLength();

					File savefile = new File(savePath);
					if (savefile.exists()) {
						// savefile.delete();
						return;
					}
					savefile.createNewFile();

					FileOutputStream outputStream = new FileOutputStream(
							savePath, true);

					byte[] buffer = new byte[1024];
					int readCount = 0;
					int readNum = 0;
					int prevPercent = 0;
					while (readCount < fileSize && readNum != -1) {
						readNum = inputStream.read(buffer);
						byte[] temp = new byte[readNum];
						System.arraycopy(buffer, 0, temp, 0, readNum); // ���鿽��

						if (readNum > -1) {
							outputStream.write(temp);
							readCount = readCount + readNum;

							int percent = (int) (readCount * 100 / fileSize);
							if (percent > prevPercent)
								prevPercent = percent;
						}
					}

					outputStream.close();
				} catch (Exception e) {
					Log.e("MyError", e.toString());
				}
			}
		}).start();
	}
}
