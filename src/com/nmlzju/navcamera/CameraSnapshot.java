package com.nmlzju.navcamera;

import android.graphics.Bitmap;

public class CameraSnapshot {
	private static Bitmap bitmap = null;
	
	public static void save(Bitmap snapshot){
		bitmap = snapshot;
	}
	
	public static Bitmap getBitmap(){
		return bitmap;
	}
}
