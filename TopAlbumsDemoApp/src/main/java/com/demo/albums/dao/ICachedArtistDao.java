package com.demo.albums.dao;

import com.demo.albums.model.Artist;
import java.util.List;

/**
 *
 * @author Deividas
 */
public interface ICachedArtistDao {

    boolean hasArtist(String name);

    List<Artist> loadArtist(String name);

    void saveArtist(String name, List<Artist> artistList);

    void removeArtist(String name);
}
