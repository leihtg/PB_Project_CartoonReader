package com.comic.dialog;

import com.comic.R;

import android.app.Activity;
import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
/**自定义Dialog*/
public abstract class DefaultDialog extends Builder{

	 private String[]items;
	/**创建构造方法  传进3个参数 **/   //dialog显示到哪个activity中   //items显示的内用        //flag 判断是否有确定按钮
	public DefaultDialog(Activity activity ,String []items,boolean flag) {
		super(activity);
		this.items=items;
		this.item(flag);  //调用item方法
		
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
					dialog.dismiss(); //取消
					
				}
			});
			this.setItems(items, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
				   doItems(which);
					
				}
			});
		}
		
	
	protected abstract void doPositive();  //DefalutDialog作为父类  任何子类在实现它的时候  都必须实现这个方法    
	protected abstract void doItems(int which);// protected 子类可以访问到
}
