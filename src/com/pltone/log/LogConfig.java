package com.pltone.log;

import com.pltone.init.FilePathInit;
import com.pltone.ui.MyFrame;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.LogLog;

import java.io.IOException;

/**
 * 日志配置
 * 
 * @author chenlong
 * @version 1.0 2018-02-13
 */
public class LogConfig {
	private static final String CONVERSION_PATTERN = "[%-5p][%d{yyyy-MM-dd HH:mm:ss}][%l]:%m%n";
	private static final String DATE_PATTERN = "'_'yyyyMMdd'.log'";
	private static final Logger LOG4J_ROOT_LOGGER = Logger.getRootLogger();
	private static final Layout PATTERN_LAYOUT = new PatternLayout(CONVERSION_PATTERN);

	private LogConfig() {
	}

    /**
     * 初始化log4j配置
     */
	public static final void initLog4j() {
        // 设置日志最低输出级别
        LOG4J_ROOT_LOGGER.setLevel(Level.DEBUG);

        // 添加控制台日志输出器
        ConsoleAppender consoleAppender = new ConsoleAppender(PATTERN_LAYOUT, ConsoleAppender.SYSTEM_OUT);
        LOG4J_ROOT_LOGGER.addAppender(consoleAppender);
    }

    /**
     * 添加窗体日志输出器
     * @param frame  {@link MyFrame}
     */
    public static void addFrameLogAppender(MyFrame frame) {
        FrameLogAppender frameLogAppender = new FrameLogAppender(frame.getTextArea());
        LOG4J_ROOT_LOGGER.addAppender(frameLogAppender);
    }

    public static void addDailyFileAppender() {
        String filename = FilePathInit.getLogFileName();
        try {
            DailyRollingFileAppender dailyFileAppender = new DailyRollingFileAppender(PATTERN_LAYOUT, filename, DATE_PATTERN);
            dailyFileAppender.setAppend(true);
            LOG4J_ROOT_LOGGER.addAppender(dailyFileAppender);
        } catch (IOException e) {
            LogLog.error("配置每日日志输出器异常", e);
        }
    }

	/**
	 * 编程配置log4j
	 * 
	 * @param frame
	 *            {@link MyFrame}
	 */
	public static final void configLog4j(MyFrame frame) {
		// 设置日志最低输出级别
		LOG4J_ROOT_LOGGER.setLevel(Level.DEBUG);
		// 添加窗体日志输出器
		FrameLogAppender frameLogAppender = new FrameLogAppender(frame.getTextArea());
		LOG4J_ROOT_LOGGER.addAppender(frameLogAppender);

		Layout layout = new PatternLayout(CONVERSION_PATTERN);
		// 添加控制台日志输出器
		ConsoleAppender consoleAppender = new ConsoleAppender(layout, ConsoleAppender.SYSTEM_OUT);
		LOG4J_ROOT_LOGGER.addAppender(consoleAppender);

		// 获取当前jar包路径
		// String path = System.getProperty("java.class.path");
		// LogLog.debug("path1: " + path);
		// System.out.println("path1: " + path);
		// int firstIndex =
		// path.lastIndexOf(System.getProperty("path.separator")) + 1;
		// int lastIndex = path.lastIndexOf(File.separator) + 1;
		// path = path.substring(firstIndex, lastIndex);
		// LogLog.debug("path2: " + path);
		// System.out.println("path2: " + path);

		String filename = FilePathInit.LOG_DIR.concat("/distribute.log");
		try {
			DailyRollingFileAppender dailyFileAppender = new DailyRollingFileAppender(layout, filename, DATE_PATTERN);
//			dailyFileAppender.setThreshold(Level.INFO);
			dailyFileAppender.setAppend(true);
			LOG4J_ROOT_LOGGER.addAppender(dailyFileAppender);
		} catch (IOException e) {
			LogLog.error("配置每日日志输出器异常", e);
		}

	}

}
