package com.fdu201.jobmanager.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


import com.fdu201.jobmanager.executor.DailyExecutor;
import com.fdu201.jobmanager.executor.RecurringExecutor;
import com.fdu201.jobmanager.task.DemoTask;
import org.apache.log4j.Logger;


@WebListener
public class CommonContextListener implements ServletContextListener {
	private DemoTask task = new DemoTask();
//	private DailyExecutor dailyExecutor = new DailyExecutor(task, 1);
	private RecurringExecutor recurringExecutor = new RecurringExecutor(task, 1, 6000);
	private final static Logger logger = Logger.getLogger(CommonContextListener.class);

	public void contextInitialized(ServletContextEvent sce) {
		logger.info("-----------------CommonContextListener initialized.-------------------");
		// execute task every day at 2:00:00
//		dailyExecutor.startAt(22, 36, 00);
		recurringExecutor.startDuring(9,0,17,0);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		recurringExecutor.stop(false);
		logger.info("-----------------CommonContextListener stopped.--------------------");
	}

}
