package info.rsdev.boombox.domain.events;

import info.rsdev.boombox.domain.Song;

public interface ISongSelectionListener {
    
    public void onSongSelected(Song newSelection);
    
}
