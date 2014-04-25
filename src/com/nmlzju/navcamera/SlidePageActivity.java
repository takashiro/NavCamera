package com.nmlzju.navcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import cn.w.song.widget.scroll.SlidePageView;
import cn.w.song.widget.scroll.SlidePageView.OnPageViewChangedListener;

/**
 * SlidePageView支持各显示单元的自行定制，不仅可以支持等宽的显示单元设计，也支持不规则宽度的显示单元设计。
 * SlidePageView支持移动滑动和甩手两种滑动方式。
 * 了解详情看http://blog.csdn.net/swadair/article/details/7529159
 * 注意确保本项目导入了w.song.android.widget-1.0.3.jar组件包
 * w.song.android.widget-1.0.3.jar下载地址http://download.csdn.net/detail/swadair/4271503
 * @author w.song
 * @version 1.0.1
 * @date 2012-5-2
 */
public class SlidePageActivity extends Activity {
	private String tag = "SlidePageViewDemoActivity";
	private int maxTime = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidepage);//仿微信引导页
//		setContentView(R.layout.slidepageviewdemo_ui_b);//自定义等宽显示单元demo
//		setContentView(R.layout.slidepageviewdemo_ui_c);//自定义不规则宽度显示单元demo
		SlidePageView spv = (SlidePageView) findViewById(R.id.slidepageviewtest_ui_SlidePageView_test);
		//spv.setCurrPagePosition(0);//设置当前页位置
		spv.setOnPageViewChangedListener(new OnPageViewChangedListener() {

			@Override
			public void OnPageViewChanged(int currPagePosition,
					View currPageView) {
				Log.v(tag, "currPagePosition=" + currPagePosition);
				if(maxTime == 1)
				{
					Intent it = new Intent(SlidePageActivity.this, MyCameraActivity.class);
					startActivity(it);
					finish();
				}
				if(currPagePosition == 4)maxTime++;
				
			}
		});
	}
}
