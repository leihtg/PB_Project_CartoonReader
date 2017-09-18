package com.comic.activity.core;

import java.util.ArrayList;

import com.comic.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public class BaseActivity extends Activity {
	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		allActivity.add(this);
	}

	public static Activity getActivityByName(String name) {
		for (Activity ac : allActivity) { // ������ ac ��������allActivity
			if (ac.getClass().getName().indexOf(name) > 0) { // getclass�õ��ֽ����ļ�
																// getName
																// .indexOf(name)����������
																// ����õ����� ����ac
				return ac;
			} else {
				return null; // û���ҵ�Ϊnull
			}

		}
		return null;
	}

	/** �����û����� **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			promptExit(this);

		}

		return super.onKeyDown(keyCode, event);
	}

	/** �˳����� */
	public void promptExit(Context context) {
		new AlertDialog.Builder(context)
				.setTitle(R.string.layout_title)
				.setMessage(R.string.layout_body)
				.setPositiveButton(R.string.submit,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								for (Activity ac : allActivity) {
									if (ac != null) {
										ac.finish();
									}
								}

							}
						})
				.setNegativeButton(R.string.cancle,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();

	}

}