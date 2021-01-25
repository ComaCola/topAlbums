package com.demo.albums.service;

import com.demo.albums.model.pojo.Artist;
import java.util.List;

/**
 *
 * @author Deividas
 */
public interface IArtistService {

    List<Artist> loadArtistList(String name);

    List<Artist> loadArtistListFromItunes(String artistName);

    String getArtistTermUrl();
}
