package com.demo.albums.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.demo.albums.service.IRequestCounterService;

/**
 *
 * @author Deividas
 */
@Component
public class LimitResetTask {

    private final IRequestCounterService requestCounter;

    @Autowired
    public LimitResetTask(IRequestCounterService requestCounter) {
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
