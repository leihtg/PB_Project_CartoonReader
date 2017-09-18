package com.comic.utils;
/**常量类*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.comic.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Util {
	//保存当前已放大缩小过的图片路径
	public static String filePath=null;
	//当前处理的宽高
	public static int bmpWidth=0;
	public static int bmpHeight=0;
	private static float scaleWidth=1;
	private static float scaleHeight=1;
	private static Bitmap bitmap =null;
	private static int ScaleAngle=0;//旋转角度
	
	public static String getSDcaradPath(){
		/**获取路径LoadingActivity中Environment*/
		String rootPath=Environment.getExternalStorageDirectory().getAbsolutePath();
		if(rootPath!=null){
			return rootPath;
		}else{
		
		
		return null;
		}
		
	}
	/***给用户显示Toast信息*/
	public static void showMessage(Context context,String message){
		Toast.makeText(context, message, 10).show();
	}
	/**获得数据源*/
	public static List<Map<String, Object>> getListData(String path) {
     List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
     
       
		File file=new File(path);  //通过传过来的path
		//如果副目录不为空 并且!=父目录、mnt
	
		if(file.getParent()!=null&&!"/mnt".equals(file.getParent())){
			//root最上面一行
			Map<String,Object>root=new HashMap<String,Object>();//泛型    //map是ListView中的每一行
			
			root.put("img",R.drawable.lastdir);
			root.put("name",R.string.sdcard_goback_fatherDir);
			root.put("path", file.getParent());
			list.add(root);
		}
		File[]files=getFilesOrder(file);//获得所有的文件文件夹
		if(files!=null&&files.length>0){
		
			for(File f:files){  //遍历文件
				Map<String,Object>item=new HashMap<String,Object>();//泛型    //map是ListView中的每一行
				   //  遍历f    是个目录
				if(f.isDirectory()){
					item.put("img",R.drawable.folder);   //如果是img图片，显示的是folder文件夹的图片
					item.put("name", f.getName());
					item.put("path", f.getAbsolutePath());
					list.add(item);
					
				}else if(f.isFile()&& isPic(f.getPath())){
					item.put("img",R.drawable.pic);   //如果是图片，显示的是图片秃瓢
					item.put("name", f.getName());
					item.put("path", f.getAbsolutePath());
					list.add(item);
				}
			}
			
		}
		
		return list;
	}
	/**判断当前文件是不是一个图片**/
	public static boolean isPic(String path) {
        //不为空且不为空字符串
		if(path!=null&&!"".equals(path)){//sdcard/000.jpg  索引从0开始 +1 取.后面的
			//获取文件的后缀名          substring取字符串             以什么开始 以什么结束
			String fileExt=path.substring(path.lastIndexOf(".")+1,path.length());
			//忽略大小写   并判断是不是此4种图片
        	 if("jpg".equalsIgnoreCase(fileExt)||
        	    "png".equalsIgnoreCase(fileExt)||
        	    "gif".equalsIgnoreCase(fileExt)||
        	    "jpeg".equalsIgnoreCase(fileExt)){
        		 return true;///!!!!!!!!!!!
        		 
        	 }
         }
		return false;
	}
	/**获取某个路径下所有文件与文件夹，按一定顺序返回*/
	public static File[] getFilesOrder(File file) {
        File[] files=null;
        //判断文件是否存在
        if(file.exists()){
        	files=file.listFiles();
        	File temp;
        	if(files!=null&&files.length>0){
        		for(int i=0;i<files.length;i++){
        			for(int j=0;j<files.length-i-1;j++){
        				//前面的名称大于后面的名称
        				if(files[j].getName().compareTo(files[j+1].getName())>0){
        					temp = files[j];
        					files[j]=files[j+1];
        					files[j+1]=temp;
        					
        				}
        			}
        		}
        		
        	}
        	
        }
		return files;
	}
	/**图片的缩放 *///picPath路径   disWidth disHeight 屏幕分辨率宽高
	public static Bitmap imageZoom(String picPath,int disWidth,int disHeight,String action){
	double scale;//放大缩小多少
	Bitmap newBitmap=null;
	ScaleAngle=0;
	int widthUri=0;
	int heightUri=0;
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inSampleSize=2;
	if((filePath !=null && !filePath.equals(picPath))||bmpWidth==0||bmpHeight==0){
		bitmap=BitmapFactory.decodeFile(picPath,options);
		//得到新图的高
		bmpWidth =bitmap.getWidth();
		bmpHeight=bitmap.getHeight();
		scaleWidth=1;
		scaleHeight=1;
		
	}
	if(picPath !=null && picPath.length()>0){
		//原图的宽高
		widthUri=bitmap.getWidth();
		heightUri=bitmap.getHeight();
		
	}
	
		filePath =picPath;
		if("small".equalsIgnoreCase(action)){
			if((bmpWidth>disWidth/4 &&bmpHeight>disHeight/4)){
				scale =0.8;
				scaleWidth=(float) (scaleWidth*scale);
				scaleHeight=(float) (scaleHeight*scale);
				
			}
			//bmp当前的  dis屏幕
		}else if("big".equalsIgnoreCase(action)){
			if((bmpWidth<disWidth*2 &&bmpHeight<disHeight*2)){
				scale =1.2;
				scaleWidth=(float) (scaleWidth*scale);
				scaleHeight=(float) (scaleHeight*scale);
				
			}
			
			
		}
		if(bitmap!=null){
			Matrix matrix =new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			//创建新图片
			newBitmap =Bitmap.createBitmap(bitmap,0,0,widthUri,heightUri,matrix,true);
			bmpWidth =newBitmap.getWidth();
			bmpHeight=newBitmap.getHeight();
		}
		return newBitmap;
		
	}
 /**图片旋转**/
	public static Bitmap imageRotate(String direction,String picPath){
		int scaleAngle=45;//每次旋转45度
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inSampleSize =2;
		if((filePath!=null && filePath.equals(picPath))||filePath==null){
			ScaleAngle=0;
			bitmap=BitmapFactory.decodeFile(picPath,opts);
			
		}
		filePath=picPath;
		int widthUri=bitmap.getWidth();
		int heightUri=bitmap.getHeight();
		//向左旋转 每次45度
		if("left".equalsIgnoreCase(direction)){
			ScaleAngle --;
			if(scaleAngle<=-8){//向左旋转8次回到原点，重新向左旋转45度
				scaleAngle= -1;
				
			}
		}else if("right".equalsIgnoreCase(direction)){
			ScaleAngle ++;
			if(scaleAngle>=8){
				scaleAngle =1;
			}
		}
		Matrix matrix =new Matrix();
		matrix.setRotate(scaleAngle*ScaleAngle);
		return Bitmap.createBitmap(bitmap,0,0,widthUri,heightUri,matrix,true);

	}
   public static String saveFile(String fileName,String fileContent,boolean flag){
	   String sdCardPath=getSDcaradPath();
	   String root_temp_path=sdCardPath+Constants.TEMPPATH;
	   String filePath=null;
	   BufferedWriter bw=null;
	   FileOutputStream fos=null;
	   OutputStreamWriter osw=null;
	   if(sdCardPath!=null){
		   filePath=root_temp_path;
		   filePath=root_temp_path+fileName;
		   try {
			fos=new FileOutputStream(new File(filePath),flag );
			osw=new OutputStreamWriter(fos);
			bw=new BufferedWriter(osw);
			bw.write(fileContent);
			bw.newLine();
			bw.flush();
			}
		    catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(bw!=null){
				try {
					bw.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
			}
			if(osw!=null){
				try {
					osw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		   
	   }
	return filePath;
   }
	
   public static String getFileRead(String filePath){
		String sdCardPath = getSDcaradPath();
		String root_temp_path = sdCardPath+Constants.TEMPPATH;
		FileReader fr = null;
		BufferedReader bfr = null;
		String content = null;
		if(sdCardPath!=null){
			String path = root_temp_path;
			path+=filePath;
			File file = new File(path);
			if(file.exists()){
				String line="";
				content = new String();
				try {
					fr = new FileReader(file);
					bfr = new BufferedReader(fr);
						while((line=bfr.readLine())!=null){
						   content+=line.trim();
						}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try{
						if(bfr!=null){
							bfr.close();
						}if(fr!=null){
							fr.close();
						}
					}catch(IOException e){
					Log.e("util", e.getMessage());
					}
				}
				}
			}
		return content;
			
		}
	}

	



