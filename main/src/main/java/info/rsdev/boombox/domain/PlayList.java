package info.rsdev.boombox.domain;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlayList {
    
    private SecureRandom randomGenerator = null;
    
    /**
     * Randomly select the next song to play. Has no effect when {@link #playSingle} is set to true
     */
    private boolean randomPlay = false;
    
    /**
     * Only play the current song, over and over again. Do we register each instance played in the history or not...? 
     * This option implies {@link #repeat} set to true.
     */
    private boolean playSingle = false;
    
    /**
     * When we have reached the end of the playlist, we start over again.
     */
    private boolean repeat = false;
    
    /**
     * The songs from the playlist that have been played
     */
    private LinkedList<Song> history = new LinkedList<>();
    
    /**
     * When we are playing from history (when calling {@link #previousSong()}), this index helps us keep track of the
     * history to playback. When this value is negative, we are not playing from history.
     */
    private int indexIntoHistory = -1;
    
    /**
     * The songs from the playlist that have not yet been played. Maybe split in nearFuture and distentFuture someday.
     */
    private List<Song> future = null;
    
    /**
     * All songs for this playlist in natural playing order
     */
    private final List<Song> songs;
    
    private String filterCriteria = null;
    
    /**
     * Index into {@link #songs} of the song currently being played. When playing has not yet been started, this value is
     * negative.
     */
    private int currentSongIndex = -1;
    
    public PlayList(List<Song> songs) {
        this.songs = new ArrayList<>(songs);
        resetFuture();
    }
    
    public Song nextSong() {
        if (songs == null) { return null; }
        
        //first handle single play modus and playing-from-history modus
        if (playSingle) {
            if (repeat) {
                if (!history.isEmpty()) {
                    return history.getLast();
                }
            } else {
                if (currentSongIndex >= 0) {
                    return null;
                }
            }
        } else if (indexIntoHistory >= 0) {
            //we are playing from history (previousSong() has been called one or more times)
            indexIntoHistory++;
            if (history.size() == indexIntoHistory) {
                indexIntoHistory = -1;  //we have finished playing last song from history
            } else {
                return history.get(indexIntoHistory);   //do not register in history again
            }
        }
        
        //When there is nothing left to play, stop serving next songs
        if (repeat) { 
            if (future.isEmpty()) {
                resetFuture();
            }
        } else {
            if (future.isEmpty()) {
                return null;
            }
        }

        //select next song either randomly or sequentially
        Song nextSong = null;
        if (!randomPlay) {
            currentSongIndex = 0;
            nextSong = future.get(currentSongIndex);   //when playing sequentially, always play the first song from the future
        } else {
            nextSong = future.get(randomGenerator.nextInt(future.size()));
            currentSongIndex = songs.indexOf(nextSong);
        }
        processSong(nextSong);
        return nextSong;
    }
    
    public Song previousSong() {
        if (history.isEmpty()) { return null; }
        
        //TODO: consider: should we stay in singlePlay, or is a call to previous implying that we wabt to break out of singleplay modus?
        if (playSingle && repeat) {
            System.out.println(String.format("playSingle=%b, repeat=%b", playSingle, repeat));
            return history.getLast();
        }
        
        if (indexIntoHistory < 0) {
            indexIntoHistory = Math.max(0, history.size() - 2); //current song is top of history, we want the previous song
        } else {
            indexIntoHistory = Math.max(0, --indexIntoHistory);
        }
        return history.get(indexIntoHistory);
    }
    
    public int setCurrentSong(Song song) {
        int index = songs.indexOf(song);
        if (index < 0) {
            index = 0;  //TODO: throw exception?
            song = songs.get(index);
        }
        processSong(song);
        currentSongIndex = index;
        return index;
    }
    
    /**
     * Add song to history and remove from future, so that it won't be selected randomly anymore
     * @param song
     */
    private void processSong(Song song) {
        if (song != null) {
            this.future.remove(song);
            if ((indexIntoHistory < 0) && (history.isEmpty() || !song.equals(history.getLast()))) {
                this.history.add(song);
            }
        }
    }
    
    public List<Song> applyFilter(String filterCriteria) {
        if (filterCriteria.trim().isEmpty()) {
            this.filterCriteria = null;
        } else {
            this.filterCriteria = filterCriteria.toLowerCase();
        }
        resetFuture();
        return Collections.unmodifiableList(future);
    }
    
    public void resetFuture() {
        if (filterCriteria != null) {
            String filterCriteria = this.filterCriteria;
            List<Song> filteredSongs = new ArrayList<>();
            for (Song candidate: songs) {
                if (candidate.getRelativeUrl().toLowerCase().contains(filterCriteria)) {
                    filteredSongs.add(candidate);
                }
            }
            future = filteredSongs;
        } else {
            future = new ArrayList<>(songs);
        }
//        byte[] seed = SecureRandom.getSeed(8);  //generating seed takes very long time
//        byte[] seed = new byte[] {12, -55, 127, 85, -44, 53, 66, -45 };   //hmm, a bit static...
//        randomGenerator = new SecureRandom(seed);
        randomGenerator = new SecureRandom();   //does it makes a difference when we create new instance or reuse same instance??
        //may do something more sophisticated in the future (like split in nearFuture and distentFuture based on last played time
    }

    public boolean isRandomPlay() {
        return this.randomPlay;
    }
    
    public void setRandomPlay(boolean enableRandomPlay) {
        this.randomPlay = enableRandomPlay;
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    public void setRepeat(boolean enableRepeat) {
        this.repeat = enableRepeat;
    }

    public boolean isSinglePlay() {
        return this.playSingle;
    }

    public void setSinglePlay(boolean enableSinglePlay) {
        this.playSingle = enableSinglePlay;
    }

    /**
     * Remove the given song from history. Most common case is that there was an error during playback (file corruption)
     */
    public boolean removeFromHistory(Song defectedSong) {
        if (!history.isEmpty()) {
            int index = history.indexOf(defectedSong);
            if ((index >= 0) && history.remove(defectedSong)) {
                if (indexIntoHistory >= 0) {
                    if (history.isEmpty()) {
                        indexIntoHistory = -1;
                    } else {
                        indexIntoHistory = Math.max(0, index - 1);
                    }
                }
                return true;
            }
        }
        return false;
    }
    
}
