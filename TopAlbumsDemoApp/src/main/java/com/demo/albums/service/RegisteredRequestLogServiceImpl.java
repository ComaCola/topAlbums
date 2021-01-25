package com.demo.albums.service;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.albums.dao.IRegisteredRequestLogDao;
import java.time.LocalDateTime;

/**
 *
 * @author Deividas
 */
@Service
public class RegisteredRequestLogServiceImpl implements IRegisteredRequestLogService {

    private final IRegisteredRequestLogDao registeredRequestLogDao;

    @Autowired
    public RegisteredRequestLogServiceImpl(IRegisteredRequestLogDao registeredRequestLogDao) {
        this.registeredRequestLogDao = registeredRequestLogDao;
    }

    @Override
    public RegisteredRequest mostRegisteredRequest() {
        long requestCount = 0;
        String key = "";
        Map<String, RegisteredRequest> registeredRequestMap = registeredRequestLogDao.loadAllRegisteredRequest();
        for (Map.Entry<String, RegisteredRequest> entry : registeredRequestMap.entrySet()) {
            if (entry.getValue().getRegisteredRequestCounter() > requestCount) {
                key = entry.getKey();
            }
        }
        return registeredRequestMap.get(key);
    }

    @Override
    public boolean isEmptyRequestAfterLimitMap() {
        return registeredRequestLogDao.isEmptyRequestAfterLimitMap();
    }

    @Override
    public RegisteredRequest removeRegisteredRequest(String key) {
        return registeredRequestLogDao.removeRegisteredRequest(key);
    }

    @Override
    public void removeRegisteredRequestOlderThanXHours(long hours) {
        registeredRequestLogDao.loadAllRegisteredRequest().entrySet()
                .removeIf(entry -> entry.getValue()
                .getLastRequest().isBefore(LocalDateTime.now().minusHours(hours)));
    }

}
