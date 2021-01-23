package com.demo.albums.service;

import com.demo.albums.model.Artist;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Deividas
 */
public interface IArtistService {

    String ARTIST_TERM_URL = "https://itunes.apple.com/search?entity=allArtist&term=%s";

    List<Artist> loadArtistList(String name) throws IOException;
}
