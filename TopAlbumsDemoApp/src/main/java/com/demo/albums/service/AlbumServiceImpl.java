package com.demo.albums.service;

import com.demo.albums.model.Album;
import com.demo.albums.model.AlbumResultFromJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author Deividas
 */
@Service
public class AlbumServiceImpl implements IAlbumService {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<Album> loadAlbumList(Long artistId) throws IOException {
        InputStream inputStream = inputStreamFromUrl(String.format(ALBUM_ARTIS_ID_URL, artistId));
        AlbumResultFromJson albumResult = mapper.readValue(inputStream, AlbumResultFromJson.class);
        inputStream.close();
        if (albumResult == null && albumResult.getResults().isEmpty()) {
            return null;
        }
        return albumResult.getResults().stream().filter(album -> "collection".equals(album.getWrapperType())).collect(Collectors.toList());
    }

    @Override
    public List<Album> loadAlbumSet(Long artistId, Long limit) throws IOException {
        InputStream inputStream = inputStreamFromUrl(String.format(ALBUM_ARTIS_ID_AND_LIMIT_URL, artistId, limit));
        AlbumResultFromJson albumResult = mapper.readValue(inputStream, AlbumResultFromJson.class);
        inputStream.close();
        if (albumResult == null || albumResult.getResults().isEmpty()) {
            return null;
        }
        return albumResult.getResults().stream().filter(album -> "collection".equals(album.getWrapperType())).collect(Collectors.toList());
    }

    private InputStream inputStreamFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return url.openStream();
    }
}
