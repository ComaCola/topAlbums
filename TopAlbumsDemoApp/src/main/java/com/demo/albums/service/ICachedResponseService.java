package com.demo.albums.service;

/**
 *
 * @author Deividas
 */
public interface ICachedResponseService {

    void removeCachedResponseOlderThanXHours(long hours);

    long getCachedResponseDataMapSize();

    void updateMostRegisteredRequestXHoursBefore(long hours);
}
