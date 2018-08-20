package com.pltone.init;

import java.io.File;

/**
 * 文件路径初始化
 * 
 * @author chenlong
 * @version 1.0 2018-02-27
 */
public class FilePathInit {
    /** 应用程序主目录 */
	public static final String HOME_DIR = System.getProperty("user.dir");
	/** 文件目录分隔符 */
	public static final String FILE_SEP = System.getProperty("file.separator");
	/** 物流转发系统Sqlite数据库文件存放目录 */
	public static String DATA_DIR = new StringBuffer().append(HOME_DIR).append(FILE_SEP).append("data").toString();
	/** 物流转发系统日志文件存放目录 */
	public static String LOG_DIR = new StringBuffer().append(HOME_DIR).append(FILE_SEP).append("log").toString();

    /**
	 * 初始化
	 */
	public static void init() {
		createPath(DATA_DIR);
		createPath(LOG_DIR);
	}

	/**
	 * 创建文件目录
	 * 
	 * @param path {@link String} 目录路径
	 */
	private static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

    /**
     * 获取服务配置文件路径
     *
     * @return {@link String} 服务配置文件路径
     */
	public static String getPropFilePath() {
	    return new StringBuffer().append(HOME_DIR).append(FILE_SEP).append("service.properties").toString();
    }

    /**
     * 获取日志文件名称
     *
     * @return {@link String} 日志文件名称
     */
	public static String getLogFileName() {
	    return new StringBuffer().append(LOG_DIR).append(FILE_SEP).append("distribute").toString();
    }

	private FilePathInit() {
	}
}
