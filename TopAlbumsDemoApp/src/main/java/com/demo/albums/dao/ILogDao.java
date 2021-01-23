package com.demo.albums.dao;

/**
 *
 * @author Deividas
 */
public interface ILogDao {

    void incrementCachedRequestCounter();

    void incrementNewRequestCounter();

    void incrementRequestAfterLimitCounter();

    void resetRequestCounters();

    boolean isRequestLimitExceeded();

    boolean isEmptyRequestAfterLimitList();

    void saveRequestAfterLimit(String name);

    String loadRequestAfterLimit();
}
