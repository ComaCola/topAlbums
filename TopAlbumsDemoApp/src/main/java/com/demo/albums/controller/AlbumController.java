package com.demo.albums.controller;

import com.demo.albums.model.pojo.Album;
import com.demo.albums.service.IAlbumService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Deividas
 */
@RestController
public class AlbumController {

    private final IAlbumService albumService;

    @Autowired
    public AlbumController(IAlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/album")
    public ResponseEntity getAlbum() {
        return emptyResponseEntity();
    }

    @GetMapping("/album/{artistId}")
    public ResponseEntity<List<Album>> getAlbums(@PathVariable Long artistId) throws IOException {
        List<Album> albumSet = albumService.loadAlbumList(artistId);
        if (albumSet == null) {
            return emptyResponseEntity();
        }
        return new ResponseEntity<>(albumSet, HttpStatus.OK);
    }

    private ResponseEntity emptyResponseEntity() {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
