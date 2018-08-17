package com.pltone;

import com.pltone.init.FilePathInit;
import com.pltone.init.SqliteDbInit;
import com.pltone.log.LogConfig;
import com.pltone.ui.MyFrame;

/**
 * 启动器
 * 
 * @author chenlong
 * @version 1.0 2018-02-12
 */
public class Main {

	public static void main(String[] args) {
		FilePathInit.init();
		MyFrame frame = new MyFrame();
		frame.init();
		LogConfig.configLog4j(frame);
		SqliteDbInit.init();
	}

}
