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
		 * Environment�õ� getExternalStorageState()״̬ ��Environment.MEDIA_MOUNTED
		 * SD�����ڿ���*
		 */
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			/* ��ȡ��Ŀ¼ ����Util�µķ���* */
			String rootPath = Util.getSDcaradPath();
			if (rootPath != null) {
				/** ������ʱ�ļ��� **/
				createTempFile(rootPath);
				onLoading();
			} else {
				Util.showMessage(this, getString(R.string.sdcard_rootError));
			}
		} else {
			Util.showMessage(this, getString(R.string.sdcard_nosdcard));
		}

	}

	// ������ʱ�ļ���
	private void createTempFile(String rootPath) {
		// ʵ����һ��File ������ //������ʱ�ļ�
		File file = new File(rootPath + Constants.TEMPPATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = null;
	}

	// ����2��
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
