package com.demo.albums.model.CachedResponse;

import com.demo.albums.model.pojo.Album;
import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Deividas
 */
@Data
@SuperBuilder
public class CachedResponseForAlbum extends CachedResponse implements Serializable {

    private Long amgArtistId;
    private List<Album> albumList;
}
