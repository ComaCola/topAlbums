package com.demo.albums.service;

import com.demo.albums.dao.ICachedArtistDao;
import com.demo.albums.dao.ILogDao;
import com.demo.albums.model.Artist;
import com.demo.albums.model.ArtistResultFromJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deividas
 */
@Service
public class ArtistServiceImpl implements IArtistService {

    private static final ObjectMapper mapper = new ObjectMapper();

    private final ICachedArtistDao cachedArtistDao;
    private final ILogDao logDao;

    @Autowired
    public ArtistServiceImpl(ICachedArtistDao cachedArtistDao, ILogDao logDao) {
        this.cachedArtistDao = cachedArtistDao;
        this.logDao = logDao;
    }

    @Override
    public List<Artist> loadArtistList(String name) throws IOException {

        // load from cache
        if (cachedArtistDao.hasArtist(name)) {
            logDao.incrementCachedRequestCounter();
            return cachedArtistDao.loadArtist(name);
        }

        // limit exceeded
        if (logDao.isRequestLimitExceeded()) {
            logDao.saveRequestAfterLimit(name);
            logDao.incrementRequestAfterLimitCounter();
            return null;
        }

        // load from iTunes
        logDao.incrementNewRequestCounter();
        InputStream inputStream = inputStreamFromUrl(String.format(ARTIST_TERM_URL, name));
        ArtistResultFromJson resultArtist = mapper.readValue(inputStream, ArtistResultFromJson.class);
        inputStream.close();
        if (resultArtist == null || resultArtist.getResults().isEmpty()) {
            return null;
        }
        return resultArtist.getResults();

    }

    private InputStream inputStreamFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return url.openStream();
    }
}
