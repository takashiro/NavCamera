package com.nmlzju.navcamera;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.VideoView;
import com.nmlzju.navcamera.R;

public class VdoActivity extends Activity {
	/** Called when the activity is first created. */

	Button playButton;
	VideoView videoView;
	StringBuilder rtspUrl;
	String videoName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vdo);

		String atext = getIntent().getStringExtra("name");
		String id = atext.substring(0, atext.indexOf("*"));

		videoName = queryVideo(id).get(0);
		rtspUrl = new StringBuilder().append("http")
				.append(HttpUtil.STREAM_URL).append(videoName);

		playButton = (Button) this.findViewById(R.id.startplay);
		playButton.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				PlayRtspStream(rtspUrl.toString());
			}
		});

		videoView = (VideoView) this.findViewById(R.id.player);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		videoView.pause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		videoView.resume();
		super.onResume();
	}

	// play rtsp stream
	private void PlayRtspStream(String rtspUrl) {
		videoView.setVideoURI((Uri.parse(rtspUrl.toString())));
		videoView.requestFocus();
		videoView.start();
	}

	private List<String> queryVideo(String id) {
		// 查询参数
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpVideo?" + queryString;
		// 查询返回结果
		String[] rout = HttpUtil.queryStringForGet(url).split("&");
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rout.length; i++)
			result.add(rout[i]);
		return result;
	}
}