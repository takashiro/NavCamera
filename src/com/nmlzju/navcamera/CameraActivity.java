package com.nmlzju.navcamera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;

public class CameraActivity extends Activity {
	CameraView cameraView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		setContentView(R.layout.camera);
		cameraView = (CameraView) findViewById(R.id.camera_view);
		
		ActivityManager.add(this);
	}

	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			CameraSnapshot.save(data);
			Intent intent = new Intent(CameraActivity.this, WaitActivity.class);
			startActivity(intent);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (cameraView != null) {
				cameraView.takePicture(pictureCallback);
			}
		}

		return super.onKeyDown(keyCode, event);
	}
	
	//确定是否退出
	@Override
	public void onBackPressed() {
		AlertDialog alertDialog = new AlertDialog.Builder(CameraActivity.this)
			.setTitle(getString(R.string.exit_program))
			.setMessage(getString(R.string.confirm_to_exit_program))
			.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					ActivityManager.finishProgram();
				}
			})
			.setNegativeButton(getString(R.string.cancel), null)
		.create();

		alertDialog.show();
	}
	
	@Override public boolean onTouchEvent(MotionEvent e){
		if(e.getAction() == MotionEvent.ACTION_UP){
			if(cameraView != null){
				cameraView.takePicture(pictureCallback);
			}
		}
		
		return super.onTouchEvent(e);
	}
}
