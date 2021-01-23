package controller;

import com.demo.albums.Application;
import com.demo.albums.model.Album;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    public AlbumControllerTest() {
    }

    @Test
    public void albumListNoContentTest() throws Exception {
        String urlPattern = "/album/%d";
        String artistId = "";
        String url = String.format(urlPattern, artistId);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 204);
    }

    @Test
    public void albumListLoadTest() throws Exception {
        String urlPattern = "/album/%d";
        long artistId = 3492;
        long defaultLimit = 5;
        String url = String.format(urlPattern, artistId);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 200);
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        ObjectMapper mapper = new ObjectMapper();
        List<Album> albumList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Album>>() {
        });
        assertNotNull(albumList);
        assertTrue(albumList.size() == defaultLimit);
    }

    @Test
    public void albumListLoadWithLimitTest() throws Exception {
        String urlPattern = "/album/%d/%d";
        long artistId = 3492;
        long limit = 10;
        String url = String.format(urlPattern, artistId, limit);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(status, 200);

        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());

        ObjectMapper mapper = new ObjectMapper();
        List<Album> albumList = mapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<Album>>() {
        });
        assertNotNull(albumList);
        assertTrue(albumList.size() == limit);
    }
}
