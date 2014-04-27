package com.nmlzju.navcamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapUtil {
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
	
	public static Bitmap createThumbnail(Bitmap source, int size){
		int width = source.getWidth();
		int height = source.getHeight();
		int x = (width - size) / 2;
		if(x < 0){
			x = 0;
		}else{
			width = size;
		}
		int y = (height - size) / 2;
		if(y < 0){
			y = 0;
		}else{
			height = size;
		}
		
		return Bitmap.createBitmap(source, x, y, width, height);
	}
}
