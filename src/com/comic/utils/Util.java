package com.comic.utils;
/**������*/
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
	//���浱ǰ�ѷŴ���С����ͼƬ·��
	public static String filePath=null;
	//��ǰ����Ŀ��
	public static int bmpWidth=0;
	public static int bmpHeight=0;
	private static float scaleWidth=1;
	private static float scaleHeight=1;
	private static Bitmap bitmap =null;
	private static int ScaleAngle=0;//��ת�Ƕ�
	
	public static String getSDcaradPath(){
		/**��ȡ·��LoadingActivity��Environment*/
		String rootPath=Environment.getExternalStorageDirectory().getAbsolutePath();
		if(rootPath!=null){
			return rootPath;
		}else{
		
		
		return null;
		}
		
	}
	/***���û���ʾToast��Ϣ*/
	public static void showMessage(Context context,String message){
		Toast.makeText(context, message, 10).show();
	}
	/**�������Դ*/
	public static List<Map<String, Object>> getListData(String path) {
     List<Map<String,Object>>list=new ArrayList<Map<String,Object>>();
     
       
		File file=new File(path);  //ͨ����������path
		//�����Ŀ¼��Ϊ�� ����!=��Ŀ¼��mnt
	
		if(file.getParent()!=null&&!"/mnt".equals(file.getParent())){
			//root������һ��
			Map<String,Object>root=new HashMap<String,Object>();//����    //map��ListView�е�ÿһ��
			
			root.put("img",R.drawable.lastdir);
			root.put("name",R.string.sdcard_goback_fatherDir);
			root.put("path", file.getParent());
			list.add(root);
		}
		File[]files=getFilesOrder(file);//������е��ļ��ļ���
		if(files!=null&&files.length>0){
		
			for(File f:files){  //�����ļ�
				Map<String,Object>item=new HashMap<String,Object>();//����    //map��ListView�е�ÿһ��
				   //  ����f    �Ǹ�Ŀ¼
				if(f.isDirectory()){
					item.put("img",R.drawable.folder);   //�����imgͼƬ����ʾ����folder�ļ��е�ͼƬ
					item.put("name", f.getName());
					item.put("path", f.getAbsolutePath());
					list.add(item);
					
				}else if(f.isFile()&& isPic(f.getPath())){
					item.put("img",R.drawable.pic);   //�����ͼƬ����ʾ����ͼƬͺư
					item.put("name", f.getName());
					item.put("path", f.getAbsolutePath());
					list.add(item);
				}
			}
			
		}
		
		return list;
	}
	/**�жϵ�ǰ�ļ��ǲ���һ��ͼƬ**/
	public static boolean isPic(String path) {
        //��Ϊ���Ҳ�Ϊ���ַ���
		if(path!=null&&!"".equals(path)){//sdcard/000.jpg  ������0��ʼ +1 ȡ.�����
			//��ȡ�ļ��ĺ�׺��          substringȡ�ַ���             ��ʲô��ʼ ��ʲô����
			String fileExt=path.substring(path.lastIndexOf(".")+1,path.length());
			//���Դ�Сд   ���ж��ǲ��Ǵ�4��ͼƬ
        	 if("jpg".equalsIgnoreCase(fileExt)||
        	    "png".equalsIgnoreCase(fileExt)||
        	    "gif".equalsIgnoreCase(fileExt)||
        	    "jpeg".equalsIgnoreCase(fileExt)){
        		 return true;///!!!!!!!!!!!
        		 
        	 }
         }
		return false;
	}
	/**��ȡĳ��·���������ļ����ļ��У���һ��˳�򷵻�*/
	public static File[] getFilesOrder(File file) {
        File[] files=null;
        //�ж��ļ��Ƿ����
        if(file.exists()){
        	files=file.listFiles();
        	File temp;
        	if(files!=null&&files.length>0){
        		for(int i=0;i<files.length;i++){
        			for(int j=0;j<files.length-i-1;j++){
        				//ǰ������ƴ��ں��������
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
	/**ͼƬ������ *///picPath·��   disWidth disHeight ��Ļ�ֱ��ʿ��
	public static Bitmap imageZoom(String picPath,int disWidth,int disHeight,String action){
	double scale;//�Ŵ���С����
	Bitmap newBitmap=null;
	ScaleAngle=0;
	int widthUri=0;
	int heightUri=0;
	BitmapFactory.Options options = new BitmapFactory.Options();
	options.inSampleSize=2;
	if((filePath !=null && !filePath.equals(picPath))||bmpWidth==0||bmpHeight==0){
		bitmap=BitmapFactory.decodeFile(picPath,options);
		//�õ���ͼ�ĸ�
		bmpWidth =bitmap.getWidth();
		bmpHeight=bitmap.getHeight();
		scaleWidth=1;
		scaleHeight=1;
		
	}
	if(picPath !=null && picPath.length()>0){
		//ԭͼ�Ŀ��
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
			//bmp��ǰ��  dis��Ļ
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
			//������ͼƬ
			newBitmap =Bitmap.createBitmap(bitmap,0,0,widthUri,heightUri,matrix,true);
			bmpWidth =newBitmap.getWidth();
			bmpHeight=newBitmap.getHeight();
		}
		return newBitmap;
		
	}
 /**ͼƬ��ת**/
	public static Bitmap imageRotate(String direction,String picPath){
		int scaleAngle=45;//ÿ����ת45��
		BitmapFactory.Options opts=new BitmapFactory.Options();
		opts.inSampleSize =2;
		if((filePath!=null && filePath.equals(picPath))||filePath==null){
			ScaleAngle=0;
			bitmap=BitmapFactory.decodeFile(picPath,opts);
			
		}
		filePath=picPath;
		int widthUri=bitmap.getWidth();
		int heightUri=bitmap.getHeight();
		//������ת ÿ��45��
		if("left".equalsIgnoreCase(direction)){
			ScaleAngle --;
			if(scaleAngle<=-8){//������ת8�λص�ԭ�㣬����������ת45��
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

	



