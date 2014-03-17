package com.ygy;

import java.io.File;

import android.app.Activity; 
import android.content.Context; 
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
 
public class WaitActivity extends Activity {
 
    private String path;
    // 背景图
    private Bitmap[] BackgroundImage = new Bitmap[4];

    
	// 定义Handler对象

	private Handler handler = new Handler() {

		@Override
		// 当有消息发送出来的时候就执行Handler的这个方法
		public void handleMessage(Message msg) {
			//super.handleMessage(msg);
			String tmpname = msg.obj.toString();
			Intent intent = new Intent(WaitActivity.this,
					JumpActivity.class);
			intent.putExtra("name", tmpname);
			startActivity(intent);
//			BackgroundImage = null;
			finish();
//	    	System.gc();
		}
	};

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(new MySurfaceView(this));
       path = getIntent().getStringExtra("path");
       ActivityStackControlUtil.add(this);  
    }
 
    // 自定义的SurfaceView子类
    class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
 

       
 
       SurfaceHolder Holder;
       
       Thread my = new Thread(new MyThread());
 
       public MySurfaceView(Context context) {
           super(context);
           BackgroundImage[0] = BitmapFactory.decodeResource(getResources(),
                  R.drawable.wait0);
           BackgroundImage[1] = BitmapFactory.decodeResource(getResources(),
                   R.drawable.wait1);
           BackgroundImage[2] = BitmapFactory.decodeResource(getResources(),
                   R.drawable.wait2);
           BackgroundImage[3] = BitmapFactory.decodeResource(getResources(),
                   R.drawable.wait3);
//           BackgroundImage[4] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait4);
//           BackgroundImage[5] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait5);
//           BackgroundImage[6] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait6);
//           BackgroundImage[7] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait7);
//           BackgroundImage[8] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait8);
//           BackgroundImage[9] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait9);
//           BackgroundImage[10] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait10);
//           BackgroundImage[11] = BitmapFactory.decodeResource(getResources(),
//                   R.drawable.wait11);
 
           Holder = this.getHolder();// 获取holder
           Holder.addCallback(this);
       }
 
       @Override
       public void surfaceChanged(SurfaceHolder holder, int format, int width,
              int height) {
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
    	   //System.exit(0);

       }
       // 自定义线程类
       class MyThread implements Runnable {
           @Override
           public void run() {
        	   //以下注释为绘制转动效果
//              Canvas canvas = null;
//              int rotate = 0;// 旋转角度变量
//              int i = 0;
//              while (true) {
//                  try {
//                     canvas = Holder.lockCanvas();// 获取画布
//                     Paint mPaint = new Paint();
//                     // 绘制背景
//                     canvas.drawBitmap(BackgroundImage, null, new Rect(0, 0, 1280, 753), mPaint);
//                     // 创建矩阵以控制图片旋转和平移
//                     Matrix m = new Matrix();
//                     // 设置旋转角度和圆心
//                     m.postRotate((rotate += 48) % 360, 
//                            QuestionImage.getWidth() / 2,
//                            QuestionImage.getHeight() / 2);
//                     // 设置左边距和上边距
//                     m.postTranslate(536, 255);
//                     // 绘制问号图
//                     canvas.drawBitmap(QuestionImage, m, mPaint);
//                     // 休眠以控制最大帧频为每秒约30帧
//                     Thread.sleep(33);
//                     Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
//                  } catch (Exception e) {
//                  } finally {
//                     //Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
//                  }
//             }
        	   Canvas canvas = null;
        	   int i = 0;
        	   while (true) {
                 try {
                    canvas = Holder.lockCanvas();// 获取画布
                    Paint mPaint = new Paint();
                    // 绘制背景
                    	canvas.drawBitmap(BackgroundImage[(i++) % 4], null, new Rect(0, 0, 1280, 753), mPaint);
                    // 休眠以控制最大帧频为每秒约30帧
                    Thread.sleep(33);
                    Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                 } catch (Exception e) {
                 } finally {
                    //Holder.unlockCanvasAndPost(canvas);// 解锁画布，提交画好的图像
                 }
            }
          } 
       } 
       
       class BackThread implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String tmpname = FileUpload.uploadFile(path, "upload", "0");
//			File fl = new File(path);
//			if(fl.exists())
//				fl.delete();
	   		// 构造需要向 Handler 发送的消息
	   		Message msg = handler.obtainMessage(QUERY_OK, tmpname);
	   		// 发送消息
	   		handler.sendMessage(msg);
	   		
		}
    	   
       }
    }
    
	private static final int QUERY_OK = 0;
}
