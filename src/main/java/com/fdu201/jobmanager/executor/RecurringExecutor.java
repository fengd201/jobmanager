package com.fdu201.jobmanager.executor;

import com.fdu201.jobmanager.task.JobTask;
import org.apache.log4j.Logger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RecurringExecutor {
	private ScheduledExecutorService scheduledExecutor;
	private JobTask task;
	private int frequency;
	private final static Logger logger = Logger.getLogger(RecurringExecutor.class);
	
	/**
	 * Executor to execute recurring job
	 * 
	 * @param task task to be executed
	 * @param numOfThread number of threads
	 * @param frequency unit is second
	 */
	public RecurringExecutor(JobTask task, int numOfThread, int frequency) {
		this.scheduledExecutor = Executors.newScheduledThreadPool(numOfThread);
		this.task = task;
		this.frequency = frequency;
	}
	
	/**
	 * method to start task at certain time
	 * 
	 * @param targetHour target hour
	 * @param targetMin target minute
	 * @param targetSec target second
	 */
	public void startAt(int targetHour, int targetMin, int targetSec) {
		Runnable taskWrapper = new Runnable() {

			@Override
			public void run() {
				try {
					task.execute();					
				}
				catch (Exception e) {
					logger.error("Failed to execute task.", e);
				}
				startAt(targetHour, targetMin, targetSec);
			}
			
		};
		long delay = computeNextDelay(targetHour, targetMin, targetSec, frequency); 
		logger.info("Next task " +  task.getClass().getName() + " will be executed in " + delay + " seconds.");
		scheduledExecutor.schedule(taskWrapper, delay, TimeUnit.SECONDS);
	}
	
	public void stop(boolean waitForTask) {
		if (waitForTask) {
			scheduledExecutor.shutdown();
			try {
				logger.info("Waiting for pending task to be completed.");
				scheduledExecutor.awaitTermination(1, TimeUnit.DAYS);
			}
			catch (InterruptedException ie) {
				logger.error("Normal termination was interrupted.", ie);
				ie.printStackTrace();
			}
		}
		else {
			scheduledExecutor.shutdownNow();
			logger.info("DailyExecutor shut down.");
		}
	}
	
	private long computeNextDelay(int targetHour, int targetMin, int targetSec, int frequency) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
		if(zonedNow.compareTo(zonedNextTarget) > 0)
            zonedNextTarget = zonedNextTarget.plusSeconds(frequency);
		
		Duration duration = Duration.between(zonedNow, zonedNextTarget);
        return duration.getSeconds();
	}
}
