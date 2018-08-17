package com.pltone.init;

import java.io.File;

/**
 * 文件路径初始化
 * 
 * @author chenlong
 * @version 1.0 2018-02-27
 */
public class FilePathInit {
	/** 物流转发系统生成文件主存放路径 */
	public static String mainPath = "C:/distribute";
	/** 物流转发系统Sqlite数据库文件存放路径 */
	public static String sqlPath = "C:/distribute/sqldb";
	/** 物流转发系统日志文件存放路径 */
	public static String logPath = "C:/distribute/log";

	/**
	 * 初始化
	 */
	public static void init() {
		File[] roots = File.listRoots();
		boolean isDDiskExist = false;
		if (roots.length > 1) {
			isDDiskExist = roots[1].getPath().equalsIgnoreCase("D:\\") && roots[1].getTotalSpace() > 0;
		}
		if (isDDiskExist) {
			mainPath = "D:/distribute";
			sqlPath = "D:/distribute/sqldb";
			logPath = "D:/distribute/log";
		}
		createPath(mainPath);
		createPath(sqlPath);
		createPath(logPath);
	}

	/**
	 * 创建文件目录
	 * 
	 * @param path
	 *            目录路径
	 */
	private static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private FilePathInit() {
	}
}
