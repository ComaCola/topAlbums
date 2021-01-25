package com.demo.albums.controller;

import com.demo.albums.model.pojo.Artist;
import com.demo.albums.service.IArtistService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Deividas
 */
@RestController
public class ArtistController {

    private final IArtistService artistService;

    @Autowired
    public ArtistController(IArtistService artistService) {
        this.artistService = artistService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("/artist")
    public void noArtist() {
    }

    @GetMapping(value = "/artist/{artistName}")
    public ResponseEntity<List<Artist>> getArtists(@PathVariable String artistName) throws IOException {
        List<Artist> artistList = artistService.loadArtistList(artistName);

        if (artistList == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(artistList, HttpStatus.OK);
    }
}
