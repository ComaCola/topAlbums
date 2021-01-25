package com.demo.albums.dao;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;
import java.util.Map;

/**
 *
 * @author Deividas
 */
public interface IRegisteredRequestLogDao {

    boolean isEmptyRequestAfterLimitMap();

    void saveRegisteredRequestAfterLimit(String artistName);

    void saveRegisteredRequestAfterLimit(Long amgArttistId);

    void registerRequest(String artistName);

    RegisteredRequest loadRegisteredRequest(String key);

    Map<String, RegisteredRequest> loadAllRegisteredRequest();

    RegisteredRequest removeRegisteredRequest(String key);
}
