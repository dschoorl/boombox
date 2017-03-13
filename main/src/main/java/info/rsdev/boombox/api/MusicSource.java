package info.rsdev.boombox.api;

import java.net.URI;

/**
 * A MusicSource provides a Facade to some source of music, be it a folder on the filesystem, a
 * network drive, a streaming music server, whatever. The interface allows you to search the source,
 * and obtain the music to play (as a binary stream)
 */
public interface MusicSource {
    
    public URI getBaseUri();
    
    public URI getUri(String relativePath);
    
}
