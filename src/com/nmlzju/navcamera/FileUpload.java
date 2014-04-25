package com.nmlzju.navcamera;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;


public class FileUpload {
	
	//private static String newName = "fcsjt.jpg";
	//private static String uploadFile = "/sdcard/fcsjt.jpg";

	/* 上传文件至Server的方法 */
	public static String uploadFile(String uploadFile,String servelet,String id) {
		String realUrl = new String();
		if(servelet.equals("image"))realUrl = HttpUtil.BASE_URL + "/AddImage";
		else if(servelet.equals("audio"))realUrl = HttpUtil.BASE_URL + "/AddAudio";
		else if(servelet.equals("video"))realUrl = HttpUtil.BASE_URL + "/AddVideo";
		else if(servelet.equals("upload"))realUrl = HttpUtil.BASE_URL + "/Upload";
		else return "0";
		
		String newName = uploadFile.substring(uploadFile.lastIndexOf("/") + 1);
		String end = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		String path = "";
		try {
			URL url = new URL(realUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* 允许Input、Output，不使用Cache */
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* 设置传送的method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"id\"" + end + end + id);
			ds.writeBytes(end);
			
			ds.writeBytes(twoHyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; "
					+ "name=\"file1\";filename=\"" + newName + "\"" + end);
			ds.writeBytes(end);
			
			/* 取得文件的FileInputStream */
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			ds.writeBytes(end);
			ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

			/* close streams */
			fStream.close();
			ds.flush();

			Log.i("rrrr", "rrrr up succeed");
			/* 取得Response内容 */
			InputStream is = con.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8"); 
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = isr.read()) != -1) {
				b.append((char) ch);
			}
			/* 将Response显示于Dialog */
			//showDialog("上传成功" + b.toString().trim());
			/* 关闭DataOutputStream */
			path = b.toString();
			ds.close();
		} catch (Exception e) {
			//showDialog("上传失败" + e);
			Log.i("fileup", "fileup" + e.toString());
		}
		
		return path;
	}

}
