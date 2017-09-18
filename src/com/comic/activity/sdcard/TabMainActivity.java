package com.comic.activity.sdcard;

import java.io.File;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.comic.R;
import com.comic.utils.Constants;
import com.comic.utils.Util;

public class TabMainActivity extends TabActivity {
	private String picPath = null;// 要打开图片的路径
	private String historyPath = null;// 历史记录

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost tabHost = this.getTabHost();
		Intent intent1 = new Intent(this, SDcardActivity.class);
		Intent intent2 = new Intent(this, SDcardActivity.class);

		getOpenPath();
		// intent1.putExtra("picPath", picPath);
		// intent2.putExtra("historyPath", historyPath);

		Bundle picBundle = new Bundle();
		Bundle hisBundle = new Bundle();
		picBundle.putString("picPath", picPath);
		hisBundle.putString("historyPath", historyPath);
		hisBundle.putString("picPath", picPath);
		intent1.putExtras(picBundle);
		intent2.putExtras(hisBundle);

		tabHost.addTab(tabHost
				.newTabSpec(getString(R.string.fileOpen))
				.setIndicator(getString(R.string.fileOpen),
						getResources().getDrawable(R.drawable.fileopen))
				.setContent(intent1));
		tabHost.addTab(tabHost
				.newTabSpec(getString(R.string.recordHistory))
				.setIndicator(getString(R.string.recordHistory),
						getResources().getDrawable(R.drawable.history))
				.setContent(intent2));
	}

	/** 获取要打卡的路径 */
	private void getOpenPath() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			picPath = intent.getExtras().getString("picPath");
		}
		historyPath = Util.getFileRead(Constants.FILENAME);
		if (historyPath != null) {
			File history_file = new File(historyPath);
			if (history_file.exists()) {
				historyPath = history_file.getParent();
			}
			history_file = null;
		}
		// picPath=intent.getStringExtra("picPath");
		// historyPath=intent.getStringExtra("picPath");

	}
}
