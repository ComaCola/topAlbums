package com.demo.albums.service;

import com.demo.albums.dao.ICachedResponseDao;
import com.demo.albums.model.CachedResponse.CachedResponse;
import com.demo.albums.model.CachedResponse.CachedResponseForAlbum;
import com.demo.albums.model.CachedResponse.CachedResponseForArtist;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deividas
 */
@Service
public class CachedResponseServiceImpl implements ICachedResponseService {

    private final ICachedResponseDao cachedResponseDao;

    private final IArtistService artistService;

    private final IAlbumService albumService;

    @Autowired
    public CachedResponseServiceImpl(ICachedResponseDao cachedResponseDao,
            IArtistService artistService, IAlbumService albumService) {
        this.cachedResponseDao = cachedResponseDao;
        this.artistService = artistService;
        this.albumService = albumService;
    }

    @Override
    public void removeCachedResponseOlderThanXHours(long hours) {
        cachedResponseDao.loadAllCachedResponse().entrySet()
                .removeIf(entry -> entry.getValue()
                .getLastRequest().isBefore(LocalDateTime.now().minusHours(hours)));
    }

    @Override
    public long getCachedResponseDataMapSize() {
        return cachedResponseDao.getCachedResponseDataMapSize();
    }

    @Override
    public void updateMostRegisteredRequestXHoursBefore(long hours) {
        CachedResponse cachedResponse = mostRegisteredRequestXHoursBefore(hours);
        if (cachedResponse instanceof CachedResponseForArtist) {
            CachedResponseForArtist cachedResponseForArtist = (CachedResponseForArtist) cachedResponse;
            artistService.loadArtistListFromItunes(cachedResponseForArtist.getArtistName());
        } else if (cachedResponse instanceof CachedResponseForAlbum) {
            CachedResponseForAlbum cachedResponseForAlbum = (CachedResponseForAlbum) cachedResponse;
            albumService.loadAlbumListFromItunes(cachedResponseForAlbum.getAmgArtistId());
        }
    }

    // find most requested data, which is updated not early than "hours" hours
    private CachedResponse mostRegisteredRequestXHoursBefore(long hours) {
        long requestCount = 0;
        String key = "";
        for (Map.Entry<String, CachedResponse> entry : cachedResponseDao.loadAllCachedResponse().entrySet()) {
            if (entry.getValue().getLastUpdate().isBefore(LocalDateTime.now().minusHours(hours))) {
                if (entry.getValue().getRequestCounter() > requestCount) {
                    requestCount = entry.getValue().getRequestCounter();
                    key = entry.getKey();
                }
            }
        }
        return cachedResponseDao.loadCachedResponse(key);
    }

}
