package info.rsdev.boombox.ui.alternate;

import info.rsdev.boombox.domain.Song;
import info.rsdev.boombox.domain.events.IPlayListener;

public class PlayingDetailsPanel extends SongDetailsPanel implements IPlayListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void onSongPlaybackStarted(Song songStarted) {
        setSong(songStarted); 
        selectThisTab();
    }

    @Override
    public void onPlaybackEnded() {
        setSong(null); 
        selectThisTab();
    }
    
}
