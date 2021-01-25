package com.demo.albums.service;

/**
 *
 * @author Deividas
 */
public interface IRequestCounterService {

    void incrementCachedRequestCounter();

    void incrementNewRequestCounter();

    void incrementRequestAfterLimitCounter();

    void resetRequestCounters();

    boolean isRequestLimitExceeded();

    long getRequestLimit();

    long getNewRequestCount();

    long getCachedRequestCount();

    long getRequestAfterLimitCount();
}
