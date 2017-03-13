package info.rsdev.boombox.domain;

import info.rsdev.boombox.api.MusicSource;
import info.rsdev.boombox.util.Utils;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class FilesystemSource implements MusicSource {
    
    public static final Logger logger = Logger.getLogger(FilesystemSource.class.getName());
    
    /**
     * TODO: .ape, .mpc and .wma codec is not yet supported
     */
//    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("mp3", "ogg", "flac", "ape", "wma", "mpc");
    private static final List<String> SUPPORTED_EXTENSIONS = Arrays.asList("mp3", "ogg", "flac");
    
    private File musicDirectory = null;
    
    private static final FileFilter musicFiles = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            // TODO: read magic bytes of file
            if (pathname.isFile()) {
                String filename = pathname.getName();
                int dotIndex = filename.lastIndexOf('.');
                if (dotIndex > 0) {
                    String extension = pathname.getName().substring(dotIndex + 1);
                    return SUPPORTED_EXTENSIONS.contains(extension.toLowerCase());
                }
            }
            return false;
        }
    };
    
    private static final FileFilter subDirectories = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    };
    
    public FilesystemSource(File musicDirectory) {
        if (musicDirectory == null) {
            throw new NullPointerException("Music directory must be provided");
        }
        this.musicDirectory = musicDirectory;
    }
    
    public List<Song> getAllSongs() {
        Song[] songs = new Song[0];
        File[] directories = new File[] { musicDirectory };
        
        int dirNameLength = this.musicDirectory.toString().length();
        
        int indexIntoDirectories = 0;
        while (directories.length > indexIntoDirectories) {
            directories = Utils.concat(directories, directories[indexIntoDirectories].listFiles(subDirectories));
            File[] musicFilesInDirectory = directories[indexIntoDirectories].listFiles(musicFiles);
            Song[] songsInDirectory = null;
            if (musicFilesInDirectory != null) {
                songsInDirectory = new Song[musicFilesInDirectory.length];
                for (int i=0; i<musicFilesInDirectory.length; i++) {
                    String relativePath = musicFilesInDirectory[i].toString().substring(dirNameLength + 1);
                    songsInDirectory[i] = Factory.newSong(this, relativePath);
                }
            }
            songs = Utils.concat(songs, songsInDirectory);
            indexIntoDirectories++;
        }
        
        logger.info(String.format("stats:%n%s", Factory.stats));
        return Arrays.asList(songs);
    }

    @Override
    public URI getUri(String relativePath) {
        return new File(musicDirectory, relativePath).toURI();
    }

    @Override
    public URI getBaseUri() {
        return this.musicDirectory.toURI();
    }
    
}
