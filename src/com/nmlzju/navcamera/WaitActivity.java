package com.nmlzju.navcamera;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

public class WaitActivity extends Activity {
	// 动画效果线程
	Thread animationThread;

	// 定义Handler对象
	private ResultHandler handler = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MySurfaceView(this));
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
			String hotspot = HotspotManager.findHotspot(CameraSnapshot.getBitmap());
			CameraSnapshot.clear();

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

		SurfaceHolder holder;
		
		// 背景图
		private Bitmap background = null;
		
		private Bitmap icon = null;
		
		public MySurfaceView(Context context) {
			super(context);

			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			int mScreenWidth = dm.widthPixels;
			int mScreenHeight = dm.heightPixels;
			
			Resources resources = getResources();
			
			icon = BitmapFactory.decodeResource(resources, R.drawable.wait_run);
			
			background = BitmapFactory.decodeResource(resources, R.drawable.wait_bg);
			if(background.getWidth() != mScreenWidth || background.getHeight() != mScreenHeight){
				background = Bitmap.createScaledBitmap(background, mScreenWidth, mScreenHeight, false);
			}
			
			holder = this.getHolder();// 获取holder
			holder.addCallback(this);
			
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
				Matrix matrix = new Matrix();
				
				// 图标位置
				float background_width = (float) background.getWidth();
				float background_height = (float) background.getHeight();
				float icon_x = (background_width - icon.getWidth()) / 2 + background_width / 640 * 3;
				float icon_y = (background_height - icon.getHeight()) / 2 - background_height / 800 * 9;
				matrix.postTranslate(icon_x, icon_y);
				
				// 图标旋转中心
				float icon_px = icon_x + (float) icon.getWidth() / 2;
				float icon_py = icon_y + (float) icon.getHeight() / 2;
				
				while (!aboutToClose) {
					Canvas canvas = holder.lockCanvas();// 获取画布
					Paint paint = new Paint();
					
					// 绘制背景
					canvas.drawBitmap(background, 0, 0, paint);
					
					// 绘制旋转图标
					matrix.postRotate(45, icon_px, icon_py);
					canvas.drawBitmap(icon, matrix, paint);

					// 休眠以控制最大帧频为每秒约30帧
					try {
						Thread.sleep(33);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
				}
				
				icon = null;
				background = null;
				finish();
			}
		}
	}

}
