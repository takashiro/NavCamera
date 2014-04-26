package com.nmlzju.navcamera;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

public class TextActivity extends Activity {

	TextView title;
	TextView info;

	List<String> nowimgpath, imgpath;
	String imgfn, imgdes;

	SystemClock sc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.Transparent);
		setContentView(R.layout.text);
		Intent intent = getIntent();

		title = (TextView) findViewById(R.id.Name);
		info = (TextView) findViewById(R.id.Info);

		// TODO not ideal here
		String hotspot_id = intent.getStringExtra("hotspot_id");
		String filePath = new Hotspot(hotspot_id).getPath() + "/intro.txt";
		File intro = new File(filePath);
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(intro));
			String line = br.readLine();
			if(line != null){
				title.setText(line);
				
				StringBuffer body = new StringBuffer();
				while((line = br.readLine()) != null){
					body.append(line);
				}
				
				info.setText(body.toString());
			}
			br.close();

		}catch(IOException e){
			e.printStackTrace();
		}
		
		ActivityManager.add(this);  
	}
}
