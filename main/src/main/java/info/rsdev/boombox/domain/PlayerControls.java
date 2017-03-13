package info.rsdev.boombox.domain;

import info.rsdev.boombox.domain.MusicPlayer.MusicPlayerReturnStates;
import info.rsdev.boombox.domain.events.IPlayListener;
import info.rsdev.boombox.domain.events.ISongSelectionListener;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * This class is responsible for playing audio streams in it's own thread. Currently only playing of files is
 * supported. later on, this should probably be changed to binary streams.
 */
public class PlayerControls {
    
    private static final Logger logger = Logger.getLogger(MusicPlayer.class.getName());
    
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    private static final LinkedList<IPlayListener> songPlayListeners = new LinkedList<>();
    
    private Future<?> currentSongPlaying = null;
    
    private PlayList playList = null;
    
    public void play() {
        Song song = playList.nextSong();
        play(song);
    }
    
    public void next() {
        Song song = playList.nextSong();
        play(song);
    }
    
    public void previous() {
        Song song = playList.previousSong();
        play(song);
    }
    
    public synchronized void play(Song song) {
        if (this.currentSongPlaying != null) { stop(); }
        if (song != null) {
            playList.setCurrentSong(song);
            song.setLastPlayed(new Date());
            currentSongPlaying = executorService.submit(new MusicPlayer(song, this));
            notifySongPlaybackChanged(song);
        } else {
            if (this.currentSongPlaying == null) {
                notifyPlaybackEnded();
            }
        }
    }
    
    public synchronized void stop() {
        if (this.currentSongPlaying != null) {
            if (!this.currentSongPlaying.cancel(true)) {
                logger.info("Current playing song could not be stopped for unknown reasons");
            }
            this.currentSongPlaying = null;
        }
    }
    
    //TODO: support forward and rewind

    public void pause() {
        
    }
    
    public void resume() {
        
    }
    
    public void onFinished(Song finishedSong, MusicPlayerReturnStates state) {
        switch (state) {
            case CANCELLED: 
                if (this.currentSongPlaying == null) {
                    notifyPlaybackEnded();
                }
                break;
            case ERROR:
                //remove current song from playlist history...
                playList.removeFromHistory(finishedSong);
                //No break: continue with SUCCESS code to select next song playing (if any)
            case SUCCESS:
                Song nextSong = playList.nextSong();
                if (nextSong != null) {
                    play(nextSong);
                } else {
                    notifyPlaybackEnded();
                }
                break;
        }
    }
    
    public void addPlayListener(IPlayListener listener) {
        if (listener == null) {
            throw new NullPointerException(String.format("%s cannot be null", IPlayListener.class.getSimpleName()));
        }
        if (!songPlayListeners.contains(listener)) {
            songPlayListeners.add(listener);
        }
    }
    
    public void removeSongSelectionListener(ISongSelectionListener listener) {
        songPlayListeners.remove(listener);
    }
    
    private void notifySongPlaybackChanged(Song newSong) {
        for (IPlayListener listener: songPlayListeners) {
            listener.onSongPlaybackStarted(newSong);
        }
    }
    
    /**
     * Signal that music has stopped playing
     */
    private void notifyPlaybackEnded() {
        for (IPlayListener listener: songPlayListeners) {
            listener.onPlaybackEnded();
        }
    }

    public void setPlayList(PlayList newPlayList) {
        this.playList = newPlayList;
    }
    
    public PlayList getPlayList() {
        return this.playList;
    }
    
    public boolean isRandomPlay() {
        if (playList == null) { return false; }
        return playList.isRandomPlay();
    }
    
    public void setRandomPlay(boolean enableRandomPlay) {
        if (this.playList != null) {
            this.playList.setRandomPlay(enableRandomPlay);
        }
    }

    public boolean isRepeat() {
        if (playList == null) { return false; }
        return playList.isRepeat();
    }
    
    public void setRepeat(boolean enableRepeat) {
        if (this.playList != null) {
            this.playList.setRepeat(enableRepeat);
        }
    }

    public boolean isSinglePlay() {
        if (playList == null) { return false; }
        return playList.isSinglePlay();
    }
    
    public void setSinglePlay(boolean enableSinglePlay) {
        if (playList != null) {
            this.playList.setSinglePlay(enableSinglePlay);
        }
    }
}
