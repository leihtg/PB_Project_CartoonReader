package com.comic.Listener;

import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;

public abstract class GestureListener implements OnGestureListener {
	private final float FLING_MIN_DISTANCE=150;//滑动多少 才开始跳转
	private final float FLING_MIN_VELOCITY=150;
	
	
	public boolean onDown(MotionEvent e) {
		
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		//向右滑动
		if(e1.getX()-e2.getX()>FLING_MIN_DISTANCE&&Math.abs(velocityY)>FLING_MIN_VELOCITY){
			doOnFling("right");
		}
		//向左滑动
		if(e2.getX()-e1.getX()>FLING_MIN_DISTANCE&&Math.abs(velocityY)>FLING_MIN_VELOCITY){
			doOnFling("left");
		}
		return true;
	}
/**监听收市后应该执行的操作**/
	public abstract void  doOnFling(String string);

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

}
