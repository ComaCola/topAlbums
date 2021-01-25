package com.demo.albums.dao;

import com.demo.albums.model.pojo.Album;
import com.demo.albums.model.pojo.Artist;
import com.demo.albums.model.CachedResponse.CachedResponse;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Deividas
 */
public interface ICachedResponseDao {

    boolean hasCachedResponse(String name);

    CachedResponse loadCachedResponse(String key);

    Map<String, CachedResponse> loadAllCachedResponse();

    void saveArtistList(String name, List<Artist> artistList);

    void saveAlbumList(Long amgArtistId, List<Album> albumList);

    void removeCachedResponse(String name);

    long getCachedResponseDataMapSize();
}
