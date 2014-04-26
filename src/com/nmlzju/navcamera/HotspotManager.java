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

public class HotspotManager {
	
	private static String storageRootPath = Environment.getExternalStorageDirectory().getPath() + "/NavCamera";
	
	public static String findHotspot(Bitmap bitmap){
		String storagePath = storageRootPath + "/hotspot";

		File storage = new File(storagePath);
		if(!storage.exists()){
			storage.mkdirs();
			return null;
		}

		List<KDFeaturePoint> snapshotFP = getKDFeaturePoint(bitmap);
		KDTree snapshotKDTree = KDTree.createKDTree(snapshotFP);
		
		String targetHotspot = null;
		int maxMatchedKeyNum = 0;
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
		    
		    //TODO 此处可能需要修改，来判断图片是否匹配
		    if(ms.size() >= maxMatchedKeyNum){
		    	maxMatchedKeyNum = ms.size();
		    	targetHotspot = dir.getName();
		    }
		}
		
		return targetHotspot;
	}
	
	public static String getDataStoragePath(){
		return storageRootPath;
	}

	public static List<KDFeaturePoint> getKDFeaturePoint(Bitmap bitmap){
		RenderImage img = new RenderImage(bitmap);
		img.scaleWithin(100);
		SIFT sift = new SIFT();
		sift.detectFeatures(img.toPixelFloatArray(null));
		return sift.getGlobalKDFeaturePoints();
	}
}
