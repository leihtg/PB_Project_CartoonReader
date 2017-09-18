package com.comic.activity.sdcard;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comic.R;
import com.comic.Listener.GobackListener;
import com.comic.activity.core.BaseActivity;
import com.comic.activity.core.MainActivity;
import com.comic.utils.Constants;
import com.comic.utils.Util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SDcardActivity extends BaseActivity {

	private Button sdcard;
	private Button fileParent;
	private Button browse;
	private ListView filesList;
	private String parentPath=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_sdcard_directory_list);
		allActivity.add(this);//加入到集合中去
		// 获取界面控件
		sdcard = (Button) this.findViewById(R.id.sdcard);
		fileParent = (Button) this.findViewById(R.id.fileParent);
		browse = (Button) this.findViewById(R.id.browse);
		filesList = (ListView) this.findViewById(R.id.filesList);
		// 给按键设置监听器
		sdcard.setOnClickListener(sdcardButton);
		fileParent.setOnClickListener(parentDirectorButton);
		GobackListener goback=new GobackListener(this);//自定义一个监听
		browse.setOnClickListener(goback);
		filesList.setOnItemClickListener(listViewItemListener);
		
		Intent intent =this.getIntent();//判断显示历史数据还是
		String picPath=intent.getStringExtra("picPath");
		String historyPath=intent.getStringExtra("historyPath");
		if(picPath!=null){
			refreshListItems(picPath);//显示特定路径下的数据
			
		}else if(historyPath!=null){
			refreshListItems(historyPath);//像是历史记录
			
		}else{
			refreshListItems(Util.getSDcaradPath());//显示SDcard根目录下的数据
			
		}

	}
	
	/**根据不同路径显示该路径下的数据**/
       private void refreshListItems(String path) {
    	   parentPath=path;
             if("/mnt/sdcard".equals(path)){
            	 fileParent.setText(R.string.sdcard_is_root);
            	 fileParent.setEnabled(false);//用户不能点击
             }else{
            	 fileParent.setText(R.string.sdcard_goback_fatherDir);
            	 fileParent.setEnabled(true);
             } 
             //   得到util.class  data数据源            、、 给ListView添加、显示数据(getListData)
             List<Map<String,Object>>data = Util.getListData(path);
             String[]from={"img","name"};  //from是Map的KEY
             int[]to={R.id.img,R.id.name};//顺序与上面对应
             SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.layout_sdcard_directory,from,to);
    	   filesList.setAdapter(adapter);  //从后往前写 
    	   
	}
       /**listView行点击事件**/
    private OnItemClickListener  listViewItemListener=new OnItemClickListener() {

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HashMap<String,Object>itemMap=(HashMap<String, Object>) parent.getItemAtPosition(position);//getItemAtPosition获取listview的某一行
		String path=(String) itemMap.get("path");
		if(path!=null&&!"".equals(path)){
			if(Util.isPic(path)){
				showImage(path);
			}else{
				refreshListItems(path);
				
			}
			
		}
	}

};
       

	//打开SDcard按钮监听事件
	public OnClickListener sdcardButton = new OnClickListener() {

		public void onClick(View v) {
			refreshListItems(Util.getSDcaradPath());//显示SDcard根目录下的数据
		}
	};
	   //打开上级目录按键监听事件
	public OnClickListener parentDirectorButton = new OnClickListener() {

		public void onClick(View v) {
			//parent父目录
			File parent = new File(parentPath);
			String path=parent.getParent();
			if(path !=null && !"/mnt".equals(path)){
				refreshListItems(path);
				
			}

		}
	};
	/**将当前的漫画在主界面显示**/
	private void showImage(String path) {
		Util.saveFile(Constants.FILENAME, path, false);
		Intent intent=new Intent(SDcardActivity.this,MainActivity.class);
		intent.putExtra("picPath", path);
		startActivity(intent);
		finish();
		
		
	}
	
	
      //
//	public OnClickListener fiClickListener = new OnClickListener() {
//
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//
//		}
//	};
}
