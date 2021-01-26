package service;

import com.demo.albums.Application;
import com.demo.albums.dao.IRegisteredRequestLogDao;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForArtist;
import com.demo.albums.service.IRegisteredRequestLogService;
import java.time.LocalDateTime;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Deividas
 */
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class RegisteredRequestTest {

    @Autowired
    private IRegisteredRequestLogService registeredRequestLogService;

    @Autowired
    private IRegisteredRequestLogDao registeredRequestLogDao;

    public RegisteredRequestTest() {
    }

    @Test
    public void mostRegisteredRequestTest() {
        registeredRequestLogDao.loadAllRegisteredRequest().clear();
        assertTrue(registeredRequestLogDao.loadAllRegisteredRequest().isEmpty());

        registeredRequestLogDao.saveRegisteredRequestAfterLimit("Artist4");
        assertTrue(registeredRequestLogDao.loadAllRegisteredRequest().size() == 1);

        RegisteredRequestForArtist artist4 = (RegisteredRequestForArtist) registeredRequestLogService.mostRegisteredRequest();
        assertEquals("Artist4", artist4.getArtistName());
        assertTrue(1 == artist4.getRegisteredRequestCounter());

        registeredRequestLogDao.saveRegisteredRequestAfterLimit("Artist1");
        registeredRequestLogDao.saveRegisteredRequestAfterLimit("Artist4");
        registeredRequestLogDao.saveRegisteredRequestAfterLimit("Artist4");
        artist4 = (RegisteredRequestForArtist) registeredRequestLogService.mostRegisteredRequest();
        assertTrue(3 == artist4.getRegisteredRequestCounter());

    }

    @Test
    public void removeRegisteredRequestOlderThan5HoursTest() {
        registeredRequestLogDao.loadAllRegisteredRequest().clear();
        assertTrue(registeredRequestLogDao.loadAllRegisteredRequest().isEmpty());

        RegisteredRequestForArtist artistOld = RegisteredRequestForArtist
                .builder()
                .artistName("ArtistOld")
                .lastRequest(LocalDateTime.now().minusHours(10))
                .build();

        registeredRequestLogDao.loadAllRegisteredRequest().put("ArtistOld", artistOld);
        assertFalse(registeredRequestLogService.isEmptyRequestAfterLimitMap());

        registeredRequestLogDao.saveRegisteredRequestAfterLimit("Artist5");

        assertTrue(registeredRequestLogDao.loadAllRegisteredRequest().size() == 2);

        registeredRequestLogService.removeRegisteredRequestOlderThanXHours(9);
        assertTrue(registeredRequestLogDao.loadAllRegisteredRequest().size() == 1);
    }

    @Test
    public void registerRequestDirectlyTest() {
        // register request and count
        // registerRequest
        // loadRegisteredRequest
    }

    @Test
    public void removeRegisterRequestDirectlyTest() {

        //loadAllRegisteredRequest
        //removeRegisteredRequest
    }
}
