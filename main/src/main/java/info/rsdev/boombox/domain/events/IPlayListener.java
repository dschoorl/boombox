package info.rsdev.boombox.domain.events;

import info.rsdev.boombox.domain.Song;

public interface IPlayListener {
    
    /**
     * Called when the givenSong has started to play
     * @param givenSong the song that has started to play
     */
    public void onSongPlaybackStarted(Song givenSong);
    
    /**
     * Called when playback is cancelled and no songs are currently playing
     */
    public void onPlaybackEnded();
    
}
