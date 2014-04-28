package com.nmlzju.navcamera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;

public final class Hotspot {
	private String id = null;

	public Hotspot(String id){
		this.id = id;
	}
	
	public String getPath(){
		return HotspotManager.HOTSPOT_PATH + "/" + id;
	}
	
	public String getName(){
		String filePath = getTextPath();
		File intro = new File(filePath);

		try{
			BufferedReader br = new BufferedReader(new FileReader(intro));
			String line = br.readLine();
			br.close();
			return line;

		}catch(IOException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String getRawImage(){
		return getPath() + "/hotspot.jpg";
	}
	
	public String getTextPath(){
		return getPath() + "/intro.txt";
	}
	
	public String getGalleryPath(){
		return getPath() + "/images";
	}
	
	public File[] getGalleryFiles(){
		File gallery = new File(getGalleryPath());
		if(gallery.exists() && gallery.isDirectory()){
			return gallery.listFiles();
		}
		
		return null;
	}
	
	public String getVideoPath(){
		return getPath() + "/videos";
	}
	
	private List<KDFeaturePoint> featurePoints = null;
	public List<KDFeaturePoint> getHotspotKDFeaturePoint(){
		if(featurePoints != null){
			return featurePoints;
		}
		
		File siftFile = new File(getPath() + "/img.sift");
		if(!siftFile.exists()){
			File imgFile = new File(getRawImage());
			if(!imgFile.exists()){
				return null;
			}
			
			Bitmap hotspotBitmap = BitmapFactory.decodeFile(imgFile.getPath());
			featurePoints = HotspotManager.getKDFeaturePoint(hotspotBitmap);
			
			try {
				siftFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			try {
				FileOutputStream os = new FileOutputStream(siftFile);
				Iterator<KDFeaturePoint> it = featurePoints.iterator();
				while(it.hasNext()){
					KDFeaturePoint fp = it.next();
					JSONArray ap = new JSONArray();
					ap.put(fp.dim);
					ap.put(Float.floatToIntBits(fp.x));
					ap.put(Float.floatToIntBits(fp.y));
					ap.put(Float.floatToIntBits(fp.scale));
					ap.put(Float.floatToIntBits(fp.orientation));
				
					JSONArray desc = new JSONArray();
					for(int i = 0; i < fp.descriptor.length; i++){
						desc.put(fp.descriptor[i]);
					}
					ap.put(desc);
	
					os.write(ap.toString().getBytes());
					os.write('\n');
				}
				os.close();
			} catch (IOException e) {
				return null;
			}

		}else{
			featurePoints = new ArrayList<KDFeaturePoint>();
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(siftFile));
				String line = null;
				while((line = br.readLine()) != null){
					JSONArray arr = new JSONArray(line);

					KDFeaturePoint fp = new KDFeaturePoint();
					fp.dim = arr.getInt(0);
					fp.x = Float.intBitsToFloat(arr.getInt(1));
					fp.y = Float.intBitsToFloat(arr.getInt(2));
					fp.scale = Float.intBitsToFloat(arr.getInt(3));
					fp.orientation = Float.intBitsToFloat(arr.getInt(4));
					
					JSONArray desc = arr.getJSONArray(5);
					fp.descriptor = new int[desc.length()];
					for(int i = 0; i < desc.length(); i++){
						fp.descriptor[i] = desc.getInt(i);
					}
					
					featurePoints.add(fp);
				}
				
				br.close();
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e){
				e.printStackTrace();
				return null;
			} catch (JSONException e){
				e.printStackTrace();
				return null;
			}
		}
		
		return featurePoints;
	}
}
