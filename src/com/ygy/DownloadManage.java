package com.ygy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.protocol.HTTP;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

// 以一个实例，即异步下载，来演示 Android 的异步消息处理（用 Handler 的方式）   
public class DownloadManage {

	public DownloadManage(EventHandler mHandler) {
		this.mHandler = mHandler;
	}

	// 实例化自定义的 Handler
	EventHandler mHandler;
	int id;

	// 按指定 url 地址下载文件到指定路径
	public void download(final String url, final String savePath) {
		new Thread(new Runnable() {
			public void run() {
				try {
					sendMessage(FILE_DOWNLOAD_CONNECT);
					URL sourceUrl = new URL(url);
					URLConnection conn = sourceUrl.openConnection();
					InputStream inputStream = conn.getInputStream();

					int fileSize = conn.getContentLength();

					File savefile = new File(savePath);
					if (savefile.exists()) {
						//savefile.delete();
						return;
					}
					savefile.createNewFile();

					FileOutputStream outputStream = new FileOutputStream(
							savePath, true);

					byte[] buffer = new byte[1024];
					int readCount = 0;
					int readNum = 0;
					int prevPercent = 0;
					while (readCount < fileSize && readNum != -1) {
						readNum = inputStream.read(buffer);
						byte[] temp = new byte[readNum];
						System.arraycopy(buffer, 0, temp, 0, readNum);         //数组拷贝
						
						if (readNum > -1) {
							outputStream.write(temp);
							readCount = readCount + readNum;

							int percent = (int) (readCount * 100 / fileSize);
							if (percent > prevPercent) {
								// 发送下载进度信息
								sendMessage(FILE_DOWNLOAD_UPDATE, percent, readCount
										);

								prevPercent = percent;
							}
						}
					}

					outputStream.close();
					sendMessage(FILE_DOWNLOAD_COMPLETE, savePath);

				} catch (Exception e) {
					sendMessage(FILE_DOWNLOAD_ERROR, e);
					Log.e("MyError", e.toString());
				}
			}
		}).start();
	}

	// 读取指定 url 地址的响应内容
	public void download(final String url) {
		new Thread(new Runnable() {
			public void run() {
				try {
					sendMessage(FILE_DOWNLOAD_CONNECT);
					URL sourceUrl = new URL(url);
					URLConnection conn = sourceUrl.openConnection();
					conn.setConnectTimeout(3000);
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream(),
									HTTP.UTF_8));

					String line = null;
					StringBuffer content = new StringBuffer();
					while ((line = reader.readLine()) != null) {
						content.append(line);
					}

					reader.close();

					sendMessage(FILE_DOWNLOAD_COMPLETE, content.toString());

				} catch (Exception e) {
					sendMessage(FILE_DOWNLOAD_ERROR, e);
					Log.e("MyError", e.toString());
				}
			}
		}).start();
	}

	// 向 Handler 发送消息
	private void sendMessage(int what, Object obj) {
		// 构造需要向 Handler 发送的消息
		Message msg = mHandler.obtainMessage(what, obj);
		// 发送消息
		mHandler.sendMessage(msg);
	}

	private void sendMessage(int what) {
		Message msg = mHandler.obtainMessage(what);
		mHandler.sendMessage(msg);
	}

	private void sendMessage(int what, int arg1, int arg2) {
		Message msg = mHandler.obtainMessage(what, arg1, arg2);
		mHandler.sendMessage(msg);
	}

	private static final int FILE_DOWNLOAD_CONNECT = 0;
	private static final int FILE_DOWNLOAD_UPDATE = 1;
	private static final int FILE_DOWNLOAD_COMPLETE = 2;
	private static final int FILE_DOWNLOAD_ERROR = -1;



	
}
