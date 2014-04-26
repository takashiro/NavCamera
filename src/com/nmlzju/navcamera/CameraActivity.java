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
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraActivity extends Activity {
	/** Called when the activity is first created. */

	private CameraView cv;
	private SurfaceHolder holder = null;
	private Camera mCamera = null;
	private Bitmap mBitmap = null;
	private int mScreenWidth;
	private int mScreenHeight;

	// private FrameLayout fl;

	/**
	 * 图片去色,返回灰度图片
	 * 
	 * @param bmpOriginal
	 *            传入的图片
	 * @return 去色后的图片
	 */
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	public Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			CameraSnapshot.save(mBitmap);
			Intent intent = new Intent(CameraActivity.this, WaitActivity.class);
			startActivity(intent);
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		Log.i("ping", mScreenWidth + "");
		Log.i("ping", mScreenHeight + "");

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		cv = new CameraView(this);

		setContentView(cv);
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
			if (mCamera != null) {
				mCamera.takePicture(null, null, pictureCallback);
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
					mCamera.stopPreview();
					mCamera.release();
					mCamera = null;
				}

				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					// TODO Auto-generated method stub
					mCamera = Camera.open();
					try {
						Camera.Parameters param = mCamera.getParameters();
						int bestWidth = 1024;
						int bestHeight = 600;
						List<Camera.Size> sizeList = param
								.getSupportedPreviewSizes();
						
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
						mCamera.setParameters(param);

						mCamera.setPreviewDisplay(holder);
					} catch (IOException e) {
						mCamera.release();
						mCamera = null;
					}
					// mCamera.startPreview();
				}

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

					Camera.Parameters param = mCamera.getParameters();
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
					mCamera.setParameters(param);

					mCamera.startPreview();
				}
			});
		}

	}
}
