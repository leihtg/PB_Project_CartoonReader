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
		allActivity.add(this);//���뵽������ȥ
		// ��ȡ����ؼ�
		sdcard = (Button) this.findViewById(R.id.sdcard);
		fileParent = (Button) this.findViewById(R.id.fileParent);
		browse = (Button) this.findViewById(R.id.browse);
		filesList = (ListView) this.findViewById(R.id.filesList);
		// ���������ü�����
		sdcard.setOnClickListener(sdcardButton);
		fileParent.setOnClickListener(parentDirectorButton);
		GobackListener goback=new GobackListener(this);//�Զ���һ������
		browse.setOnClickListener(goback);
		filesList.setOnItemClickListener(listViewItemListener);
		
		Intent intent =this.getIntent();//�ж���ʾ��ʷ���ݻ���
		String picPath=intent.getStringExtra("picPath");
		String historyPath=intent.getStringExtra("historyPath");
		if(picPath!=null){
			refreshListItems(picPath);//��ʾ�ض�·���µ�����
			
		}else if(historyPath!=null){
			refreshListItems(historyPath);//������ʷ��¼
			
		}else{
			refreshListItems(Util.getSDcaradPath());//��ʾSDcard��Ŀ¼�µ�����
			
		}

	}
	
	/**���ݲ�ͬ·����ʾ��·���µ�����**/
       private void refreshListItems(String path) {
    	   parentPath=path;
             if("/mnt/sdcard".equals(path)){
            	 fileParent.setText(R.string.sdcard_is_root);
            	 fileParent.setEnabled(false);//�û����ܵ��
             }else{
            	 fileParent.setText(R.string.sdcard_goback_fatherDir);
            	 fileParent.setEnabled(true);
             } 
             //   �õ�util.class  data����Դ            ���� ��ListView��ӡ���ʾ����(getListData)
             List<Map<String,Object>>data = Util.getListData(path);
             String[]from={"img","name"};  //from��Map��KEY
             int[]to={R.id.img,R.id.name};//˳���������Ӧ
             SimpleAdapter adapter=new SimpleAdapter(this,data,R.layout.layout_sdcard_directory,from,to);
    	   filesList.setAdapter(adapter);  //�Ӻ���ǰд 
    	   
	}
       /**listView�е���¼�**/
    private OnItemClickListener  listViewItemListener=new OnItemClickListener() {

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		HashMap<String,Object>itemMap=(HashMap<String, Object>) parent.getItemAtPosition(position);//getItemAtPosition��ȡlistview��ĳһ��
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
       

	//��SDcard��ť�����¼�
	public OnClickListener sdcardButton = new OnClickListener() {

		public void onClick(View v) {
			refreshListItems(Util.getSDcaradPath());//��ʾSDcard��Ŀ¼�µ�����
		}
	};
	   //���ϼ�Ŀ¼���������¼�
	public OnClickListener parentDirectorButton = new OnClickListener() {

		public void onClick(View v) {
			//parent��Ŀ¼
			File parent = new File(parentPath);
			String path=parent.getParent();
			if(path !=null && !"/mnt".equals(path)){
				refreshListItems(path);
				
			}

		}
	};
	/**����ǰ����������������ʾ**/
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
