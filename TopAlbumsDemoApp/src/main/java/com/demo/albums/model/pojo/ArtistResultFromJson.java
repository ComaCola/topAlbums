package com.demo.albums.model.pojo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 *
 * @author Deividas
 */
@Data
public class ArtistResultFromJson implements Serializable {

    private Long resultCount;
    private List<Artist> results;
}
