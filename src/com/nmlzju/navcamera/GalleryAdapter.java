package com.nmlzju.navcamera;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GalleryAdapter extends BaseAdapter {

	// 定义Context
	private Context mContext;
	private List<String> lis;

	// 声明ImageAdapter
	public GalleryAdapter(Context c, List<String> lis) {
		mContext = c;
		this.lis = lis;
	}

	// 获取图片的个数
	public int getCount() {
		return lis.size();
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

		/*if ((position == lis.size() - 2) && (allis.size() > lis.size())) {
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
		}*/
		
		if (new File(lis.get(position).toString()).exists()) {
			Bitmap bm = BitmapFactory.decodeFile(lis.get(position).toString());
			imageview.setImageBitmap(bm);
		}
		
		return imageview;

	}
}
