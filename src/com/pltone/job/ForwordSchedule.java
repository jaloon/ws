package com.pltone.job;

import com.pltone.init.SqliteDbInit;
import com.pltone.util.JdbcUtil;
import com.pltone.ws.Elock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时转发任务
 * 
 * @author chenlong
 * @version 1.0 2018-02-27
 */
public class ForwordSchedule {
	private static final Logger logger = LoggerFactory.getLogger(ForwordSchedule.class);
	private static final JdbcUtil JDBC_UTIL = new JdbcUtil();
	private static final String SQL_RT = "SELECT id, xml FROM \"tbl_distribute\" WHERE rt = 2 AND time BETWEEN DATETIME('now', 'localtime', '-30 minute') AND DATETIME('now', 'localtime');";
	private static final String SQL_PLT = "SELECT id, xml FROM \"tbl_distribute\" WHERE plt = 2 AND time BETWEEN DATETIME('now', 'localtime', '-30 minute') AND DATETIME('now', 'localtime');";

	/**
	 * 执行转发定时任务
	 * 
	 * @param elock
	 *            {@link Elock}
	 */
	public static ScheduledExecutorService executeForwordSchedule(Elock elock) {
		Runnable task = () -> {
			// task to run goes here
			JDBC_UTIL.createSqliteConnection(SqliteDbInit.SQLITE_DB_NAME);
			List<Map<String, Object>> rtXmls = JDBC_UTIL.selectForwordXml(SQL_RT);
			List<Map<String, Object>> pltXmls = JDBC_UTIL.selectForwordXml(SQL_PLT);
			JDBC_UTIL.close();
			// 使用Java8新特性：并行流parallelStream，充分利用多核CPU，比foreach效率提高两倍以上
			if (elock.isForwordRt() && rtXmls.size() > 0) {
				logger.info("定时任务：转发失败的配送信息重发瑞通服务器");
				rtXmls.parallelStream().forEach(map -> elock.forwordToRt((String) map.get("xml"), (int) map.get("id")));
			}
			if (elock.isForwordPlt() && pltXmls.size() > 0) {
				logger.info("定时任务：转发失败的配送信息重发普利通服务器");
				pltXmls.parallelStream().forEach(map -> elock.forwordToPlt((String) map.get("xml"), (int) map.get("id")));
			}
		};
		// ScheduledExecutorService是从Java SE5开始在java.util.concurrent包里做为并发工具类被引进的，这是最理想的定时任务实现方式。
		// 相比于使用java.util.Timer和线程等待来实现定时任务，它有以下好处：
		// 1>相比于Timer的单线程，它是通过线程池的方式来执行任务的
		// 2>可以很灵活的去设定第一次执行任务delay时间
		// 3>提供了良好的约定，以便设定执行的时间间隔
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		// 参数：1、任务体 2、首次执行的延时时间 3、任务执行间隔 4、间隔时间单位
		service.scheduleAtFixedRate(task, 10, 10, TimeUnit.MINUTES);
		return service;
	}
}
