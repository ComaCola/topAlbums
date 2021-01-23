package com.demo.albums.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Deividas
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist implements Serializable {

    @JsonProperty("artistId")
    private Long id;
    private String wrapperType;
    private String artistType;
    @JsonProperty("artistName")
    private String name;
    private String artistLinkUrl;
    private Long amgArtistId;
    private String primaryGenreName;
}
