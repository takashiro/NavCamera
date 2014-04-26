package com.nmlzju.navcamera;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;


/**  
 * Description: activity栈管理类，每当新产生一个activity时就加入，finish掉一个activity时就remove，这样到最后需要
 * 完全退出程序时就只要调用finishProgram方法就可以将程序完全结束。
 */
public class ActivityManager {

	private static List<Activity> activityList = new LinkedList<Activity>();
	
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
		
		System.exit(0);
	}
}




