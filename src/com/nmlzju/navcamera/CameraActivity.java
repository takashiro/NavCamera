package com.nmlzju.navcamera;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraActivity extends Activity {
	private SurfaceHolder holder = null;
	private Camera camera = null;
	
	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			CameraSnapshot.save(BitmapFactory.decodeByteArray(data, 0, data.length));
			Intent intent = new Intent(CameraActivity.this, WaitActivity.class);
			startActivity(intent);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(new CameraView(this));
		ActivityManager.add(this);  
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (camera != null) {
				camera.takePicture(null, null, pictureCallback);
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showDialog();
		}

		return super.onKeyDown(keyCode, event);
	}
	
	//确定是否退出
	private void showDialog() {
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

	class CameraView extends SurfaceView {
		Camera.Size size = null;

		public CameraView(Context context) {
			super(context);

			holder = this.getHolder();
			holder.addCallback(new SurfaceHolder.Callback() {

				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					camera = Camera.open();
					if(camera == null){
						return;
					}
					
					//选择最大照片分辨率
					Camera.Parameters param = camera.getParameters();
					size = param.getPictureSize();
					List<Camera.Size> sizeList = param.getSupportedPreviewSizes();

					if (sizeList.size() > 1) {
						for(Camera.Size cur : sizeList){
							if (cur.width > size.width || cur.height > size.height) {
								size = cur;
							}
						}
						
						param.setPictureSize(size.width, size.height);
						camera.setParameters(param);
					}

					try {
						camera.setPreviewDisplay(holder);
					} catch (IOException e) {
						camera.release();
						camera = null;
					}
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
					camera.startPreview();
				}
				
				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					camera.stopPreview();
					camera.release();
					camera = null;
				}
			});
		}
	}
}
