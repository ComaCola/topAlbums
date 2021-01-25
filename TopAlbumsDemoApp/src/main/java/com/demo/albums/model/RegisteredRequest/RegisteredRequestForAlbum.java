package com.demo.albums.model.RegisteredRequest;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Deividas
 */
@Data
@SuperBuilder
public class RegisteredRequestForAlbum extends RegisteredRequest implements Serializable {

    private Long amgArtistId;
}
