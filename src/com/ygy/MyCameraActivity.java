package com.ygy;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MyCameraActivity extends Activity {
	/** Called when the activity is first created. */

	private CameraView cv;
	private ProgressDialog pd;
	private SurfaceHolder holder = null;
	private Camera mCamera = null;
	private Bitmap mBitmap = null;
	// private SurfaceView mSurfaceView;
	private int mScreenWidth;
	private int mScreenHeight;
	private TelephonyManager tm;
	private String phoneid;

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

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
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
			// TODO Auto-generated method stub
//			Log.i("ygy", "onPictureTaken");
//			Toast.makeText(getApplicationContext(), "正在保存图片",
//					Toast.LENGTH_LONG).show();
//			 pd= ProgressDialog.show(MyCameraActivity.this, "查询热点信息", "正在查询热点信息……",true);
			mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//			mBitmap = MyCameraActivity.toGrayscale(BitmapFactory
//					.decodeByteArray(data, 0, data.length));
			String path = "/sdcard/DunHuang/Pictures/" + "phoneid" + ".jpg";
			File file = new File(path);
			try {
				if(file.exists())
					file.delete();
				
				file.createNewFile();
				BufferedOutputStream os = new BufferedOutputStream(
						new FileOutputStream(file));
				mBitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);
				os.flush();
				os.close();
				
//				Toast.makeText(getApplicationContext(), "图像保存成功",
//						Toast.LENGTH_LONG).show();

				//String tmpname = FileUpload.uploadFile(path, "upload", "0");
				
				
				
				// Uri uri = Uri.fromFile(new File(path));
				// Intent intent = new Intent();
				// intent.setAction("android.intent.action.VIEW");
				// intent.setDataAndType(uri, "image/jpeg");
				// startActivity(intent);
				
				
				
//				pd.dismiss();
//				Intent intent = new Intent(MyCameraActivity.this,
//						MainUIActivity.class);
//				intent.putExtra("name", tmpname);
//				startActivity(intent);
				Intent intent = new Intent(MyCameraActivity.this,
						WaitActivity.class);
				intent.putExtra("path", path);
				startActivity(intent);
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		phoneid = tm.getDeviceId();
		/*设置Activity全屏，也可在AndroidManifest.xml设置android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
*/
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		Log.i("ping", mScreenWidth + "");
		Log.i("ping", mScreenHeight + "");

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		cv = new CameraView(this);

		/*
		 * fl = new FrameLayout(this); fl.addView(cv);
		 * 
		 * TextView tv = new TextView(this); tv.setText("请拍摄"); fl.addView(tv);
		 */
		setContentView(cv);
		ActivityStackControlUtil.add(this);  
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onKeyDown(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		Log.i("ygy", "onKeyDown");
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (mCamera != null) {
				Log.i("ygy", "mCamera.takePicture");
				// mCamera.autoFocus(null);
				//mCamera.stopPreview();
				mCamera.takePicture(null, null, pictureCallback);
			}
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			showDialog();
		}

		return super.onKeyDown(keyCode, event);
	}
	
	
	//确定是否退出
	 private void showDialog(){
         AlertDialog alertDialog = new AlertDialog.Builder(MyCameraActivity.this)
         .setTitle("退出程序")
         .setMessage("是否退出程序")
         .setPositiveButton("确定", 
                         new DialogInterface.OnClickListener() {
                                     
                                     public void onClick(DialogInterface dialog, int which) {
                                             // TODO Auto-generated method stub
                                    	 //MyCameraActivity.this.finish();
                                    	 //System.exit(0);
                                    	 //android.os.Process.killProcess(android.os.Process.myPid());
                                    	 ActivityStackControlUtil.finishProgram(); 
                                     }
                             })
             .setNegativeButton("取消", 
                             new DialogInterface.OnClickListener() {
                                     
                                     public void onClick(DialogInterface dialog, int which) {
                                             // TODO Auto-generated method stub
                                             return;
                                     }
                             }).create();
         
         alertDialog.show();
 }

	class CameraView extends SurfaceView {
		// class CameraView extends SurfaceView implements
		// SurfaceHolder.Callback{


		public CameraView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub

			Log.i("ygy", "CameraView");
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
						// mCamera.setPreviewDisplay(holder);

						Camera.Parameters param = mCamera.getParameters();
						param.setPictureFormat(PixelFormat.JPEG);
						int bestWidth = 1024;
						int bestHeight = 600;
						List<Camera.Size> sizeList = param
								.getSupportedPreviewSizes();
						// 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
						String ssize = String.valueOf(sizeList.size());
						Log.i("surfaceCreated.............................................",
								ssize);
						if (sizeList.size() > 1) {
							Iterator<Camera.Size> itor = sizeList.iterator();
							while (itor.hasNext()) {
								Camera.Size cur = itor.next();
								String swidth = String.valueOf(cur.width);
								String sheight = String.valueOf(cur.height);
								Log.i("surfaceChanged.............................................",
										swidth);
								Log.i("surfaceChanged.............................................",
										sheight);
								if (cur.width < bestWidth
										|| cur.height < bestHeight) {
									bestWidth = cur.width;
									bestHeight = cur.height;
								}
							}
							// if(bestWidth!=1024){
							// param.setPreviewSize(bestWidth, bestHeight);
							param.setPictureSize(bestWidth, bestHeight);
							// 这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
							// cv.setLayoutParams(new
							// LinearLayout.LayoutParams(bestWidth,
							// bestHeight));
							// fl.setLayoutParams(new LayoutParams(bestWidth,
							// bestHeight));
							// }
							/*
							 * RelativeLayout.LayoutParams lp =
							 * (RelativeLayout.LayoutParams)
							 * cv.getLayoutParams(); lp.width = bestWidth;
							 * lp.height = bestHeight; cv.setLayoutParams(lp);
							 */
							// cv.setLayoutParams(new
							// RelativeLayout.LayoutParams(bestWidth,
							// bestHeight));
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
				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {
					// TODO Auto-generated method stub
					// Camera.Parameters param = mCamera.getParameters();
					// param.setPictureFormat(PixelFormat.JPEG);

					// param.setPreviewSize(width, height);
					// param.setPictureSize(width, height);

					// mCamera.setParameters(param);

					Camera.Parameters param = mCamera.getParameters();
					param.setPictureFormat(PixelFormat.JPEG);
					int bestWidth = 1024;
					int bestHeight = 600;
					List<Camera.Size> sizeList = param
							.getSupportedPreviewSizes();
					String ssize = String.valueOf(sizeList.size());
					Log.i("surfaceChanged.............................................",
							ssize);
					// 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
					if (sizeList.size() > 1) {
						Iterator<Camera.Size> itor = sizeList.iterator();
						while (itor.hasNext()) {
							Camera.Size cur = itor.next();
							String swidth = String.valueOf(cur.width);
							String sheight = String.valueOf(cur.height);
							Log.i("surfaceChanged.............................................",
									swidth);
							Log.i("surfaceChanged.............................................",
									sheight);
							if (cur.width < bestWidth
									|| cur.height < bestHeight) {
								bestWidth = cur.width;
								bestHeight = cur.height;
							}
						}
						// if(bestWidth!=1024){
						// param.setPreviewSize(bestWidth, bestHeight);
						param.setPictureSize(bestWidth, bestHeight);
						// 这里改变了SIze后，我们还要告诉SurfaceView，否则，Surface将不会改变大小，进入Camera的图像将质量很差
						// fl.setLayoutParams(new LayoutParams(bestWidth,
						// bestHeight));
						// cv.setLayoutParams(new LayoutParams(bestWidth,
						// bestHeight));

						// }
						/*
						 * RelativeLayout.LayoutParams lp =
						 * (RelativeLayout.LayoutParams) cv.getLayoutParams();
						 * lp.width = bestWidth; lp.height = bestHeight;
						 * cv.setLayoutParams(lp);
						 */
						// cv.setLayoutParams(new
						// RelativeLayout.LayoutParams(bestWidth, bestHeight));
					}
					mCamera.setParameters(param);

					mCamera.startPreview();
				}
			});
			holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

	}
}
