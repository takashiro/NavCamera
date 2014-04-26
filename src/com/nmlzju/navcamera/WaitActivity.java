package com.nmlzju.navcamera;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class WaitActivity extends Activity {

	private Bitmap snapshot = null;

	// 背景图
	private Bitmap[] BackgroundImage = new Bitmap[4];

	// 定义Handler对象
	private static class ResultHandler extends Handler{
		private WeakReference<Activity> parent = null;
		
		public ResultHandler(Activity activity){
			parent = new WeakReference<Activity>(activity);
		}
		
		public void handleMessage(Message msg) {
			Activity main = parent.get();
			String hotspot_id = (String) msg.obj;
			
			if(hotspot_id != null){
				Intent intent = new Intent(main, HotspotActivity.class);
				intent.putExtra("hotspot_id", hotspot_id);
				main.startActivity(intent);
				main.finish();
			}else{
				Toast.makeText(main, "未检测到热点信息！", Toast.LENGTH_LONG).show();
			}
		}
	}
	private Handler handler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MySurfaceView(this));
		snapshot = CameraSnapshot.getBitmap();
		handler = new ResultHandler(this);
		ActivityManager.add(this);
	}

	// 自定义的SurfaceView子类
	class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

		SurfaceHolder Holder;

		Thread my = new Thread(new MyThread());

		public MySurfaceView(Context context) {
			super(context);
			BackgroundImage[0] = BitmapFactory.decodeResource(getResources(), R.drawable.wait0);
			BackgroundImage[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wait1);
			BackgroundImage[2] = BitmapFactory.decodeResource(getResources(), R.drawable.wait2);
			BackgroundImage[3] = BitmapFactory.decodeResource(getResources(), R.drawable.wait3);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mScreenWidth = dm.widthPixels;
			int mScreenHeight = dm.heightPixels;
			
			for(int i=0; i<4; i++)
			{
				BackgroundImage[(i++) % 4] = Bitmap.createScaledBitmap(BackgroundImage[(i++) % 4],mScreenWidth,mScreenHeight,true);
			}
			
			Holder = this.getHolder();// 获取holder
			Holder.addCallback(this);
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			// TODO Auto-generated method stub

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// 启动自定义线程
			new Thread(new BackThread()).start();
			my.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			// System.exit(0);

		}

		// 自定义线程类
		class MyThread implements Runnable {
			@Override
			public void run() {
				Canvas canvas = null;
				int i = 0;
			
				int mWidth = BackgroundImage[0].getWidth();
				int mHeight = BackgroundImage[0].getHeight();
				
				while (true) {
					try {
						canvas = Holder.lockCanvas();// 获取画布
						Paint mPaint = new Paint();
						// 绘制背景
						canvas.drawBitmap(BackgroundImage[(i++) % 4], null, new Rect(0, 0, mWidth, mHeight), mPaint);
						// 休眠以控制最大帧频为每秒约30帧
						
						Thread.sleep(33);
						Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
					} catch (Exception e) {
					}
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
			}
		}
	}
}
