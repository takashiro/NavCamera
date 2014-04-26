package com.nmlzju.navcamera;

import java.lang.ref.WeakReference;

import com.nmlzju.navcamera.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class BootActivity extends Activity {
	/** Called when the activity is first created. */

	private static class ActivityHandler extends Handler{
		private WeakReference<Activity> parent = null;
		
		public ActivityHandler(Activity activity){
			parent = new WeakReference<Activity>(activity);
		}
		
		public void handleMessage(Message msg){
			Activity activity = parent.get();
			Intent intent = new Intent(activity, CameraActivity.class);
			activity.startActivity(intent);
			activity.finish();
		}
	}
	private Handler activityHandler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		
		activityHandler = new ActivityHandler(this);

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(500);
					activityHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

}