package com.demo.albums.task;

import com.demo.albums.service.IRequestCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class LimitResetTask {

    private final IRequestCounter requestCounter;

    @Autowired
    public LimitResetTask(IRequestCounter requestCounter) {
        this.requestCounter = requestCounter;
    }

    // counters reset every new hour for logging new iTunes' requests
    @Scheduled(cron = "0 0 * * * *")
    public void resetCounters() {
        System.out.println("New requests count " + requestCounter.getNewRequestCount());
        System.out.println("Cached requests count " + requestCounter.getCachedRequestCount());
        System.out.println("Requests after limit count " + requestCounter.getRequestAfterLimitCount());
        requestCounter.resetRequestCounters();
    }
}
