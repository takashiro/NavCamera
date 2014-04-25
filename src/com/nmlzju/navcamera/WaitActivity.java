package com.nmlzju.navcamera;

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
import com.nmlzju.navcamera.R;
 
public class WaitActivity extends Activity {
 
    private String path;
    // ����ͼ
    private Bitmap[] BackgroundImage = new Bitmap[4];

    
	// ����Handler����

	private Handler handler = new Handler() {

		@Override
		// ������Ϣ���ͳ�����ʱ���ִ��Handler���������
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
 
    // �Զ����SurfaceView����
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
 
           Holder = this.getHolder();// ��ȡholder
           Holder.addCallback(this);
       }
 
       @Override
       public void surfaceChanged(SurfaceHolder holder, int format, int width,
              int height) {
           // TODO Auto-generated method stub
 
       }
 
       @Override
       public void surfaceCreated(SurfaceHolder holder) {
           // �����Զ����߳�
    	   new Thread(new BackThread()).start();
           my.start();
       }
 
       @Override
       public void surfaceDestroyed(SurfaceHolder holder) {
          // TODO Auto-generated method stub
    	   //System.exit(0);

       }
       // �Զ����߳���
       class MyThread implements Runnable {
           @Override
           public void run() {
        	   //����ע��Ϊ����ת��Ч��
//              Canvas canvas = null;
//              int rotate = 0;// ��ת�Ƕȱ���
//              int i = 0;
//              while (true) {
//                  try {
//                     canvas = Holder.lockCanvas();// ��ȡ����
//                     Paint mPaint = new Paint();
//                     // ���Ʊ���
//                     canvas.drawBitmap(BackgroundImage, null, new Rect(0, 0, 1280, 753), mPaint);
//                     // ���������Կ���ͼƬ��ת��ƽ��
//                     Matrix m = new Matrix();
//                     // ������ת�ǶȺ�Բ��
//                     m.postRotate((rotate += 48) % 360, 
//                            QuestionImage.getWidth() / 2,
//                            QuestionImage.getHeight() / 2);
//                     // ������߾���ϱ߾�
//                     m.postTranslate(536, 255);
//                     // �����ʺ�ͼ
//                     canvas.drawBitmap(QuestionImage, m, mPaint);
//                     // �����Կ������֡ƵΪÿ��Լ30֡
//                     Thread.sleep(33);
//                     Holder.unlockCanvasAndPost(canvas);// �������ύ���õ�ͼ��
//                  } catch (Exception e) {
//                  } finally {
//                     //Holder.unlockCanvasAndPost(canvas);// �������ύ���õ�ͼ��
//                  }
//             }
        	   Canvas canvas = null;
        	   int i = 0;
        	   while (true) {
                 try {
                    canvas = Holder.lockCanvas();// ��ȡ����
                    Paint mPaint = new Paint();
                    // ���Ʊ���
                    	canvas.drawBitmap(BackgroundImage[(i++) % 4], null, new Rect(0, 0, 1280, 753), mPaint);
                    // �����Կ������֡ƵΪÿ��Լ30֡
                    Thread.sleep(33);
                    Holder.unlockCanvasAndPost(canvas);// �������ύ���õ�ͼ��
                 } catch (Exception e) {
                 } finally {
                    //Holder.unlockCanvasAndPost(canvas);// �������ύ���õ�ͼ��
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
	   		// ������Ҫ�� Handler ���͵���Ϣ
	   		Message msg = handler.obtainMessage(QUERY_OK, tmpname);
	   		// ������Ϣ
	   		handler.sendMessage(msg);
	   		
		}
    	   
       }
    }
    
	private static final int QUERY_OK = 0;
}
