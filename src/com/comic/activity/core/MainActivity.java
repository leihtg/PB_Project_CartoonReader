package com.comic.activity.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comic.R;
import com.comic.Listener.GestureListener;
import com.comic.activity.ext.GetMoreSetup;
import com.comic.activity.sdcard.TabMainActivity;
import com.comic.dialog.DefaultDialog;
import com.comic.utils.Constants;
import com.comic.utils.Util;

public class MainActivity extends BaseActivity {
	private ImageView imageView;
	private ImageButton openSDcard;
	private ImageButton page;
	private ImageButton zoomSmall;
	private ImageButton zoomBig;
	private ImageButton bookmark;
	private ImageButton setup;
	private ImageButton logout;
	private String path = null;
	private List<String> imageList;// 所有同一路径下的图片集合
	private int picIndex = 0;// 图片的位置
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout layout4;
	private TextView imageName;
	private TextView pagePosition;
	private ImageButton lastPage;
	private ImageButton nextPage;
	private GestureDetector gestureDetector;
	private int disWidth;// 宽
	private int disHeight;// 高
	private GetMoreSetup gms; // 更多
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		getDisplayMetrics();

		allActivity.add(this); // 加入到集合中去

		// 获取界面控件
		gestureDetector = new GestureDetector(gestureListener);// 初始手势触摸
		imageView = (ImageView) findViewById(R.id.imageView);
		openSDcard = (ImageButton) findViewById(R.id.openSDcard);
		page = (ImageButton) findViewById(R.id.page);
		zoomSmall = (ImageButton) findViewById(R.id.zoomSmall);
		zoomBig = (ImageButton) findViewById(R.id.zoomBig);
		bookmark = (ImageButton) findViewById(R.id.bookmark);
		logout = (ImageButton) findViewById(R.id.logout);
		setup = (ImageButton) findViewById(R.id.setup);
		layout2 = (RelativeLayout) this.findViewById(R.id.layout2);
		layout3 = (RelativeLayout) this.findViewById(R.id.layout3);
		layout4 = (RelativeLayout) this.findViewById(R.id.layout4);
		imageName = (TextView) this.findViewById(R.id.imageName);
		pagePosition = (TextView) this.findViewById(R.id.pagePosition);
		lastPage = (ImageButton) this.findViewById(R.id.lastPage);
		nextPage = (ImageButton) this.findViewById(R.id.nextPage);

		// 设置点击事件
		openSDcard.setOnClickListener(openSDcardButton);// 打开SD
		page.setOnClickListener(pageButton);// 分页
		zoomSmall.setOnClickListener(zoomButton);// 缩小
		zoomBig.setOnClickListener(zoomButton);// 放大
		bookmark.setOnClickListener(bookmarkButton);// 书签
		logout.setOnClickListener(logoutButton);// 关闭
		setup.setOnClickListener(setupButton);// 更多
		lastPage.setOnClickListener(page_slideButton);
		nextPage.setOnClickListener(page_slideButton);

		imageView.setOnClickListener(hiddenMenu);// 调用接口
		imageView.setLongClickable(true);// 长按事件为可用

