package com.nmlzju.navcamera;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class WaitActivity extends Activity {

	// 照片
	private Bitmap snapshot;

	// 动画效果线程
	Thread animationThread;
	
	// 动态背景帧ID
	private static final int[] BACKGROUND_IMAGE_ID = new int[]{R.drawable.wait0, R.drawable.wait1, R.drawable.wait2, R.drawable.wait3};

	// 定义Handler对象
	private ResultHandler handler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MySurfaceView(this));
		snapshot = CameraSnapshot.getBitmap();
		handler = new ResultHandler(this);
		ActivityManager.add(this);
		
		new Thread(new BackThread()).start();
	}
	
	private boolean aboutToClose = false;
	public void setAboutToClose(boolean value){
		aboutToClose = value;
	}
	
	private static class ResultHandler extends Handler{
		private WeakReference<WaitActivity> parent = null;
		
		public ResultHandler(WaitActivity activity){
			parent = new WeakReference<WaitActivity>(activity);
		}
		
		public void handleMessage(Message msg) {
			WaitActivity main = parent.get();
			String hotspot_id = (String) msg.obj;
			
			if(hotspot_id != null){
				Intent intent = new Intent(main, HotspotActivity.class);
				intent.putExtra("hotspot_id", hotspot_id);
				main.startActivity(intent);
				main.setAboutToClose(true);
			}else{
				Toast.makeText(main, main.getString(R.string.no_hotspot_is_detected), Toast.LENGTH_LONG).show();
			}
		}
	}
	
	class BackThread implements Runnable {
		@Override
		public void run() {
			String hotspot = HotspotManager.findHotspot(snapshot);

			// 构造需要向 Handler 发送的消息
			Message msg = handler.obtainMessage(0, hotspot);
			handler.sendMessage(msg);
			
			if(hotspot == null){
				handler.postDelayed(new Runnable(){
					public void run(){
						setAboutToClose(true);
					}
				}, 3500);
			}
		}
	}

	// 自定义的SurfaceView子类
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		SurfaceHolder Holder;
		
		// 背景图
		private Bitmap[] backgroundImage = new Bitmap[4];
		
		public MySurfaceView(Context context) {
			super(context);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mScreenWidth = dm.widthPixels;
			int mScreenHeight = dm.heightPixels;
			
			Resources resources = getResources();
			BitmapFactory.Options options = new BitmapFactory.Options();
			for(int i = 0; i < 4; i++) {
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(resources, BACKGROUND_IMAGE_ID[i], options);
				options.inJustDecodeBounds = false;
				options.inSampleSize = calculateInSampleSize(options, mScreenWidth, mScreenHeight);
				backgroundImage[i] = BitmapFactory.decodeResource(resources, BACKGROUND_IMAGE_ID[i], options);
				backgroundImage[i] = Bitmap.createScaledBitmap(backgroundImage[i], mScreenWidth, mScreenHeight, false);
			}
			
			Holder = this.getHolder();// 获取holder
			Holder.addCallback(this);
			
			animationThread = new Thread(new AnimationThread());
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// 启动动画线程
			animationThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// System.exit(0);

		}

		// 自定义线程类
		class AnimationThread implements Runnable {
			@Override
			public void run() {
				Canvas canvas = null;
				byte i = 0;

				while (!aboutToClose) {
					canvas = Holder.lockCanvas();// 获取画布
					Paint mPaint = new Paint();
					// 绘制背景
					canvas.drawBitmap(backgroundImage[i], 0, 0, mPaint);
					i++;
					if(i >= 4){
						i = 0;
					}

					// 休眠以控制最大帧频为每秒约30帧
					try {
						Thread.sleep(33);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
				}
				
				backgroundImage = null;
				finish();
			}
		}
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
}
