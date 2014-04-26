package com.nmlzju.navcamera;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraActivity extends Activity {
	private CameraView cameraView;
	private SurfaceHolder holder = null;
	private Camera camera = null;
	private Bitmap bitmap = null;
	private int screenWidth;
	private int screenHeight;

	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			CameraSnapshot.save(bitmap);
			Intent intent = new Intent(CameraActivity.this, WaitActivity.class);
			startActivity(intent);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		Log.i("ping", screenWidth + "");
		Log.i("ping", screenHeight + "");

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		cameraView = new CameraView(this);

		setContentView(cameraView);
		ActivityManager.add(this);  
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
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
			.setTitle("退出程序")
			.setMessage("是否退出程序")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int which) {
					ActivityManager.finishProgram();
				}
			})
			.setNegativeButton("取消", null)
		.create();

		alertDialog.show();
	}

	class CameraView extends SurfaceView {

		public CameraView(Context context) {
			super(context);

			holder = this.getHolder();
			holder.addCallback(new SurfaceHolder.Callback() {

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					camera.stopPreview();
					camera.release();
					camera = null;
				}

				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					camera = Camera.open();
					try {
						Camera.Parameters param = camera.getParameters();
						int bestWidth = 1024;
						int bestHeight = 600;
						List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
						
						// 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择

						if (sizeList.size() > 1) {
							Iterator<Camera.Size> itor = sizeList.iterator();
							while (itor.hasNext()) {
								Camera.Size cur = itor.next();

								if (cur.width < bestWidth || cur.height < bestHeight) {
									bestWidth = cur.width;
									bestHeight = cur.height;
								}
							}
							
							param.setPictureSize(bestWidth, bestHeight);
						}
						camera.setParameters(param);

						camera.setPreviewDisplay(holder);
					} catch (IOException e) {
						camera.release();
						camera = null;
					}
					// mCamera.startPreview();
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

					Camera.Parameters param = camera.getParameters();
					int bestWidth = 1024;
					int bestHeight = 600;
					List<Camera.Size> sizeList = param.getSupportedPreviewSizes();
	
					// 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
					if (sizeList.size() > 1) {
						Iterator<Camera.Size> itor = sizeList.iterator();
						while (itor.hasNext()) {
							Camera.Size cur = itor.next();

							if (cur.width < bestWidth || cur.height < bestHeight) {
								bestWidth = cur.width;
								bestHeight = cur.height;
							}
						}
						
						param.setPictureSize(bestWidth, bestHeight);
					}
					camera.setParameters(param);

					camera.startPreview();
				}
			});
		}

	}
}
