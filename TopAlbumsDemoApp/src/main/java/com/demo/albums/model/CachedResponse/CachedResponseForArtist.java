package com.demo.albums.model.CachedResponse;

import com.demo.albums.model.pojo.Artist;
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
public class CachedResponseForArtist extends CachedResponse implements Serializable {

    private String artistName;
    private List<Artist> artistList;

}
