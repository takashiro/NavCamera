package com.nmlzju.navcamera;

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

// ��һ��ʵ���첽���أ�����ʾ Android ���첽��Ϣ���?�� Handler �ķ�ʽ��   
public class DownloadManage {

	public DownloadManage(EventHandler mHandler) {
		this.mHandler = mHandler;
	}

	// ʵ���Զ���� Handler
	EventHandler mHandler;
	int id;

	// ��ָ�� url ��ַ�����ļ���ָ��·��
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
						System.arraycopy(buffer, 0, temp, 0, readNum);         //���鿽��
						
						if (readNum > -1) {
							outputStream.write(temp);
							readCount = readCount + readNum;

							int percent = (int) (readCount * 100 / fileSize);
							if (percent > prevPercent) {
								// �������ؽ����Ϣ
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

	// ��ȡָ�� url ��ַ����Ӧ����
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

	// �� Handler ������Ϣ
	private void sendMessage(int what, Object obj) {
		// ������Ҫ�� Handler ���͵���Ϣ
		Message msg = mHandler.obtainMessage(what, obj);
		// ������Ϣ
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
