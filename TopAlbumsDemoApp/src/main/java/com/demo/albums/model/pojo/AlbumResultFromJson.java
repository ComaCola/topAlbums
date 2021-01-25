package com.demo.albums.model.pojo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Deividas
 */
@Data
public class AlbumResultFromJson implements Serializable {

    private Long resultCount;
    private List<Album> results;
}
