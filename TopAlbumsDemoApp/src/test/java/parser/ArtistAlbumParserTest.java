package parser;

import com.demo.albums.Application;
import com.demo.albums.model.AlbumResultFromJson;
import com.demo.albums.model.ArtistResultFromJson;
import com.demo.albums.service.IAlbumService;
import com.demo.albums.service.IArtistService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author Deividas
 */
@SpringBootTest(classes = Application.class)
public class ArtistAlbumParserTest {

    private ObjectMapper mapper;

    public ArtistAlbumParserTest() {
        mapper = new ObjectMapper();
    }

    @Test
    public void artistArrayTest() throws JsonProcessingException, MalformedURLException, IOException {
        String term = "abba";
        String urlString = String.format(IArtistService.ARTIST_TERM_URL, term);
        ArtistResultFromJson resultInJson = mapper.readValue(inputStreamFromUrl(urlString), ArtistResultFromJson.class);

        Assert.assertNotNull(resultInJson);
        Assert.assertEquals(resultInJson.getResultCount().longValue(), resultInJson.getResults().size());
    }

    @Test
    public void albumArrayTest() throws IOException {
        long amgArtistId = 3492;
        String urlString = String.format(IAlbumService.ALBUM_ARTIS_ID_URL, amgArtistId);
        AlbumResultFromJson albumResultInJson = mapper.readValue(inputStreamFromUrl(urlString), AlbumResultFromJson.class);

        assertNotNull(albumResultInJson);
        assertNotNull(albumResultInJson.getResultCount());
        assertEquals(albumResultInJson.getResultCount().longValue(), albumResultInJson.getResults().size());
    }

    private InputStream inputStreamFromUrl(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        return url.openStream();
    }
}
