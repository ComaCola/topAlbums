package com.demo.albums.service;

import org.springframework.stereotype.Service;

/**
 *
 * @author Deividas
 */
@Service
public class RequestCounterImpl implements IRequestCounter {

    private static final long REQUEST_LIMIT = 10;
    private static long newRequestCounter;          // count of requests when iTunes was called (no data found in cache)
    private static long cachedRequestCounter;       // count of requests when data found in cache (no call to iTunes)
    private static long requestAfterLimitCounter;   // count of requests when limit was exceeded

    @Override
    public void incrementCachedRequestCounter() {
        cachedRequestCounter++;
    }

    @Override
    public void incrementNewRequestCounter() {
        newRequestCounter++;

    }

    @Override
    public void incrementRequestAfterLimitCounter() {
        requestAfterLimitCounter++;
    }

    @Override
    public void resetRequestCounters() {
        newRequestCounter = 0;
        cachedRequestCounter = 0;
        requestAfterLimitCounter = 0;
    }

    @Override
    public boolean isRequestLimitExceeded() {
        return newRequestCounter >= REQUEST_LIMIT;
    }

    @Override
    public long getRequestLimit() {
        return REQUEST_LIMIT;
    }

    @Override
    public long getNewRequestCount() {
        return newRequestCounter;
    }

    @Override
    public long getCachedRequestCount() {
        return cachedRequestCounter;
    }

    @Override
    public long getRequestAfterLimitCount() {
        return requestAfterLimitCounter;
    }
}
