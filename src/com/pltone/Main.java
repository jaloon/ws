package com.pltone;

import com.pltone.cnf.ServiceProperties;
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
	    LogConfig.initLog4j();
        FilePathInit.init();
        LogConfig.addDailyFileAppender();
        MyFrame frame = new MyFrame();
        frame.initUI();
        LogConfig.addFrameLogAppender(frame);
        ServiceProperties.init();
        frame.initConf();
        SqliteDbInit.init();
	}

}
