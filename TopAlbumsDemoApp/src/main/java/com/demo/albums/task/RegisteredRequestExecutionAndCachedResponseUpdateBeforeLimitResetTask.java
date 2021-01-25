package com.demo.albums.task;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForAlbum;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForArtist;
import com.demo.albums.service.IAlbumService;
import com.demo.albums.service.IArtistService;
import com.demo.albums.service.ICachedResponseService;
import java.time.LocalTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.demo.albums.service.IRegisteredRequestLogService;
import com.demo.albums.service.IRequestCounter;

/**
 *
 * @author Deividas
 */
@Component
public class RegisteredRequestExecutionAndCachedResponseUpdateBeforeLimitResetTask {

    private final static long HOURS_24 = 24;

    private final IArtistService artistService;

    private final IAlbumService albumService;

    private final IRegisteredRequestLogService registeredRequestLogService;

    private final IRequestCounter requestCounter;

    private final ICachedResponseService cachedResponseService;

    @Autowired
    public RegisteredRequestExecutionAndCachedResponseUpdateBeforeLimitResetTask(
            IArtistService artistService, IAlbumService albumService,
            IRegisteredRequestLogService logService, IRequestCounter requestCounter,
            ICachedResponseService cachedResponseService) {
        this.artistService = artistService;
        this.albumService = albumService;
        this.registeredRequestLogService = logService;
        this.requestCounter = requestCounter;
        this.cachedResponseService = cachedResponseService;
    }

    // 5 seconds before limit reset - execution of registered requests or update of cached responses' data
    @Scheduled(cron = "55 59 * * * *")
    public void executeTask() {

        // if limit exceeded
        if (requestCounter.isRequestLimitExceeded()) {
            // do nothing
        } else {
            if (!registeredRequestLogService.isEmptyRequestAfterLimitMap()) {
                // if limit not exceeded - execution of logged requests (for most requested artists/albums)
                executeRegisteredRequests();
            } else {
                // if no logged requests - update of cached responses' data (most requested artists/albums)
                updateCachedResponses();
            }
        }
    }

    private void executeRegisteredRequests() {
        int hour = LocalTime.now().getHour();

        // how many iterations to limit
        long emptyRequestCount = requestCounter.getRequestLimit() - requestCounter.getNewRequestCount();
        for (int i = 0; i < emptyRequestCount; i++) {
            // if new hour then limit is reseted
            if (isTimeToFinish(hour)) {
                System.out.println("Empty requests " + emptyRequestCount);
                System.out.println("Used requests for refresh/cached requests " + (emptyRequestCount - i));
                break;
            }

            // call logged request (most called request)
            // removed registered request
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

    // flag for 5 seconds
    private boolean isTimeToFinish(int hour) {
        return LocalTime.now().getHour() != hour;
    }

    private void updateCachedResponses() {
        int hour = LocalTime.now().getHour();
        long cachedResponseDataMapSize = cachedResponseService.getCachedResponseDataMapSize();
        long iter = 0;
        while (iter < cachedResponseDataMapSize || !isTimeToFinish(hour)) {
            cachedResponseService.updateMostRegisteredRequestXHoursBefore(HOURS_24);
            iter++;
        }
    }
}
