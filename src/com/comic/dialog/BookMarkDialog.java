package com.comic.dialog;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.comic.R;
import com.comic.activity.core.MainActivity;
import com.comic.activity.ext.BookMarkActivity;
import com.comic.utils.BookMark;
import com.comic.utils.Constants;
import com.comic.utils.Util;


public class BookMarkDialog extends DefaultDialog {
	private BookMarkActivity bookMark_act;
	private BookMark bookmark;

	public BookMarkDialog(Activity activity, BookMark bookmark,String items[]) {
		super(activity, items, false);
		this.bookmark = bookmark;
		this.bookMark_act =(BookMarkActivity)activity;
	}

	
	protected void doPositive() {
		// TODO Auto-generated method stub
	}
	public void removeBookMarkByNameAndTime(String bookMarkName,String markTime){
		String sdCardPath = Util.getSDcaradPath();
		if(sdCardPath!=null){
			String root_temp_path = sdCardPath+Constants.TEMPPATH;
			String bookmark="";
			String[]array = Util.getFileRead(Constants.BOOKMARKS).split(";");
			if(array!=null && array.length>0){
				for(int i=0;i<array.length;i++){
					if(array[i].indexOf(bookMarkName+"|")!=-1&&array[i].indexOf(markTime+",")!=-1){
						array[i]="";
					}else{
						bookmark+=array[i]+";"+"\r\n";
					}
				}
				if(!"".equals(bookmark)){
					bookmark = bookmark.substring(0,bookmark.length()-1);
				}
					String filePath = root_temp_path+Constants.BOOKMARKS;
					File file = new File(filePath);
					if(file.exists()){
						file.delete();
						file = null;
					}
					Util.saveFile(Constants.BOOKMARKS, bookmark, true);
			}
		}
	}
	public void removeAllBookMarks(){
		String sdCardPath=Util.getSDcaradPath();
		if(sdCardPath!=null){
			String root_temp_path = sdCardPath+Constants.TEMPPATH;
			String filePath = root_temp_path+Constants.BOOKMARKS;
			File file = new File(filePath);
			if(file.exists()){
				file.delete();
				file = null;
			}
	}
	}
	
	protected void doItems(int which) {
		// TODO Auto-generated method stub
		switch(which){
		//查看
		case 0:
			String picPath = bookmark.getImagePath();
			File file = new File(picPath);
			if(file.exists()){
				Intent intent = new Intent(bookMark_act,MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("picPath", picPath);
				intent.putExtras(bundle);
				bookMark_act.startActivity(intent);
				bookMark_act.finish();
			}else{
				Util.showMessage(bookMark_act, "您要查看的书签不存在！");
			}
			break;
		//删除当前标签
		case 1:
			new AlertDialog.Builder(bookMark_act)
			.setTitle(R.string.bookmarkDelete)
			.setPositiveButton(R.string.bookmarkSubmit,new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					String bookMarkName = bookmark.getBookMarkName();
					String markTime = bookmark.getSaveTime();
					//删除某个书签（根据时间和书签名）
					removeBookMarkByNameAndTime(bookMarkName,markTime);
					bookMark_act.setBookMarkAdapter();
				}
			})
			.setNegativeButton(R.string.bookmarkCancel,new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
		//删除所有标签
		case 2:
			new AlertDialog.Builder(bookMark_act)
			.setTitle(R.string.bookmarkDeleteAll)
			.setPositiveButton(R.string.bookmarkSubmit,new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					removeAllBookMarks();
					bookMark_act.setBookMarkAdapter();
				}
			})
			.setNegativeButton(R.string.bookmarkCancel,new DialogInterface.OnClickListener() {
				
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).show();
			break;
			default:
				break;
		}

	}
	

}
