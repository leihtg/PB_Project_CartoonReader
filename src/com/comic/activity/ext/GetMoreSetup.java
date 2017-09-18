package com.comic.activity.ext;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.comic.R;
import com.comic.activity.core.MainActivity;
import com.comic.dialog.DefaultDialog;
import com.comic.utils.Util;

public class GetMoreSetup extends Dialog {
     private MainActivity activity;
     private ImageView imageView;
     private List<String>imageList;
     private int picIndex;
     private Handler handler;
     private RelativeLayout view;
     private RadioGroup radioGroup;
     private RadioButton leftRotate;
     private RadioButton rightRotate;
     private String flag = "";
     private int autoTime=0;
     
	public GetMoreSetup(MainActivity activity,ImageView imageView,List<String>imageList,Handler handler){
    	  super(activity);
    	  this.activity=activity;
    	  this.imageList=imageList;
    	  this.imageView=imageView;
    	  this.handler=handler;
    	  
      }
	public void showDialog(int index){
		picIndex =index;
		//对话框要显示的项
		String [] setupArray={activity.getString(R.string.orderTimeBrowse),
				activity.getString(R.string.bookmarketBrowse),
				activity.getString(R.string.pictureRotate)};
		
		DefaultDialog more_dialog=new DefaultDialog(activity,setupArray,false) {
			@Override
			protected void doPositive() {
			}
			@Override
			protected void doItems(int which) {
               switch(which){
               case 0:
            	   getFixedTime();
            	   break;
               case 1:
            		Intent intent = new Intent(activity,BookMarkActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("picPath", imageList.get(picIndex));
					intent.putExtras(bundle);
					activity.startActivity(intent);
            	   break;
               case 2:
            	   getPicRotate();//旋转
            	   break;
               }
			}
		};
		more_dialog.setTitle(R.string.manu_more);//标题
		more_dialog.show();
	}
	public void getFixedTime() {
		String []items={activity.getString(R.string.handcontrol),
				activity.getString(R.string.second3),
				activity.getString(R.string.second5)};
		  new AlertDialog.Builder(activity).
		  setTitle(R.string.fixedtimetobrowse).
		  //单选  字符串 默认选中项 点击事件
		  setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				switch (which){
				case 0:
					flag="second0";
					break;
				case 1:
					flag="second3";
					break;
				case 2:
					flag="second5";
					break;
					default:
						break;
				}
				
			}
		}).setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				handler.removeCallbacks(autoShow);
                 if(flag!=null&&flag.equals("second0")){
                	 handler.removeCallbacks(autoShow);
                	// handler.removeMessages(1);
                	 Toast.makeText(activity, "手动", Toast.LENGTH_LONG).show();
                 }
                 if(flag!=null&&flag.equals("second3")){
                	 autoTime=3;
                	 handler.postDelayed(autoShow, autoTime*1000);
                	// Toast.makeText(activity, autoTime+"秒中", Toast.LENGTH_LONG).show();
                 }
                 if(flag!=null&&flag.equals("second5")){
                	 autoTime=5;
                	 handler.postDelayed(autoShow, autoTime*1000);
                	// Toast.makeText(activity, autoTime+"秒中", Toast.LENGTH_LONG).show();
                 }
                 flag=null;
			}
		}).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();//关闭对话框
			}
		}).show();
	}
public Runnable autoShow=new Runnable() {
		public void run() {
			if(imageList!=null&&imageList.size()>0){
				picIndex+=1;
				if(picIndex==imageList.size()-1){
					android.os.Message msq=handler.obtainMessage();
					msq.what=0;
					handler.sendMessage(msq);
				}
				
				if(picIndex==imageList.size()-1){
					android.os.Message msg=handler.obtainMessage();
					msg.what=0;
					handler.sendMessage(msg);
				}
				
				if(picIndex<=imageList.size()-1){
					BitmapFactory.Options opts=new BitmapFactory.Options();
					opts.inSampleSize=2;
					Bitmap bitmap = BitmapFactory.decodeFile(imageList.get(picIndex),opts);
					activity.setImageView(bitmap);
					Util.showMessage(activity,(picIndex+1)+"/"+imageList.size());
					handler.postDelayed(autoShow, autoTime*1000);
					
					android.os.Message msg=handler.obtainMessage();
					msg.what=picIndex;
					handler.sendMessage(msg);
				}
			}
		}
	};
	
	/**自定义旋转，向左或者向右旋转45度**/
	private void getPicRotate() {
		//获取自定义界面布局文件
         view=(RelativeLayout) getLayoutInflater().inflate(R.layout.layout_image_rotate, null);
         radioGroup =(RadioGroup) view.findViewById(R.id.radioGroup);
         leftRotate =(RadioButton) view.findViewById(R.id.left);
         rightRotate =(RadioButton) view.findViewById(R.id.right);
         //实现RadioGroup的监听
         radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// 向左旋转45度
				if(leftRotate.getId()==checkedId){
					flag ="left";
				}
				//向右旋转
				if(rightRotate.getId()==checkedId){
					flag="right";
				}
				
			}
		});
         DefaultDialog rotate_dialog=new DefaultDialog(activity,null,true) {
			
			@Override
			protected void doPositive() {
				Bitmap bitmap=null;
				bitmap=Util.imageRotate(flag, imageList.get(picIndex));
				if(bitmap!=null){
					imageView.setImageBitmap(bitmap);
				}
				flag=null;
			}
			
			@Override
			protected void doItems(int which) {
			}
		};
		rotate_dialog.setTitle(R.string.picRotate);
		rotate_dialog.setView(view);//加载自定义界面对话框
		rotate_dialog.show();
         
	}

	
	
	
	
	
	
	
	
}
