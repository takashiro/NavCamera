package com.ygy;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

// 自定义的 Handler
public class EventHandler extends Handler {

	private static final int FILE_DOWNLOAD_CONNECT = 0;
	private static final int FILE_DOWNLOAD_UPDATE = 1;
	private static final int FILE_DOWNLOAD_COMPLETE = 2;
	private static final int FILE_DOWNLOAD_ERROR = -1;

	// private DownloadManage mManager;

	public EventHandler() {
		// mManager = manager;
	}

	// 处理接收到的消息
	@Override
	public void handleMessage(Message msg) {

		switch (msg.what) {
		case FILE_DOWNLOAD_CONNECT:
			if (mOnDownloadConnectListener != null)
				mOnDownloadConnectListener.onDownloadConnect();
			break;
		case FILE_DOWNLOAD_UPDATE:
			if (mOnDownloadUpdateListener != null)
				mOnDownloadUpdateListener.onDownloadUpdate(msg.arg2);
			break;
		case FILE_DOWNLOAD_COMPLETE:
			if (mOnDownloadCompleteListener != null)
				mOnDownloadCompleteListener.onDownloadComplete(msg.obj);
			break;
		case FILE_DOWNLOAD_ERROR:
			if (mOnDownloadErrorListener != null)
				mOnDownloadErrorListener.onDownloadError((Exception) msg.obj);
			break;
		default:
			break;
		}
	}

	// 定义连接事件
	private OnDownloadConnectListener mOnDownloadConnectListener;

	public interface OnDownloadConnectListener {
		void onDownloadConnect();
	}

	public void setOnDownloadConnectListener(OnDownloadConnectListener listener) {
		mOnDownloadConnectListener = listener;
	}

	// 定义下载进度更新事件
	private OnDownloadUpdateListener mOnDownloadUpdateListener;

	public interface OnDownloadUpdateListener {
		void onDownloadUpdate(int percent);
	}

	public void setOnDownloadUpdateListener(OnDownloadUpdateListener listener) {
		mOnDownloadUpdateListener = listener;
	}

	// 定义下载完成事件
	private OnDownloadCompleteListener mOnDownloadCompleteListener;

	public interface OnDownloadCompleteListener {
		void onDownloadComplete(Object result);
	}

	public void setOnDownloadCompleteListener(
			OnDownloadCompleteListener listener) {
		mOnDownloadCompleteListener = listener;
	}

	// 定义下载异常事件
	private OnDownloadErrorListener mOnDownloadErrorListener;

	public interface OnDownloadErrorListener {
		void onDownloadError(Exception e);
	}

	public void setOnDownloadErrorListener(OnDownloadErrorListener listener) {
		mOnDownloadErrorListener = listener;
	}
}