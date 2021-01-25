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
public class RegisteredRequestForArtist extends RegisteredRequest implements Serializable {

    private String artistName;
}
