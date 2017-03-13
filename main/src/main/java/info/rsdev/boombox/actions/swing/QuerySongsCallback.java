package info.rsdev.boombox.actions.swing;

import info.rsdev.boombox.domain.Song;

import java.util.List;

public interface QuerySongsCallback {
    public void processSongs(List<Song> songs);
}
