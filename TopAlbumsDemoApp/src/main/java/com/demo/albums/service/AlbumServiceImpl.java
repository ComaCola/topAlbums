package com.demo.albums.service;

import com.demo.albums.dao.ICachedResponseDao;
import com.demo.albums.dao.IRegisteredRequestLogDao;
import com.demo.albums.model.CachedResponse.CachedResponseForAlbum;
import com.demo.albums.model.pojo.Album;
import com.demo.albums.model.pojo.AlbumResultFromJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deividas
 */
@Service
public class AlbumServiceImpl implements IAlbumService {

    private final static String ALBUM_ARTIST_ID_URL = "https://itunes.apple.com/lookup?amgArtistId=%d&entity=album&limit=5";

    private static final ObjectMapper mapper = new ObjectMapper();

    private final ICachedResponseDao cachedResponseDao;
    private final IRegisteredRequestLogDao registeredRequestLogDao;
    private final IRequestCounterService requestCounter;

    @Autowired
    public AlbumServiceImpl(ICachedResponseDao cachedResponseDao, IRegisteredRequestLogDao registeredRequestLogDao, IRequestCounterService requestCounter) {
        this.cachedResponseDao = cachedResponseDao;
        this.registeredRequestLogDao = registeredRequestLogDao;
        this.requestCounter = requestCounter;
    }

    @Override
    public List<Album> loadAlbumList(Long amgArtistId) {

        // load from cache/DB
        if (cachedResponseDao.hasCachedResponse(amgArtistId.toString())) {
            requestCounter.incrementCachedRequestCounter();
            CachedResponseForAlbum cachedResponse = (CachedResponseForAlbum) cachedResponseDao.loadCachedResponse(amgArtistId.toString());
            cachedResponse.incrementRequestCounter();
            cachedResponse.setLastRequest(LocalDateTime.now());
            return cachedResponse.getAlbumList();
        }

        // limit exceeded - request registering to log for later execution
        if (requestCounter.isRequestLimitExceeded()) {
            requestCounter.incrementRequestAfterLimitCounter();
            registeredRequestLogDao.saveRegisteredRequestAfterLimit(amgArtistId);
            return null;
        }

        // load from iTunes
        // registered data to cache/DB
        requestCounter.incrementNewRequestCounter();
        return loadAlbumListFromItunes(amgArtistId);

    }

    @Override
    public List<Album> loadAlbumListFromItunes(Long amgArtistId) {
        AlbumResultFromJson resultAlbum = null;
        try {
            InputStream inputStream = inputStreamFromUrl(String.format(ALBUM_ARTIST_ID_URL, amgArtistId));
            resultAlbum = mapper.readValue(inputStream, AlbumResultFromJson.class);
            inputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        if (resultAlbum == null || resultAlbum.getResults().isEmpty()) {
            cachedResponseDao.saveAlbumList(amgArtistId, null);
            return null;
        }
        cachedResponseDao.saveAlbumList(amgArtistId, resultAlbum.getResults());
        return resultAlbum.getResults();
    }

    private InputStream inputStreamFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return url.openStream();
    }

    @Override
    public String getAlbumArtistIdUrl() {
        return ALBUM_ARTIST_ID_URL;
    }
}
