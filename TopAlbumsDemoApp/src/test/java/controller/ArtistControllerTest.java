package controller;

import com.demo.albums.Application;
import com.demo.albums.model.pojo.Artist;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 *
 * @author Deividas
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public ArtistControllerTest() {
    }

    @Test
    public void artistListNoContentTest() throws Exception {
        String urlPattern = "/artist/%s";
        String term = "";
        String url = String.format(urlPattern, term);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 204);
    }

    @Test
    public void artistListLoadTest() throws Exception {
        String urlPattern = "/artist/%s";
        String term = "abba";
        String url = String.format(urlPattern, term);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 200);
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        ObjectMapper mapper = new ObjectMapper();
        List<Artist> artistList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Artist>>() {
        });
        assertNotNull(artistList);
        assertTrue(artistList.size() > 0);
    }

}
