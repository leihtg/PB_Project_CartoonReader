package com.comic.activity.sys;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.comic.R;
import com.comic.activity.core.BaseActivity;
import com.comic.activity.core.MainActivity;
import com.comic.utils.Constants;
import com.comic.utils.Util;

public class LoadingActivity extends BaseActivity {
	private static final String TAG = "LoadingActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading);
		allActivity.add(this);
		/*
		 * Environment得到 getExternalStorageState()状态 与Environment.MEDIA_MOUNTED
		 * SD卡存在可用*
		 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			/* 获取根目录 调用Util下的方法* */
			String rootPath = Util.getSDcaradPath();
			if (rootPath != null) {
				/** 创建临时文件夹 **/
				createTempFile(rootPath);
				onLoading();
			} else {
				Util.showMessage(this, getString(R.string.sdcard_rootError));
			}
		} else {
			Util.showMessage(this, getString(R.string.sdcard_nosdcard));
		}

	}

	// 创建临时文件夹
	private void createTempFile(String rootPath) {
		// 实例化一个File 传参数 //创建临时文件
		File file = new File(rootPath + Constants.TEMPPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
	}

	// 休眠2秒
	private void onLoading() {
		new Thread() {
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					Log.i(TAG, e.getMessage());
				} finally {
					startActivity(new Intent(LoadingActivity.this,
							InitActivity.class));
					// getShowHistory();
				}
			}
		}.start();
	}

	private void getShowHistory() {
		try {
			String picPath = Util.getFileRead(Constants.FILENAME);
			Intent intent;
			if (picPath != null && picPath.length() > 0) {
				intent = new Intent(LoadingActivity.this, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("picPath", picPath);
				intent.putExtras(bundle);
			} else {
				intent = new Intent(LoadingActivity.this, MainActivity.class);
			}
			startActivity(intent);
			finish();
		} catch (Exception e) {

		}
	}
}
