package com.demo.albums.task;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForAlbum;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForArtist;
import com.demo.albums.service.IAlbumService;
import com.demo.albums.service.IArtistService;
import com.demo.albums.service.ICachedResponseService;
import com.demo.albums.service.IRegisteredRequestLogService;
import com.demo.albums.service.IRequestCounter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class RegisteredRequestExecutionAndCachedResponseUpdateAtAnyTimeTask {

    private final static long HOURS_24 = 24;

    private final IArtistService artistService;

    private final IAlbumService albumService;

    private final IRegisteredRequestLogService registeredRequestLogService;

    private final IRequestCounter requestCounter;

    private final ICachedResponseService cachedResponseService;

    @Autowired
    public RegisteredRequestExecutionAndCachedResponseUpdateAtAnyTimeTask(
            IArtistService artistService, IAlbumService albumService,
            IRegisteredRequestLogService logService, IRequestCounter requestCounter,
            ICachedResponseService cachedResponseService) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.registeredRequestLogService = logService;
        this.requestCounter = requestCounter;
        this.cachedResponseService = cachedResponseService;
    }

    // executed registered request
    @Scheduled(cron = "0 10,30,50 * * * *")
    public void registeredRequestExecuteTask() {

        // if limit exceeded
        if (requestCounter.isRequestLimitExceeded()) {
            // do nothing
        } else {
            executeRegisteredRequests(5);
        }
    }

    // update cached data
    @Scheduled(cron = "0 5,25,45 * * * *")
    public void cachedResponseUpdateTask() {

        // if limit exceeded
        if (requestCounter.isRequestLimitExceeded()) {
            // do nothing
        } else {
            updateCachedResponses(5);
        }
    }

    private void executeRegisteredRequests(long executeCount) {
        for (int i = 0; i < executeCount; i++) {
            executeRegisteredRequest();
        }
    }

    private void executeRegisteredRequest() {
        RegisteredRequest registeredRequest = registeredRequestLogService.mostRegisteredRequest();
        if (registeredRequest instanceof RegisteredRequestForArtist) {
            //search for artist (directly from iTunes)
            RegisteredRequestForArtist registeredRequestForArtist = (RegisteredRequestForArtist) registeredRequest;
            registeredRequestLogService.removeRegisteredRequest(registeredRequestForArtist.getArtistName());
            artistService.loadArtistListFromItunes(registeredRequestForArtist.getArtistName());
        } else if (registeredRequest instanceof RegisteredRequestForAlbum) {
            //search for album (directly from iTunes)
            RegisteredRequestForAlbum registeredRequestForAlbum = (RegisteredRequestForAlbum) registeredRequest;
            registeredRequestLogService.removeRegisteredRequest(registeredRequestForAlbum.getAmgArtistId().toString());
            albumService.loadAlbumListFromItunes(registeredRequestForAlbum.getAmgArtistId());
        }
    }

    private void updateCachedResponses(long updateCount) {
        for (int i = 0; i < updateCount; i++) {
            cachedResponseService.updateMostRegisteredRequestXHoursBefore(HOURS_24);
        }
    }
}
