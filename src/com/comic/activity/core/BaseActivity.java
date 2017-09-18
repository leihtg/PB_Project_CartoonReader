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
		for (Activity ac : allActivity) { // 遍历名 ac 集合名称allActivity
			if (ac.getClass().getName().indexOf(name) > 0) { // getclass得到字节码文件
																// getName
																// .indexOf(name)获得类的名称
																// 如果得到名称 返回ac
				return ac;
			} else {
				return null; // 没有找到为null
			}

		}
		return null;
	}

	/** 监听用户操作 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			promptExit(this);

		}

		return super.onKeyDown(keyCode, event);
	}

	/** 退出程序 */
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