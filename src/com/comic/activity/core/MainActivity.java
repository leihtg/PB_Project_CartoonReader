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
	private List<String> imageList;// ����ͬһ·���µ�ͼƬ����
	private int picIndex = 0;// ͼƬ��λ��
	private RelativeLayout layout2;
	private RelativeLayout layout3;
	private RelativeLayout layout4;
	private TextView imageName;
	private TextView pagePosition;
	private ImageButton lastPage;
	private ImageButton nextPage;
	private GestureDetector gestureDetector;
	private int disWidth;// ��
	private int disHeight;// ��
	private GetMoreSetup gms; // ����
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		getDisplayMetrics();

		allActivity.add(this); // ���뵽������ȥ

		// ��ȡ����ؼ�
		gestureDetector = new GestureDetector(gestureListener);// ��ʼ���ƴ���
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

		// ���õ���¼�
		openSDcard.setOnClickListener(openSDcardButton);// ��SD
		page.setOnClickListener(pageButton);// ��ҳ
		zoomSmall.setOnClickListener(zoomButton);// ��С
		zoomBig.setOnClickListener(zoomButton);// �Ŵ�
		bookmark.setOnClickListener(bookmarkButton);// ��ǩ
		logout.setOnClickListener(logoutButton);// �ر�
		setup.setOnClickListener(setupButton);// ����
		lastPage.setOnClickListener(page_slideButton);
		nextPage.setOnClickListener(page_slideButton);

		imageView.setOnClickListener(hiddenMenu);// ���ýӿ�
		imageView.setLongClickable(true);// �����¼�Ϊ����

		path = this.getIntent().getStringExtra("picPath");
		if (path != null) {
			// Bitmap bm=new
			// BitmapFactory().decodeFile(path);//��path·������һ��BitMap
			// imageView.setImageBitmap(bm);
			imageList = loadImages();
			if (imageList != null && imageList.size() > 0) {
				setImageView();

			} else {
				// û������ͼƬʱ�����еĲ˵�����ʾ����
				layout2.setVisibility(View.VISIBLE);// �������Ƿ���ʾ
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.VISIBLE);
				// ��ʾû������ͼƬ
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
					pagePosition.setText(position);// ����setText����position
					path = imageList.get(picIndex);// ����picIndex�Ŵ���С
				}
			}

		};
		gms = new GetMoreSetup(MainActivity.this, imageView, imageList, handler);
	}

	// ���5�����ʧ
	private Runnable hideMenu = new Runnable() {

		public void run() {
			layout2.setVisibility(View.GONE);
			layout3.setVisibility(View.GONE);
			layout4.setVisibility(View.GONE);
			handler.removeCallbacks(hideMenu);
		}
	};

	// �ӿ�
	public OnClickListener hiddenMenu = new OnClickListener() {

		public void onClick(View v) {
			if (imageList != null && imageList.size() > 0) {
				layout2.setVisibility(View.VISIBLE);// �������Ƿ���ʾ
				layout3.setVisibility(View.VISIBLE);
				layout4.setVisibility(View.VISIBLE);
				handler.postDelayed(hideMenu, 5000);// 5������GONE ���ɼ�

			}

		}
	};

	/** ��ʾͼƬ **/
	private void setImageView() {
		// ͼƬ����
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;// ͼƬ�ĸ߿�Ϊԭ����1/2
		path = imageList.get(picIndex);
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		imageView.setImageBitmap(bitmap);
		setPageInfo();

	}

	/** ���ö������Ƽ���ǰ����λ�� **/
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

	/** װ��ͬһ·��������ͼƬ **/
	private List<String> loadImages() {
		ArrayList<String> list = new ArrayList<String>();
		File picFile = new File(path);
		if (!picFile.exists()) {
			return null;

		} else { // ����·��������
			File[] files = Util.getFilesOrder(new File(picFile.getParent()));
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (Util.isPic(file.getPath())) {
						list.add(file.getAbsolutePath());
					}

				}
				for (int i = 0; i < list.size(); i++) {// �жϴ��ݹ�����ͼƬ�ǵ�ǰ�ֵ�ͼƬ�����еĵڼ���
					String picPath = list.get(i);
					if (path.equalsIgnoreCase(picPath)) {
						picIndex = i;

					}
				}
			}
		}
		return list;
	}

	/** ���Ƽ��� **/
	public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
		gestureDetector.onTouchEvent(ev);
		imageView.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);

	};

	/** ���Ƽ����� **/
	GestureListener gestureListener = new GestureListener() {

		@Override
		public void doOnFling(String action) {
			if (imageList != null && imageList.size() > 0) {
				if ("right".equals(action)) {// ��һҳ
					if (picIndex < imageList.size() - 1) {
						picIndex = picIndex + 1;// =======picIndex+=1;
						setImageView();

					} else {
						Util.showMessage(MainActivity.this,
								getString(R.string.pageLast));
					}
				}
				if ("left".equals(action)) {// ��һҳ
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

	/** ��ʾ��һҳ����һҳ��ť����¼� */
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
				// imageList.size ����ͼƬ
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

	/** ��SDcard��ť����¼� **/
	public OnClickListener openSDcardButton = new OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, TabMainActivity.class);
			startActivity(intent);

		}
	};
	/** ��ҳ��ť����¼� **/
	public OnClickListener pageButton = new OnClickListener() {

		public void onClick(View v) {
			String[] pageArray = { getString(R.string.firstPage),
					getString(R.string.beforePage),
					getString(R.string.nextPage), getString(R.string.lastPage) };
			DefaultDialog pageDialog = new DefaultDialog(MainActivity.this,
					pageArray, false) {
				protected void doItems(int which) {
					switch (which) {
					case 0:// ��һҳ
						if (picIndex >= 1) {
							picIndex = 0;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageFirst));
						}
						break;
					case 1:// ��һҳ
						if (picIndex >= 1) {
							picIndex -= 1;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageFirst));
						}
						break;
					case 2:// ��һҳ
							// imageList.size()-1���һҳ
						if (picIndex < imageList.size() - 1) {
							picIndex += 1;
							setImageView();
						} else {
							Util.showMessage(MainActivity.this,
									getString(R.string.pageLast));
						}
						break;
					case 3:// ���һҳ
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

			pageDialog.setTitle(R.string.pageTitle);// ����

			pageDialog.show();

		}
	};
	/** ���Ű�ť����¼� **/
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
	// /**�Ŵ�ť����¼�**/
	// public OnClickListener zoomBigButton=new OnClickListener() {
	//
	//
	// public void onClick(View v) {
	//
	//
	// }
	// };
	/** ��ǩ��ť����¼� **/
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
										// д��ĸ�ʽabc|2013-1-3
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
	/** �˳���ť����¼� **/
	public OnClickListener logoutButton = new OnClickListener() {

		public void onClick(View v) {
			promptExit(MainActivity.this);

		}
	};
	/** ���ఴť����¼� **/
	public OnClickListener setupButton = new OnClickListener() {

		public void onClick(View v) {
			gms.showDialog(picIndex);

		}
	};

	/** ��ȡ��Ļ��͸� **/
	public void getDisplayMetrics() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		disWidth = dm.widthPixels;
		disHeight = dm.heightPixels;
	}

	/*** ����ͼƬ */
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
