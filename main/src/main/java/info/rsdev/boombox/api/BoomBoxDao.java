package info.rsdev.boombox.api;

import info.rsdev.boombox.domain.Song;

import java.util.List;

/**
 * This interface describes the persistence operations that are defined in the application.
 */
public interface BoomBoxDao {
    
    public List<Song> getAllSongs();
    
    public void update(Song song);
    
}
