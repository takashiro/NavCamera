package com.nmlzju.navcamera;

import java.io.File;
import java.util.List;

import com.alibaba.simpleimage.analyze.kdtree.KDTree;
import com.alibaba.simpleimage.analyze.sift.SIFT;
import com.alibaba.simpleimage.analyze.sift.match.Match;
import com.alibaba.simpleimage.analyze.sift.match.MatchKeys;
import com.alibaba.simpleimage.analyze.sift.render.RenderImage;
import com.alibaba.simpleimage.analyze.sift.scale.KDFeaturePoint;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class HotspotManager {
	
	public static final String STORAGE_ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/NavCamera";
	public static final String HOTSPOT_PATH = STORAGE_ROOT_PATH + "/hotspot";
	
	public static String findHotspot(Bitmap bitmap){
		File storage = new File(HOTSPOT_PATH);
		if(!storage.exists()){
			storage.mkdirs();
			return null;
		}

		List<KDFeaturePoint> snapshotFP = getKDFeaturePoint(bitmap);
		KDTree snapshotKDTree = KDTree.createKDTree(snapshotFP);
		if(snapshotKDTree == null){
			return null;
		}
		
		String targetHotspot = null;
		int maxMatchedKeyNum = 12;
		File[] hotspotDirs = storage.listFiles();
		for(int i = 0; i < hotspotDirs.length; i++){
			File dir = hotspotDirs[i];
			if(!dir.isDirectory()){
				continue;
			}
			
			Hotspot spot = new Hotspot(dir.getName());
			List<KDFeaturePoint> spotFP = spot.getHotspotKDFeaturePoint();
			if(spotFP == null){
				continue;
			}
			
		    List<Match> ms = MatchKeys.findMatchesBBF(spotFP, snapshotKDTree);
		    
		    Log.i("match", dir.getName() + ":" + ms.size());
		    
		    //TODO 此处可能需要修改，来判断图片是否匹配
		    if(ms.size() > maxMatchedKeyNum){
		    	maxMatchedKeyNum = ms.size();
		    	targetHotspot = dir.getName();
		    }
		}
		
		return targetHotspot;
	}

	public static List<KDFeaturePoint> getKDFeaturePoint(Bitmap bitmap){
		RenderImage img = new RenderImage(bitmap);
		img.scaleWithin(200);
		SIFT sift = new SIFT();
		sift.detectFeatures(img.toPixelFloatArray(null));
		return sift.getGlobalKDFeaturePoints();
	}
}
