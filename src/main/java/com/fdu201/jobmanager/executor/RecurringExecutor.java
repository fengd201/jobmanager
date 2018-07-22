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
     * method to start task during a window of time
     *
     * @param fromHour from hour
     * @param fromMin from minute
     * @param toHour to hour
     * @param toMin to minute
     */
    public void startDuring(int fromHour, int fromMin, int toHour, int toMin) {
        Runnable taskWrapper = new Runnable() {

            @Override
            public void run() {
                try {
                    task.execute();
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                    logger.error("Failed to execute task.", e);
                }
                startDuring(fromHour, fromMin, toHour, toMin);
            }

        };
        long delay = computeNextDelay(fromHour, fromMin, toHour, toMin, frequency);
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
	
	private long computeNextDelay(int fromHour, int fromMin, int toHour, int toMin, int frequency) {
		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.systemDefault();
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNextTarget = zonedNow;
		ZonedDateTime from = zonedNextTarget.withHour(fromHour).withMinute(fromMin);
		ZonedDateTime to = zonedNextTarget.withHour(toHour).withMinute(toMin);
		if (zonedNextTarget.compareTo(from) < 0)
			zonedNextTarget = from;
		else if (zonedNextTarget.compareTo(to) > 0)
			zonedNextTarget = from.plusDays(1);
		else
			zonedNextTarget = zonedNextTarget.plusSeconds(frequency);
		Duration duration = Duration.between(zonedNow, zonedNextTarget);
        return duration.getSeconds();
	}
}
