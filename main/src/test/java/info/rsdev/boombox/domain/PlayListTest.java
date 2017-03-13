package info.rsdev.boombox.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import info.rsdev.boombox.api.MusicSource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class PlayListTest {
    
    @Test
    public void randomPlayIsOffByDefault() {
        PlayList playList = new PlayList(getTestSongs());
        assertFalse(playList.isRandomPlay());
    }
    
    @Test
    public void randomPlayCanBeSwitchedOnAndOffAtWill() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setRandomPlay(true);
        assertTrue(playList.isRandomPlay());
        playList.setRandomPlay(false);
        assertFalse(playList.isRandomPlay());
    }
    
    @Test
    public void repeatIsOffByDefault() {
        PlayList playList = new PlayList(getTestSongs());
        assertFalse(playList.isRepeat());
    }
    
    @Test
    public void repeatCanBeSwitchedOnAndOffAtWill() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setRepeat(true);
        assertTrue(playList.isRepeat());
        playList.setRepeat(false);
        assertFalse(playList.isRepeat());
    }
    
    @Test
    public void singlePlayIsOffByDefault() {
        PlayList playList = new PlayList(getTestSongs());
        assertFalse(playList.isSinglePlay());
    }
    
    @Test
    public void singlePlayCanBeSwitchedOnAndOffAtWill() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setSinglePlay(true);
        assertTrue(playList.isSinglePlay());
        playList.setSinglePlay(false);
        assertFalse(playList.isSinglePlay());
    }
    
    @Test
    public void testNextSongPlayingLineair() {
        PlayList playList = new PlayList(getTestSongs());
        Song currentSong = playList.nextSong();
        assertEquals("A", new File(currentSong.getUri()).getName());
        assertEquals(0, playList.setCurrentSong(currentSong));
        currentSong = playList.nextSong();
        assertEquals("B", new File(currentSong.getUri()).getName());
        assertEquals(1, playList.setCurrentSong(currentSong));
        currentSong = playList.nextSong();
        assertEquals("C", new File(currentSong.getUri()).getName());
        assertEquals(2, playList.setCurrentSong(currentSong));
        assertNull(playList.nextSong());
    }
    
    @Test
    public void testNextSongWithSinglePlay() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setSinglePlay(true);
        assertEquals("A", new File(playList.nextSong().getUri()).getName());
        assertNull(playList.nextSong());
    }
    
    @Test
    public void testNextSongWithSinglePlayAndRepeat() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setSinglePlay(true);
        playList.setRepeat(true);
        assertEquals("A", new File(playList.nextSong().getUri()).getName());
        assertEquals("A", new File(playList.nextSong().getUri()).getName());
        assertEquals("A", new File(playList.nextSong().getUri()).getName());
        //no matter how often, but it keeps playing the same single song
        
        playList.setSinglePlay(false);
        assertEquals("B", new File(playList.nextSong().getUri()).getName());
    }
    
    @Test
    public void testNextSongWithSinglePlayAndRandom() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setSinglePlay(true);
        playList.setRandomPlay(true);
        assertNotNull(playList.nextSong()); //it is random play, so we don't know which one is selected
        assertNull(playList.nextSong());
    }
    
    @Test
    public void testNextSongWithSinglePlayRandomAndRepeat() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setSinglePlay(true);
        playList.setRandomPlay(true);
        playList.setRepeat(true);
        Song firstChoice = playList.nextSong(); //it is random play, so we don't know which one is selected
        assertNotNull(firstChoice);
        assertEquals(firstChoice, playList.nextSong());
        assertEquals(firstChoice, playList.nextSong());
        assertEquals(firstChoice, playList.nextSong());
        assertEquals(firstChoice, playList.nextSong());
        //no matter how often, but it keeps playing the same single song
        
        playList.setSinglePlay(false);
        assertNotEquals(firstChoice, playList.nextSong());
    }
    
    @Test
    public void testNextSongWithRandomAndRepeat() {
        PlayList playList = new PlayList(getTestSongs());
        playList.setRandomPlay(true);
        playList.setRepeat(true);
        
        //we know that the playlist has only three songs...
        Set<Song> firstRound = new HashSet<>(3);
        assertTrue(firstRound.add(playList.nextSong()));
        assertTrue(firstRound.add(playList.nextSong()));
        assertTrue(firstRound.add(playList.nextSong()));
        assertEquals(3, firstRound.size()); //three different songs have been played
        
        Set<Song> secondRound = new HashSet<>(3);
        assertTrue(secondRound.add(playList.nextSong()));
        assertTrue(secondRound.add(playList.nextSong()));
        assertTrue(secondRound.add(playList.nextSong()));
        assertEquals(3, secondRound.size()); //three different songs have been played
        
        assertEquals(firstRound, secondRound);  //both sets contain the same songs
    }
    
    @Test
    public void testPreviousSongPlayingLineair() {
        PlayList playList = new PlayList(getTestSongs());
        Song currentSong = playList.nextSong();
        assertEquals("A", new File(currentSong.getUri()).getName());
        assertEquals(0, playList.setCurrentSong(currentSong));
        currentSong = playList.nextSong();
        assertEquals("B", new File(currentSong.getUri()).getName());   //B is now the current song, so A is the previous song
        assertEquals(1, playList.setCurrentSong(currentSong));
        
        currentSong = playList.previousSong();
        assertEquals("A", new File(currentSong.getUri()).getName());
        assertEquals(0, playList.setCurrentSong(currentSong));    }
    
    @Test
    public void thereIsNoPreviousSongWhenNotPlayingYet() {
        PlayList playList = new PlayList(getTestSongs());
        assertNull(playList.previousSong());
    }
    
    @Test
    public void thePreviousSongIsCurrentSongWhenOnlyOneSongIsPlayed() {
        PlayList playList = new PlayList(getTestSongs());
        assertEquals("A", new File(playList.nextSong().getUri()).getName());
        assertEquals("A", new File(playList.previousSong().getUri()).getName());
        assertEquals("A", new File(playList.previousSong().getUri()).getName());
        assertEquals("A", new File(playList.previousSong().getUri()).getName());
    }
    
    /**
     * Get a well known list of fictive {@link Song songs}
     * @return a list of three fictive songs, id's 1 to 3 and file names A, B and C respectively
     */
    private List<Song> getTestSongs() {
        MusicSource musicSource = new FilesystemSource(new File("."));
        List<Song> songs = new ArrayList<>(3);
        songs.add(new Song(1L, musicSource, "A"));
        songs.add(new Song(2L, musicSource, "B"));
        songs.add(new Song(3L, musicSource, "C"));
        return songs;
    }
    
}
