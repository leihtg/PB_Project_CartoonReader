package com.comic.activity.ext;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.comic.R;
import com.comic.Listener.GobackListener;
import com.comic.activity.core.BaseActivity;
import com.comic.adapter.ImageAdapter;
import com.comic.dialog.BookMarkDialog;
import com.comic.dialog.DefaultDialog;
import com.comic.utils.BookMark;
import com.comic.utils.Constants;
import com.comic.utils.Util;

public class BookMarkActivity extends BaseActivity {
	  private ImageButton goBackBtn;
	    private ListView bookmarkList;
	    private List<BookMark> list;
		public  void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.layout_bookmark_list);
			allActivity.add(this);
			goBackBtn=(ImageButton) this.findViewById(R.id.goBack);
			GobackListener goback = new GobackListener(this);
	        goBackBtn.setOnClickListener(goback);
	        bookmarkList = (ListView) findViewById(R.id.bookmarkList);
	        setBookMarkAdapter();
		}
		public List<BookMark> getFileContent(){
			List <BookMark>list = new ArrayList<BookMark>();
			String content = Util.getFileRead(Constants.BOOKMARKS);
			if(content!=null&&content.length()>0){
				//3|2012-03-29 16:19:41,/mnt/sdcard/cartoonReader/h10.png#2;
				String[]bookMarks = content.split(";");
				for(int i=0;i<bookMarks.length;i++){
					BookMark bm = new BookMark();
					String book = bookMarks[i];
					bm.setBookMarkName(book.substring(0,book.indexOf("|")));
					bm.setSaveTime(book.substring(book.indexOf("|")+1,book.indexOf(",")));
					bm.setImagePath(book.substring(book.indexOf(",")+1,book.indexOf("#")));
					bm.setPosition(Integer.parseInt(book.substring(book.indexOf("#")+1,book.length()))+1);
				    bm.setImageId(book.substring(book.indexOf("#")+1,book.length()));
				    list.add(bm);
				    bm = null;
				}
			}else{
				Util.showMessage(this, getString(R.string.nobookmark));
			}
			return list;
			
		}
		public void setBookMarkAdapter(){
			list = getFileContent();
			ImageAdapter imageAdapter = new ImageAdapter(list,BookMarkActivity.this);
			bookmarkList.setAdapter(imageAdapter);
			bookmarkList.setOnItemClickListener(itemClickListener);
		}
		private OnItemClickListener itemClickListener = new OnItemClickListener(){

			
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				BookMark bookmark = list.get(position);
				String[] bookMarkArrary = {getString(R.string.check),getString(R.string.delete),getString(R.string.deleteAll)};
			    DefaultDialog dialog = new BookMarkDialog(BookMarkActivity.this, bookmark, bookMarkArrary);
			    dialog.setTitle(R.string.bookmarkSetUp);
			    dialog.show();
			    
			}
			
		};
			


	}
