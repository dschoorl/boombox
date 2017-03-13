package info.rsdev.boombox.ui.alternate;

import info.rsdev.boombox.domain.Song;
import info.rsdev.boombox.domain.events.ISongSelectionListener;

public class SelectedDetailsPanel extends SongDetailsPanel implements ISongSelectionListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void onSongSelected(Song newSelection) {
        setSong(newSelection);
        selectThisTab();
    }
    
}
