package com.nmlzju.navcamera;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;


/**  
 * Description: activityջ�����࣬ÿ���²���һ��activityʱ�ͼ��룬finish��һ��activityʱ��remove�����������Ҫ
 * ��ȫ�˳�����ʱ��ֻҪ����finishProgram�����Ϳ��Խ�������ȫ����
 */
public class ActivityStackControlUtil {

	private static List<Activity> activityList = new ArrayList<Activity>();
	
	public static void remove(Activity activity){
		
		activityList.remove(activity);
		
	}
	
	public static void add(Activity activity){
		
		activityList.add(activity);
		
	}
	
	public static void finishProgram() {

		for (Activity activity : activityList) {
			
			activity.finish();
			
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());

	}
}




