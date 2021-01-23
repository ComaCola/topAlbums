package com.demo.albums.service;

import com.demo.albums.model.Album;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Deividas
 */
public interface IAlbumService {

    String ALBUM_ARTIS_ID_URL = "https://itunes.apple.com/lookup?amgArtistId=%d&entity=album&limit=5";
    String ALBUM_ARTIS_ID_AND_LIMIT_URL = "https://itunes.apple.com/lookup?amgArtistId=%d&entity=album&limit=%d";

    List<Album> loadAlbumList(Long artistId) throws IOException;

    List<Album> loadAlbumSet(Long artistId, Long limit) throws IOException;
}
