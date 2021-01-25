# topAlbums

Application/web service runs on localhost:8080
There are two end-points:
* localhost:8080/artist/{artistName}
* localhost:8080/album/{amgArtistId}
First returns all Artists by term {artistName}, list size by default 50.
Second returns all Albums by Artist AMG ID (All Music Guide ID), list size by default 5.

Application requirements: 
* 100 requests limit per hour (when may be millions of requests).

About application logic

Both Artist and Album end-point has the same logic, which consists of three cases. HashMap is used for data storage (let's call it local-DB).
1. If requested Artist/Album exists in local-DB, it is retrieve from this local-DB, no request made to iTunes service.
1. If such Artist/Album data not exists in local-DB and request limit is already exceeded, then this request is registered for later execution (in local-DB, another "table"). null is returned at this point.
3. If such Artist/Album data not exists in local-DB, but request limit is not exceeded, iTunes service is called. Response data is save in local-DB. 1 of 100 request has already been used.

To avoid gloggin up memory, some jobs are used:
1. To reset counters. Counters are used to count requests to local-DB, to iTunes service, and to check when limit is exceeded.
2. To remove old data from local-DB. Old data - if last request was 2 days ago and later (it is no more interested).
3. If request limit not exceeded, some seconds before counters reset, registered requests are executed till counters will be reseted. If no more registered requests, cached response data will be updated.
4. Every 20 minutes some registered requests are executed. "Some" means 5 registered requests ordered by theirs biggest counters, i.e. most frequently called requests. Every 20 minutes some cached data from local-DB are updated. "Some" means 5 response data ordered by theirs biggest counters and last update must be older than 24 h.
