package com.demo.albums.service;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;

/**
 *
 * @author Deividas
 */
public interface IRegisteredRequestLogService {

    RegisteredRequest mostRegisteredRequest();

    boolean isEmptyRequestAfterLimitMap();

    RegisteredRequest removeRegisteredRequest(String key);

    void removeRegisteredRequestOlderThanXHours(long hour);
}
