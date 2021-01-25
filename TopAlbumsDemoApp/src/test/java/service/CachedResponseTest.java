package service;

import com.demo.albums.Application;
import com.demo.albums.dao.ICachedResponseDao;
import com.demo.albums.model.CachedResponse.CachedResponse;
import com.demo.albums.model.CachedResponse.CachedResponseForAlbum;
import com.demo.albums.model.CachedResponse.CachedResponseForArtist;
import com.demo.albums.model.pojo.Artist;
import com.demo.albums.service.IArtistService;
import com.demo.albums.service.ICachedResponseService;
import java.time.LocalDateTime;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.demo.albums.service.IRequestCounterService;
import java.util.List;
import java.util.stream.IntStream;

/**
 *
 * @author Deividas
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class CachedResponseTest {

    @Autowired
    private IArtistService artistService;

    @Autowired
    private ICachedResponseService cachedResponseService;

    @Autowired
    private ICachedResponseDao cachedResponseDao;

    @Autowired
    private IRequestCounterService requestCounterService;

    public CachedResponseTest() {
    }

    @Test
    public void oneSaveToDataMapTest() {
        cachedResponseDao.loadAllCachedResponse().clear();
        assertTrue(cachedResponseService.getCachedResponseDataMapSize() == 0);
        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 0);

        cachedResponseDao.saveArtistList("Artist1", null);

        assertTrue(cachedResponseService.getCachedResponseDataMapSize() == 1);

        cachedResponseDao.saveArtistList("Artist1", null);
        cachedResponseDao.saveArtistList("Artist1", null);

        assertTrue(cachedResponseService.getCachedResponseDataMapSize() == 1);

        CachedResponse artistGeneral = cachedResponseDao.loadCachedResponse("Artist1");

        assertTrue(artistGeneral instanceof CachedResponseForArtist);

        CachedResponseForArtist artist1 = (CachedResponseForArtist) artistGeneral;

        assertEquals("Artist1", artist1.getArtistName());
        assertNull(artist1.getArtistList());
    }

    @Test
    public void saveAndRemoveTest() {
        cachedResponseDao.loadAllCachedResponse().clear();
        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 0);

        CachedResponseForArtist artist2 = CachedResponseForArtist
                .builder()
                .artistName("Artist2")
                .lastRequest(LocalDateTime.now().minusHours(2))
                .build();
        cachedResponseDao.loadAllCachedResponse().put("Artist2", artist2);
        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 1);
        assertEquals(((CachedResponseForArtist) cachedResponseDao.loadCachedResponse("Artist2")).getArtistName(), "Artist2");

        // not removed, its too early
        cachedResponseService.removeCachedResponseOlderThanXHours(4);
        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 1);
        assertEquals(((CachedResponseForArtist) cachedResponseDao.loadCachedResponse("Artist2")).getArtistName(), "Artist2");

        // removed
        cachedResponseService.removeCachedResponseOlderThanXHours(2);
        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 0);
    }

    @Test
    public void countersTest() {
        cachedResponseDao.loadAllCachedResponse().clear();
        requestCounterService.resetRequestCounters();

        assertTrue(requestCounterService.getNewRequestCount() == 0);
        assertTrue(requestCounterService.getCachedRequestCount() == 0);
        assertTrue(requestCounterService.getRequestAfterLimitCount() == 0);
        assertFalse(requestCounterService.isRequestLimitExceeded());

        // make limit exceeded
        IntStream.range(0, 150).forEach(i -> requestCounterService.incrementNewRequestCounter());
        assertTrue(requestCounterService.getNewRequestCount() == 150);
        assertTrue(requestCounterService.isRequestLimitExceeded());

        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 0);
        CachedResponseForArtist artist3 = CachedResponseForArtist
                .builder()
                .artistName("Artist3")
                .build();
        cachedResponseDao.loadAllCachedResponse().put("Artist3", artist3);

        CachedResponseForAlbum album111 = CachedResponseForAlbum
                .builder()
                .amgArtistId(111L)
                .build();
        cachedResponseDao.loadAllCachedResponse().put("111", album111);

        assertTrue(cachedResponseDao.getCachedResponseDataMapSize() == 2);

        // client1 call for "Artist3"
        List<Artist> artistList = artistService.loadArtistList("Artist3");
        assertNull(artistList);
        assertTrue(artist3.getRequestCounter() == 1);

        // client2 call for "Artist3"
        artistService.loadArtistList("Artist3");
        // client3 call for "Artist3"
        artistService.loadArtistList("Artist3");

        assertTrue(artist3.getRequestCounter() == 3);
    }
}
