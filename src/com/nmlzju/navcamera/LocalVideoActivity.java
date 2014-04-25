package com.nmlzju.navcamera;

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
import com.nmlzju.navcamera.R;

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
        mMediaController.setPadding(568, 0, 72, 50);//����MediaControllerλ��
        videoView.setMediaController(mMediaController);
     // ����MediaController��VideView��������

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
		// ��ѯ����
		String queryString = "id=" + id;
		// url
		String url = HttpUtil.BASE_URL + "/OpVideo?" + queryString;
		// ��ѯ���ؽ��
		String[] rout = HttpUtil.queryStringForGet(url).split("&");
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < rout.length; i++)
			result.add(rout[i]);
		return result;
	}
	
	
	/*
	 * ��videoView��MediaController��˵��ʹ��setAnchorView�����ܿ���MediaControllerλ�ã�
	 * ÿ��videoview����ӰƬʱMediaController��λ�ö������ó�Ĭ��λ�á���򵥵ķ�����ʹ��
	 * MediaController��setPadding������������λ��
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