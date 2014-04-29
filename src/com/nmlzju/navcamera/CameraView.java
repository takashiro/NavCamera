package com.nmlzju.navcamera;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class CameraView extends SurfaceView {
	Camera.Size size = null;
	private Camera camera = null;
	
	public CameraView(Context context) {
		super(context);
		init();
	}
	
	public CameraView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public CameraView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		SurfaceHolder holder = this.getHolder();
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
	
	public void takePicture(PictureCallback jpeg){
		camera.takePicture(null, null, jpeg);
	}
}