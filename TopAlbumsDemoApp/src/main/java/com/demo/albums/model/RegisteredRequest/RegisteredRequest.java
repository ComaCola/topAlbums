package com.demo.albums.model.RegisteredRequest;

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
public class RegisteredRequest implements Serializable {

    private RequestTypeEnum requestType;
    private long registeredRequestCounter;
    private LocalDateTime lastRequest;

    public void incrementRegisteredRequestCounter() {
        registeredRequestCounter++;
    }
}
