package com.nmlzju.navcamera;

import java.lang.ref.WeakReference;

import com.nmlzju.navcamera.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class BootActivity extends Activity {
	private static final int Version = 1;
	/** Called when the activity is first created. */

	private static class ActivityHandler extends Handler{
		private WeakReference<Activity> parent = null;
		
		public ActivityHandler(Activity activity){
			parent = new WeakReference<Activity>(activity);
		}
		
		public void handleMessage(Message msg){
			Activity activity = parent.get();
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
			Intent intent = null;
			if(settings.getInt("firstRun", 0) != Version){
				 intent = new Intent(activity, GuideActivity.class);
				 Editor editor = settings.edit();
				 editor.putInt("firstRun", Version);
				 editor.commit();
			}else{
				 intent = new Intent(activity, CameraActivity.class);
			}
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