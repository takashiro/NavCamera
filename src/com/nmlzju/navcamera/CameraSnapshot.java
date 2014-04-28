package com.nmlzju.navcamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class CameraSnapshot {
	private static Bitmap bitmap = null;
	public static final String PATH = HotspotManager.STORAGE_ROOT_PATH + "/snapshot.jpg";
	
	public static void clear(){
		bitmap = null;
	}
	
	public static void save(byte[] data){
		if(data == null){
			bitmap = null;
			return;
		}
		
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		
		// 存文件，用于调试有问题的图片
		File file = new File(PATH);
		if(file.exists()){
			file.delete();
		}
		
		try{
			file.createNewFile();
			FileOutputStream os = new FileOutputStream(file);
			os.write(data);
			os.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static Bitmap getBitmap(){
		return bitmap;
	}
}
