package com.demo.albums.model.CachedResponse;

import com.demo.albums.model.enums.RequestTypeEnum;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author Deividas
 */
@Data
@SuperBuilder
public class CachedResponse implements Serializable {

    private RequestTypeEnum requestType;
    private long requestCounter;
    private LocalDateTime lastRequest;
    private LocalDateTime lastUpdate;

    public void incrementRequestCounter() {
        requestCounter++;
    }
}
