package com.demo.albums.dao;

import com.demo.albums.model.pojo.Album;
import com.demo.albums.model.pojo.Artist;
import com.demo.albums.model.CachedResponse.CachedResponse;
import com.demo.albums.model.CachedResponse.CachedResponseForAlbum;
import com.demo.albums.model.CachedResponse.CachedResponseForArtist;
import com.demo.albums.model.enums.RequestTypeEnum;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class CachedResponseDaoImpl implements ICachedResponseDao {

    private static final Map<String, CachedResponse> cachedResponseDataMap = new HashMap<>();

    @Override
    public boolean hasCachedResponse(String name) {
        return cachedResponseDataMap.containsKey(name);
    }

    @Override
    public void saveArtistList(String artistName, List<Artist> artistList) {
        cachedResponseDataMap.put(artistName, CachedResponseForArtist
                .builder()
                .artistName(artistName)
                .artistList(artistList)
                .requestType(RequestTypeEnum.ARTIST)
                .requestCounter(1)
                .lastRequest(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public void saveAlbumList(Long amgArtistId, List<Album> albumList) {
        cachedResponseDataMap.put(amgArtistId.toString(), CachedResponseForAlbum
                .builder()
                .amgArtistId(amgArtistId)
                .albumList(albumList)
                .requestType(RequestTypeEnum.ALBUM)
                .requestCounter(1)
                .lastRequest(LocalDateTime.now())
                .lastUpdate(LocalDateTime.now())
                .build()
        );
    }

    @Override
    public CachedResponse loadCachedResponse(String key) {
        return cachedResponseDataMap.get(key);
    }

    @Override
    public Map<String, CachedResponse> loadAllCachedResponse() {
        return cachedResponseDataMap;
    }

    @Override
    public void removeCachedResponse(String name) {
        cachedResponseDataMap.remove(name);
    }

    @Override
    public long getCachedResponseDataMapSize() {
        return cachedResponseDataMap.size();
    }

}
