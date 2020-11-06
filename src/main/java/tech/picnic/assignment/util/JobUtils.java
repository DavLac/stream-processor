package tech.picnic.assignment.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JobUtils {

    private static final Logger log = LoggerFactory.getLogger(JobUtils.class);

    private JobUtils() {
        // Util class. Do not instantiate
    }

    public static void runJobWithTimeOut(Runnable runnable, Duration timeoutDuration) {
        ExecutorService executor = Executors.newCachedThreadPool();

        Future<?> future = executor.submit(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });

        executor.shutdown();

        try {
            future.get(timeoutDuration.getSeconds(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("Job was interrupted");
        } catch (ExecutionException e) {
            log.error("Caught exception: {}", e.getCause());
        } catch (TimeoutException e) {
            log.warn("Job timeout. Max duration = {} sec", timeoutDuration.getSeconds());
            future.cancel(true);
        }
    }
}
