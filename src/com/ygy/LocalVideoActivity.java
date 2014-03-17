package com.ygy;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

public class LocalVideoActivity extends Activity {
	/** Called when the activity is first created. */

	ImageButton playButton, pauseButton, stopButton;
	VideoView videoView;
	StringBuilder localUrl;
	String videoName;
	MediaController mMediaController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.localvdo);

		String atext = getIntent().getStringExtra("name");
		String id = atext.substring(0, atext.indexOf("*"));

		videoName = queryVideo(id).get(0);
		localUrl = new StringBuilder().append("/sdcard/DunHuang/movies/")
				.append(videoName);
		

		videoView = (VideoView) this.findViewById(R.id.rtsp_player);
		//Create media controller
        mMediaController = new MediaController(this);
        mMediaController.setPadding(568, 0, 72, 50);//控制MediaController位置
        videoView.setMediaController(mMediaController);
     // 设置MediaController与VideView建立关联

        mMediaController.setMediaPlayer(videoView);


        //mMediaController.show(0);

        videoView.setVideoPath(localUrl.toString());
		videoView.requestFocus();
		videoView.start();
        
		ActivityStackControlUtil.add(this);  
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
		if (!videoView.isPlaying()) {
			videoView.setVideoPath(rtspUrl.toString());
			videoView.requestFocus();
			videoView.start();
		}
	}
	
	// play rtsp stream
	private void PauseRtspStream(String rtspUrl) {
		if(videoView.isPlaying())
			{
				videoView.pause();
				//playButton.
			}
	}
	
	// play rtsp stream
	private void StopRtspStream(String rtspUrl) {
		videoView.setVideoPath(rtspUrl.toString());
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
	
	
	/*
	 * 对videoView的MediaController来说，使用setAnchorView并不能控制MediaController位置，
	 * 每次videoview加载影片时MediaController的位置都会设置成默认位置。更简单的方法是使用
	 * MediaController的setPadding方法来设置其位置
	 */
	public class ConstantAnchorMediaController extends MediaController
	{

	    public ConstantAnchorMediaController(Context context, View anchor)
	    {
	        super(context);
	        super.setAnchorView(anchor);
	    }

		@Override
	    public void setAnchorView(View view)
	    {
	        // Do nothing
	    }
	}

}