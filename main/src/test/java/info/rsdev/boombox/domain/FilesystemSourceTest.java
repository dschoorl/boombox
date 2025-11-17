package info.rsdev.boombox.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class FilesystemSourceTest {
    
    @Test @Disabled
    //TODO: this test depends on machine dependent file system content. Remove or change before committing
    public void testGetAllSongs() {
        FilesystemSource source = new FilesystemSource(new File("/home/dschoorl/extern/Muziek"));
        List<Song> songs = source.getAllSongs();
        System.out.println(String.format("Count=%d", songs.size()));
        assertTrue(songs.size() > 10000);
    }
    
}
