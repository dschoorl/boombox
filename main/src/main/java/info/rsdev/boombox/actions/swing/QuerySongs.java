package info.rsdev.boombox.actions.swing;

import info.rsdev.boombox.domain.FilesystemSource;
import info.rsdev.boombox.domain.Song;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

public class QuerySongs extends SwingWorker<List<Song>, Void> {

    private final FilesystemSource musicSource;
    
    private final QuerySongsCallback callback;
    
    public QuerySongs(final FilesystemSource musicSource, QuerySongsCallback callback) {
        this.musicSource = musicSource;
        this.callback = callback;
    }
    
    @Override
    protected List<Song> doInBackground() throws Exception {
        return musicSource.getAllSongs();
    }
    
    @Override
    protected void done() {
        List<Song> songs = null;
        try {
            songs = get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        callback.processSongs(songs);
    }
    
}
