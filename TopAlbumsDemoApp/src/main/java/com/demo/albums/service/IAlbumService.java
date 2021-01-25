package com.demo.albums.service;

import com.demo.albums.model.pojo.Album;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Deividas
 */
public interface IAlbumService {

    List<Album> loadAlbumList(Long amgArtistId) throws IOException;

    List<Album> loadAlbumListFromItunes(Long amgArtistId);

    String getAlbumArtistIdUrl();
}
