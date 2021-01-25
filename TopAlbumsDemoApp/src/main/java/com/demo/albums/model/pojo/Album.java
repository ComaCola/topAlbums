package com.demo.albums.model.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author Deividas
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Album implements Serializable {

    @JsonProperty("collectionId")
    private Long id;
    private String wrapperType;
    private Long artistId;
    private String name;
    private String viewUrl;
}
