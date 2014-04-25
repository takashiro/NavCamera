package com.nmlzju.navcamera;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImgAdapter extends BaseAdapter {

	// 定义Context
	private Context mContext;
	private List<String> lis;
	private List<String> allis;
	private ImgActivity at;

	// 定义整型数组 即图片源
	// private Integer[] mImageIds = { R.drawable.img1, R.drawable.img2,
	// R.drawable.img3,R.drawable.img4,R.drawable.img5,R.drawable.img6 };

	// 声明ImageAdapter
	public ImgAdapter(Context c, List<String> lis, List<String> allis,
			ImgActivity at) {
		mContext = c;
		this.lis = lis;
		this.allis = allis;
		this.at = at;
	}

	// 获取图片的个数
	public int getCount() {
		return allis.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return position;
	}

	// 获取图片ID
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageview = new ImageView(mContext);

		Log.i("lis begin" + position, "lis" + lis.toString());
		if ((position == lis.size() - 2) && (allis.size() > lis.size())) {
			Log.i("lis beginif" + position, "lis" + lis.toString());

			EventHandler eh = new EventHandler();
			eh.setOnDownloadCompleteListener(at);
			DownloadManager picmanager = new DownloadManager(eh);
			String qimg = allis.get(lis.size());
			String imgfn = qimg.substring(qimg.lastIndexOf("/") + 1,
					qimg.length());
			String imgdes = TextActivity.TMPFILE + imgfn;
			lis.add(imgdes);
			picmanager.download(HttpUtil.BASE_URL + qimg, imgdes);
			Log.i("lis endif" + position, "lis" + lis.toString());
		}
		Log.i("lis bmp" + position, "lis" + lis.toString());
		if (new File(lis.get(position).toString()).exists()) {
			Bitmap bm = BitmapFactory.decodeFile(lis.get(position).toString());
			// 给ImageView设置资源
			imageview.setImageBitmap(bm);
		} else;
			//imageview.setImageResource(R.drawable.wait);
		// 设置布局 图片300*300
		//imageview.setLayoutParams(new Gallery.LayoutParams(1280, 753));
		// 设置显示比例类型
		//imageview.setScaleType(ImageView.ScaleType.FIT_CENTER);
		return imageview;

	}
}
