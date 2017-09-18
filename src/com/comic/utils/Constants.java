package com.comic.utils;

import java.io.File;

public class Constants {
	//路径                                                       separato  判断系统的 斜杠
	public static String separator=File.separator;
	                                  //创建SDcard下的文件夹
    public static final String ROOTPATH=separator+"cartoonReader";
    public static final String TEMPPATH=ROOTPATH+separator+"temp";
    public static final String BOOKMARKS=separator+"bookMarks.txt";
    public static final String FILENAME=separator+"fileHistory.txt";
}
