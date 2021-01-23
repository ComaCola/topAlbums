package com.demo.albums.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class LogDaoImpl implements ILogDao {

    private static final long REQUEST_LIMIT = 10;
    private static final AtomicLong newRequestCounter = new AtomicLong();
    private static final AtomicLong cachedRequestCounter = new AtomicLong();
    private static final AtomicLong requestAfterLimitCounter = new AtomicLong();

    private static final List<String> requestAfterLimitList = new ArrayList<>();

    @Override
    public void incrementCachedRequestCounter() {
        cachedRequestCounter.incrementAndGet();
    }

    @Override
    public void incrementNewRequestCounter() {
        newRequestCounter.incrementAndGet();

    }

    @Override
    public void incrementRequestAfterLimitCounter() {
        requestAfterLimitCounter.incrementAndGet();
    }

    @Override
    public void resetRequestCounters() {
        newRequestCounter.set(0);
        cachedRequestCounter.set(0);
        requestAfterLimitCounter.set(0);
    }

    @Override
    public boolean isRequestLimitExceeded() {
        return newRequestCounter.get() >= REQUEST_LIMIT;
    }

    @Override
    public boolean isEmptyRequestAfterLimitList() {
        return requestAfterLimitList.isEmpty();
    }

    @Override
    public void saveRequestAfterLimit(String name) {
        if (!requestAfterLimitList.contains(name)) {
            requestAfterLimitList.add(name);
        }
    }

    @Override
    public String loadRequestAfterLimit() {
        if (!requestAfterLimitList.isEmpty()) {
            return requestAfterLimitList.remove(0);
        }
        return null;
    }

}
