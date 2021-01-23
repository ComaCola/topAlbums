package com.demo.albums.dao;

import com.demo.albums.model.Artist;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class CachedArtistDaoImpl implements ICachedArtistDao {

    private static final Map<String, List<Artist>> dataMap = new HashMap<>();

    @Override
    public boolean hasArtist(String name) {
        return dataMap.containsKey(name);
    }

    @Override
    public void saveArtist(String name, List<Artist> artistList) {
        dataMap.put(name, artistList);
    }

    @Override
    public List<Artist> loadArtist(String name) {
        return dataMap.get(name);
    }

    @Override
    public void removeArtist(String name) {
        dataMap.remove(name);
    }
}
