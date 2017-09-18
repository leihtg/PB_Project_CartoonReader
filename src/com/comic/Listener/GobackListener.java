package com.comic.Listener;

import com.comic.activity.core.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class GobackListener implements OnClickListener {
private Activity activity;
	public GobackListener(Activity activity){
		this.activity=activity;
	
}
	public void onClick(View v) {
		Intent intent =new Intent(activity,MainActivity.class);
		Bundle bundle=activity.getIntent().getExtras();
		if(bundle!=null&&bundle.size()>0){
			intent.putExtras(bundle);
			
		}
		activity.startActivity(intent);
		activity.finish();
	}

}
