package com.nmlzju.navcamera;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

// �Զ���� Handler
public class EventHandler extends Handler {

	private static final int FILE_DOWNLOAD_CONNECT = 0;
	private static final int FILE_DOWNLOAD_UPDATE = 1;
	private static final int FILE_DOWNLOAD_COMPLETE = 2;
	private static final int FILE_DOWNLOAD_ERROR = -1;

	// private DownloadManage mManager;

	public EventHandler() {
		// mManager = manager;
	}

	// ������յ�����Ϣ
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

	// ���������¼�
	private OnDownloadConnectListener mOnDownloadConnectListener;

	public interface OnDownloadConnectListener {
		void onDownloadConnect();
	}

	public void setOnDownloadConnectListener(OnDownloadConnectListener listener) {
		mOnDownloadConnectListener = listener;
	}

	// �������ؽ�ȸ����¼�
	private OnDownloadUpdateListener mOnDownloadUpdateListener;

	public interface OnDownloadUpdateListener {
		void onDownloadUpdate(int percent);
	}

	public void setOnDownloadUpdateListener(OnDownloadUpdateListener listener) {
		mOnDownloadUpdateListener = listener;
	}

	// ������������¼�
	private OnDownloadCompleteListener mOnDownloadCompleteListener;

	public interface OnDownloadCompleteListener {
		void onDownloadComplete(Object result);
	}

	public void setOnDownloadCompleteListener(
			OnDownloadCompleteListener listener) {
		mOnDownloadCompleteListener = listener;
	}

	// ���������쳣�¼�
	private OnDownloadErrorListener mOnDownloadErrorListener;

	public interface OnDownloadErrorListener {
		void onDownloadError(Exception e);
	}

	public void setOnDownloadErrorListener(OnDownloadErrorListener listener) {
		mOnDownloadErrorListener = listener;
	}
}