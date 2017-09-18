package com.comic.activity.sys;

import com.comic.R;
import com.comic.activity.sdcard.TabMainActivity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class InitActivity extends Activity {

	private ImageButton choose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_init);
		choose=(ImageButton) this.findViewById(R.id.choose);
		choose.setOnClickListener(tabMainSpace);
	}
	/**choose°´Å¥¼àÌýÊÂ¼þ*/
	public OnClickListener tabMainSpace=new OnClickListener() {
		public void onClick(View v) {
          Intent intent=new Intent(InitActivity.this,TabMainActivity.class);		
          startActivity(intent);
          finish();
		}
	};

}
