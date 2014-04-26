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

import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.match.Match;
import com.alibaba.simpleimage.analyze.sift.match.MatchKeys;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class HotspotManager {
	
	private static String storageRootPath = Environment.getExternalStorageDirectory().getPath() + "/NavCamera";
	
	public static String findHotspot(Bitmap bitmap){
		String storagePath = storageRootPath + "/hotspot";

		File storage = new File(storagePath);
		if(!storage.exists()){
			storage.mkdirs();
			return null;
		}

		List<KDFeaturePoint> al1 = getKDFeaturePoint(bitmap);
		
		File[] hotspotDirs = storage.listFiles();
		for(int i = 0; i < hotspotDirs.length; i++){
			File dir = hotspotDirs[i];
			if(!dir.isDirectory()){
				continue;
			}
			
			String dirPath = dir.getPath();
			List<KDFeaturePoint> al2 = getHotspotKDFeaturePoint(dirPath);
			if(al2 == null){
				continue;
			}
			
		    List<Match> ms = MatchKeys.findMatchesBBF(al1, al2);  
		    ms = MatchKeys.filterMore(ms);
		    
		    //TODO 此处可能需要修改，来判断图片是否匹配
		    if(ms.size() >= 10){
		    	return dir.getName();
		    }
		}
		
		return null;
	}
	
	public static String getDataStoragePath(){
		return storageRootPath;
	}
	
	public static String getHotspotDirectory(String hotspot_id){
		return storageRootPath + "/hotspot/" + hotspot_id;
	}
	
	public static String getHotspotGalleryPath(String hotspot_id){
		return getHotspotDirectory(hotspot_id) + "/images";
	}

	public static List<KDFeaturePoint> getKDFeaturePoint(Bitmap bitmap){
		RenderImage img = new RenderImage(bitmap);
		img.scaleWithin(100);
		SIFT sift = new SIFT();
		sift.detectFeatures(img.toPixelFloatArray(null));
		return sift.getGlobalKDFeaturePoints();
	}
	
	public static List<KDFeaturePoint> getHotspotKDFeaturePoint(String dirPath){
		List<KDFeaturePoint> al = null;
		
		File siftFile = new File(dirPath + "/img.sift");
		if(!siftFile.exists()){
			File imgFile = new File(dirPath + "/hotspot.jpg");
			if(!imgFile.exists()){
				return null;
			}
			
			Bitmap hotspotBitmap = BitmapFactory.decodeFile(imgFile.getPath());
			al = getKDFeaturePoint(hotspotBitmap);
			
			try {
				siftFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			
			try {
				FileOutputStream os = new FileOutputStream(siftFile);
				Iterator<KDFeaturePoint> it = al.iterator();
				while(it.hasNext()){
					KDFeaturePoint fp = it.next();
					JSONArray ap = new JSONArray();
					ap.put(fp.dim);
					ap.put(fp.x);
					ap.put(fp.y);
					ap.put(fp.scale);
					ap.put(fp.orientation);
				
					JSONArray desc = new JSONArray();
					for(int i = 0; i < fp.descriptor.length; i++){
						desc.put(fp.descriptor[i]);
					}
					ap.put(desc);
	
					os.write(ap.toString().getBytes());
					os.write('\n');
				}
				os.close();
			} catch (JSONException e) {
				return null;
			} catch (IOException e) {
				return null;
			}

		}else{
			al = new ArrayList<KDFeaturePoint>();
			
			try {
				BufferedReader br = new BufferedReader(new FileReader(siftFile));
				String line = null;
				while((line = br.readLine()) != null){
					JSONArray arr = new JSONArray(line);

					KDFeaturePoint fp = new KDFeaturePoint();
					fp.dim = arr.getInt(0);
					fp.x = (float) arr.getDouble(1);
					fp.y = (float) arr.getDouble(2);
					fp.scale = (float) arr.getDouble(3);
					fp.orientation = (float) arr.getDouble(4);
					
					JSONArray desc = arr.getJSONArray(5);
					fp.descriptor = new int[desc.length()];
					for(int i = 0; i < desc.length(); i++){
						fp.descriptor[i] = desc.getInt(i);
					}
					
					al.add(fp);
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
		
		return al;
	}
}
