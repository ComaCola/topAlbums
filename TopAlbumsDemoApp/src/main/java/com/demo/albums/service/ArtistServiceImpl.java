package com.demo.albums.service;

import com.demo.albums.model.pojo.Artist;
import com.demo.albums.model.pojo.ArtistResultFromJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.albums.dao.IRegisteredRequestLogDao;
import com.demo.albums.dao.ICachedResponseDao;
import com.demo.albums.model.CachedResponse.CachedResponseForArtist;
import java.time.LocalDateTime;

/**
 *
 * @author Deividas
 */
@Service
public class ArtistServiceImpl implements IArtistService {

    private final static String ARTIST_TERM_URL = "https://itunes.apple.com/search?entity=allArtist&term=%s";

    private static final ObjectMapper mapper = new ObjectMapper();

    private final ICachedResponseDao cachedResponseDao;
    private final IRegisteredRequestLogDao registeredRequestLogDao;
    private final IRequestCounter requestCounter;

    @Autowired
    public ArtistServiceImpl(ICachedResponseDao cachedArtistDao,
            IRegisteredRequestLogDao registeredRequestLogDao,
            IRequestCounter requestCounter) {
        this.cachedResponseDao = cachedArtistDao;
        this.registeredRequestLogDao = registeredRequestLogDao;
        this.requestCounter = requestCounter;
    }

    @Override
    public List<Artist> loadArtistList(String artistName) {

        // load from cache/DB
        if (cachedResponseDao.hasCachedResponse(artistName)) {
            requestCounter.incrementCachedRequestCounter();
            CachedResponseForArtist cachedResponse = (CachedResponseForArtist) cachedResponseDao.loadCachedResponse(artistName);
            cachedResponse.incrementRequestCounter();
            cachedResponse.setLastRequest(LocalDateTime.now());
            return cachedResponse.getArtistList();
        }

        // limit exceeded - request registering to log for later execution
        if (requestCounter.isRequestLimitExceeded()) {
            requestCounter.incrementRequestAfterLimitCounter();
            registeredRequestLogDao.saveRegisteredRequestAfterLimit(artistName);
            return null;
        }

        // load from iTunes
        // registered data to cache/DB
        requestCounter.incrementNewRequestCounter();
        return loadArtistListFromItunes(artistName);

    }

    @Override
    public List<Artist> loadArtistListFromItunes(String artistName) {
        ArtistResultFromJson resultArtist = null;
        try {
            InputStream inputStream = inputStreamFromUrl(String.format(ARTIST_TERM_URL, artistName));
            resultArtist = mapper.readValue(inputStream, ArtistResultFromJson.class);
            inputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (resultArtist == null || resultArtist.getResults().isEmpty()) {
            cachedResponseDao.saveArtistList(artistName, null);
            return null;
        }
        cachedResponseDao.saveArtistList(artistName, resultArtist.getResults());
        return resultArtist.getResults();
    }

    private InputStream inputStreamFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return url.openStream();
    }

    @Override
    public String getArtistTermUrl() {
        return ARTIST_TERM_URL;
    }

}
