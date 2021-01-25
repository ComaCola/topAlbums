package com.demo.albums.dao;

import com.demo.albums.model.RegisteredRequest.RegisteredRequest;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForAlbum;
import com.demo.albums.model.RegisteredRequest.RegisteredRequestForArtist;
import com.demo.albums.model.enums.RequestTypeEnum;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author Deividas
 */
@Component
public class RegisteredRequestLogDaoImpl implements IRegisteredRequestLogDao {

    // customers made requests when limit was exceeded
    private static final Map<String, RegisteredRequest> registeredRequestAfterLimitMap = new HashMap<>();

    @Override
    public boolean isEmptyRequestAfterLimitMap() {
        return registeredRequestAfterLimitMap.isEmpty();
    }

    @Override
    public void saveRegisteredRequestAfterLimit(String artistName) {
        if (!registeredRequestAfterLimitMap.containsKey(artistName)) {
            registeredRequestAfterLimitMap.put(artistName, RegisteredRequestForArtist
                    .builder()
                    .artistName(artistName)
                    .lastRequest(LocalDateTime.now())
                    .requestType(RequestTypeEnum.ARTIST)
                    .registeredRequestCounter(1)
                    .build()
            );
        } else {
            registerRequest(artistName);
        }
    }

    @Override
    public void saveRegisteredRequestAfterLimit(Long amgArttistId) {
        if (!registeredRequestAfterLimitMap.containsKey(amgArttistId.toString())) {
            registeredRequestAfterLimitMap.put(amgArttistId.toString(), RegisteredRequestForAlbum
                    .builder()
                    .amgArtistId(amgArttistId)
                    .lastRequest(LocalDateTime.now())
                    .requestType(RequestTypeEnum.ALBUM)
                    .registeredRequestCounter(1)
                    .build()
            );
        } else {
            registerRequest(amgArttistId.toString());
        }
    }

    @Override
    public void registerRequest(String key) {
        RegisteredRequest registeredRequest = registeredRequestAfterLimitMap.get(key);
        registeredRequest.setLastRequest(LocalDateTime.now());
        registeredRequest.incrementRegisteredRequestCounter();
    }

    @Override
    public RegisteredRequest loadRegisteredRequest(String key) {
        return registeredRequestAfterLimitMap.get(key);
    }

    @Override
    public Map<String, RegisteredRequest> loadAllRegisteredRequest() {
        return registeredRequestAfterLimitMap;
    }

    @Override
    public RegisteredRequest removeRegisteredRequest(String key) {
        return registeredRequestAfterLimitMap.remove(key);
    }

}
