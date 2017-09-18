package com.comic.adapter;

import java.util.List;

import com.comic.R;
import com.comic.utils.BookMark;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Activity activity;
	private List<BookMark> markList;
	public ImageAdapter(List<BookMark>list,Activity activity){
		this.activity = activity;
		this.markList =list;
	}
    //�����б���    returnҪ��
	public int getCount() {
		// TODO Auto-generated method stub
		return markList.size();
	}
   //����б��е�ĳһ��
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    //ĳһ��ID
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
    //position �б��еĵڼ��У�View �б�ÿһ��Ĳ��֣�������
	public View getView(int position, View convertView, ViewGroup parent) {
		  if(convertView==null){
	        	 convertView = activity.getLayoutInflater().inflate(R.layout.layout_bookmark, null);
	        	 ImageView thunmail = (ImageView) convertView.findViewById(R.id.thumbnail);
	        	 RelativeLayout view = (RelativeLayout) convertView.findViewById(R.id.layout_markInfo);
	        	 TextView bookmarkName = (TextView) convertView.findViewById(R.id.bookMarkName);
	        	 TextView saveTime = (TextView) convertView.findViewById(R.id.saveTime);
	        	 TextView bookPosition = (TextView) convertView.findViewById(R.id.positon);
	        	 BookMark bookmark = markList.get(position);
	        	 BitmapFactory.Options options = new BitmapFactory.Options();
	        	 options.inSampleSize=16;
	        	 Bitmap bitmap = null;
	        	 bitmap = BitmapFactory.decodeFile(bookmark.getImagePath(),options);
	        	 thunmail.setImageBitmap(bitmap);
	        	 bookmarkName.setText(bookmark.getBookMarkName());
	        	 saveTime.setText(bookmark.getSaveTime());
	        	 bookPosition.setText(activity.getString(R.string.bookmarkstage)+bookmark.getPosition()+activity.getString(R.string.nobookmarkpage));
	        	 bitmap=null;
		
	}
		  return convertView;
}
}
