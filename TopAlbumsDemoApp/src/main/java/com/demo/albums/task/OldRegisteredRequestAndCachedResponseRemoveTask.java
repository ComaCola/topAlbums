package com.demo.albums.task;

import com.demo.albums.service.ICachedResponseService;
import com.demo.albums.service.IRegisteredRequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author Deividas
 */
public class OldRegisteredRequestAndCachedResponseRemoveTask {

    private final static long HOURS_48 = 48;

    private final IRegisteredRequestLogService registeredRequestLogService;
    private final ICachedResponseService cachedResponseService;

    @Autowired
    public OldRegisteredRequestAndCachedResponseRemoveTask(IRegisteredRequestLogService registeredRequestLogService, ICachedResponseService cachedResponseService) {
        this.registeredRequestLogService = registeredRequestLogService;
        this.cachedResponseService = cachedResponseService;
    }

    @Scheduled(cron = "0 0,30 * * * *")
    public void removeOldRegisteredRequestTask() {
        registeredRequestLogService.removeRegisteredRequestOlderThanXHours(HOURS_48);
    }

    @Scheduled(cron = "0 15,45 * * * *")
    public void removeOldCachedResponseTask() {
        cachedResponseService.removeCachedResponseOlderThanXHours(HOURS_48);
    }

}
