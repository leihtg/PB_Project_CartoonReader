package com.comic.dialog;

import com.comic.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
/**�Զ���Dialog*/
public abstract class DefaultDialog extends Builder{

	 private String[]items;
	/**�������췽��  ����3������ **/   //dialog��ʾ���ĸ�activity��   //items��ʾ������        //flag �ж��Ƿ���ȷ����ť
	public DefaultDialog(Activity activity ,String []items,boolean flag) {
		super(activity);
		this.items=items;
		this.item(flag);  //����item����
		
	}
	private void item(boolean flag) {
		if(flag){
			this.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					doPositive();
					
				}
			});
		}
			this.setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss(); //ȡ��
					
				}
			});
			this.setItems(items, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				   doItems(which);
					
				}
			});
		}
		
	
	protected abstract void doPositive();  //DefalutDialog��Ϊ����  �κ�������ʵ������ʱ��  ������ʵ���������    
	protected abstract void doItems(int which);// protected ������Է��ʵ�
}