		path = this.getIntent().getStringExtra("picPath");
		if (path != null) {
			// Bitmap bm=new
			// BitmapFactory().decodeFile(path);//有path路径构造一个BitMap
			// imageView.setImageBitmap(bm);
			imageList = loadImages();
			if (imageList != null && imageList.size() > 0) {
				setImageView();

			} else {
				// 没有漫画图片时将所有的菜单都显示出来
				layout2.setVisibility(View.VISIBLE);// 设置其是否显示
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.VISIBLE);
				// 提示没有漫画图片
				Util.showMessage(MainActivity.this, getString(R.string.noPic));
			}
		} else {
			Util.showMessage(MainActivity.this, getString(R.string.nopath));

		}
		getDisplayMetrics();
		handler = new Handler() {
			@Override
			public void handleMessage(Message msq) {

				super.handleMessage(msq);
				if (msq.what == 0) {
					handler.removeCallbacks(gms.autoShow);
				} else {
					picIndex = msq.what;
					String position = (picIndex + 1) + "/" + imageList.size();
					pagePosition.setText(position);// 调用setText更新position
					path = imageList.get(picIndex);// 更新picIndex放大缩小
				}
			}

		};
		gms = new GetMoreSetup(MainActivity.this, imageView, imageList, handler);
	}

	// 点击5秒后消失
	private Runnable hideMenu = new Runnable() {

		public void run() {
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);
			layout4.setVisibility(View.GONE);
			handler.removeCallbacks(hideMenu);
		}
	};

	// 接口
	public OnClickListener hiddenMenu = new OnClickListener() {

		public void onClick(View v) {
			if (imageList != null && imageList.size() > 0) {
				layout2.setVisibility(View.VISIBLE);// 设置其是否显示
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.VISIBLE);
				handler.postDelayed(hideMenu, 5000);// 5秒后调用GONE 不可见

			}

		}
	};

	/** 显示图片 **/
	private void setImageView() {
		// 图片处理
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// 图片的高宽为原来的1/2
		path = imageList.get(picIndex);
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		imageView.setImageBitmap(bitmap);
		setPageInfo();

	}

	/** 设置动漫名称及当前看的位置 **/
	private void setPageInfo() {
		File file = new File(path);// /mnt/sdcard/cartoonReader/001.png
		String parentPath = file.getParent();// /mnt/sdcard/cartoonReader
		if (parentPath != null && !"".equals(parentPath)) {
			String picName = parentPath.substring(
					parentPath.lastIndexOf("/") + 1, parentPath.length());
			imageName.setText(picName);

		}
		String position = (picIndex + 1) + "/" + imageList.size();
		pagePosition.setText(position);

	}

	/** 装在同一路径下所有图片 **/
	private List<String> loadImages() {
		ArrayList<String> list = new ArrayList<String>();
		File picFile = new File(path);
		if (!picFile.exists()) {
			return null;

		} else { // 传父路径传给他
			File[] files = Util.getFilesOrder(new File(picFile.getParent()));
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (Util.isPic(file.getPath())) {
						list.add(file.getAbsolutePath());
					}

				}
				for (int i = 0; i < list.size(); i++) {// 判断传递过来的图片是当前兄弟图片集合中的第几张
					String picPath = list.get(i);
					if (path.equalsIgnoreCase(picPath)) {
						picIndex = i;

					}
				}
			}
		}
		return list;
	}

	/** 手势监听 **/
	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		imageView.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);

	};

	/** 手势监听器 **/
	GestureListener gestureListener = new GestureListener() {

		@Override
		public void doOnFling(String action) {
			if (imageList != null && imageList.size() > 0) {
				if ("right".equals(action)) {// 下一页
					if (picIndex < imageList.size() - 1) {
						picIndex = picIndex + 1;// =======picIndex+=1;
						setImageView();

					} else {
						Util.showMessage(MainActivity.this,
								getString(R.string.pageLast));
					}
				}
				if ("left".equals(action)) {// 上一页
					if (picIndex >= 1) {
						picIndex = picIndex - 1;// ===picIndex-=1;
						setImageView();
					} else {
						Util.showMessage(MainActivity.this,
								getString(R.string.pageFirst));
					}
				}

			}
		}
	};

	/** 显示上一页、下一页按钮点击事件 */
	public OnClickListener page_slideButton = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lastPage:
				if (picIndex >= 1) {
					picIndex = picIndex - 1;// ===picIndex-=1;
					setImageView();
				} else {
					Util.showMessage(MainActivity.this,
							getString(R.string.pageFirst));
				}
				break;
			case R.id.nextPage:
				// imageList.size 所有图片
				if (picIndex < imageList.size() - 1) {
					picIndex = picIndex + 1;// =======picIndex+=1;
					setImageView();

				} else {
					Util.showMessage(MainActivity.this,
							getString(R.string.pageLast));
				}
				break;
			}

		}
	};

	/** 打开SDcard按钮点击事件 **/
	public OnClickListener openSDcardButton = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, TabMainActivity.class);
			startActivity(intent);

		}
	};
	/** 分页按钮点击事件 **/
	public OnClickListener pageButton = new OnClickListener() {

		public void onClick(View v) {
			String[] pageArray = { getString(R.string.firstPage),
					getString(R.string.beforePage),
					getString(R.string.nextPage), getString(R.string.lastPage) };
			DefaultDialog pageDialog = new DefaultDialog(MainActivity.this,
					pageArray, false) {
				protected void doItems(int which) {
					switch (which) {
					case 0:// 第一页
						if (picIndex >= 1) {
							picIndex = 0;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageFirst));
						}
						break;
					case 1:// 上一页
						if (picIndex >= 1) {
							picIndex -= 1;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageFirst));
						}
						break;
					case 2:// 下一页
							// imageList.size()-1最后一页
						if (picIndex < imageList.size() - 1) {
							picIndex += 1;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageLast));
						}
						break;
					case 3:// 最后一页
						if (picIndex != imageList.size() - 1) {
							picIndex = imageList.size() - 1;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageLast));
						}
						break;

					}

				};

				protected void doPositive() {
				};
			};

			pageDialog.setTitle(R.string.pageTitle);// 标题

			pageDialog.show();

		}
	};
	/** 缩放按钮点击事件 **/
	public OnClickListener zoomButton = new OnClickListener() {
		public void onClick(View v) {
			if (path != null) {
				String action = "";
				if (v == zoomSmall) {
					action = "small";

				} else if (v == zoomBig) {
					action = "big";
				}
				if (!"".equals(action)) {
					Bitmap bitmap = Util.imageZoom(path, disWidth, disHeight,
							action);
					if (bitmap != null) {
						setImageView(bitmap);
						bitmap = null;

					}

				}
			}
		}
	};
	// /**放大按钮点击事件**/
	// public OnClickListener zoomBigButton=new OnClickListener() {
	//
	//
	// public void onClick(View v) {
	//
	//
	// }
	// };
	/** 书签按钮点击事件 **/
	public OnClickListener bookmarkButton = new OnClickListener() {
		public void onClick(View v) {
			RelativeLayout view = (RelativeLayout) getLayoutInflater().inflate(
					R.layout.layout_bookmark_add, null);
			final EditText bookmarkName = (EditText) view
					.findViewById(R.id.bookMarkName);
			new AlertDialog.Builder(MainActivity.this)
					.setIcon(R.drawable.menu_bookmark)
					.setTitle(R.string.bookmarkTitle)
					.setView(view)
					.setPositiveButton(R.string.submit,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									String bookMarks = bookmarkName.getText()
											.toString();
									if (!"".equals(bookMarks)) {
										SimpleDateFormat df = new SimpleDateFormat(
												"yyyy-MM-dd HH:mm:ss");
										String curTime = df
												.format(new java.util.Date());
										bookMarks = bookMarks + "|" + curTime
												+ "," + path + "#" + picIndex
												+ ";";
										Util.saveFile(Constants.BOOKMARKS,
												bookMarks, true);
										// 写入的格式abc|2013-1-3
										// 18:52:20,mnt/sdcard/abc#2;
										Util.showMessage(
												MainActivity.this,
												getString(R.string.bookmarkSave));
									} else {
										Util.showMessage(
												MainActivity.this,
												getString(R.string.bookmarkTitleTip));

									}

								}
							})
					.setNegativeButton(R.string.cancle,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							}).show();
		}
	};
	/** 退出按钮点击事件 **/
	public OnClickListener logoutButton = new OnClickListener() {

		public void onClick(View v) {
			promptExit(MainActivity.this);

		}
	};
	/** 更多按钮点击事件 **/
	public OnClickListener setupButton = new OnClickListener() {

		public void onClick(View v) {
			gms.showDialog(picIndex);

		}
	};

	/** 获取屏幕宽和高 **/
	public void getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		disWidth = dm.widthPixels;
		disHeight = dm.heightPixels;
	}

	/*** 设置图片 */
	public void setImageView(Bitmap bitmap) {
		imageView.setImageBitmap(bitmap);
		imageView.setScaleType(ScaleType.CENTER);
		bitmap = null;

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			if (path != null) {
				Util.saveFile(Constants.FILENAME, path, false);
			}
		} catch (Exception e) {

		}

	}
}
