package info.rsdev.boombox.dao.xml;

import info.rsdev.boombox.api.MusicSource;
import info.rsdev.boombox.domain.Song;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * We have very limited needs for reading / writing xml to file. To limit the number of dependecies to 
 * external, xml-handling frameworks, we do all the low level work ourselves.
 */
public abstract class XmlPersistenceUtil {
    
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    private static final String BOOMBOX_XML_STRUCTURE_VERSION = "1.0";
    
    private static final String ROOT_ELEMENT = "BoomBox";
    private static final String STRUCTURE_VERSION_ELEMENT = "XmlVersion";
    private static final String MUSIC_SOURCE_ELEMENT = "MusicSource";
    private static final String BASE_URI_ELEMENT = "BaseUri";
    
    private static final String SONG_ELEMENT = "Song";
    private static final String PERSISTENCE_ID_ELEMENT = "Id";
    private static final String PATH_ELEMENT = "RelativePath";  //What could this be for Spotify / other network protocols, such as daap, mms etc.?
    private static final String LAST_PLAYED_ELEMENT = "LastPlayed";
    private static final String PROPERTY_ELEMENT = "Property";
    private static final String NAME_ATTRIBUTE = "Name";
    private static final String VALUE_ATTRIBUTE = "Value";
    
    /**
     * Do not instantiate this utility class
     */
    private XmlPersistenceUtil() {}
    
    public static final void write(Map<MusicSource, Map<Long, Song>> collections, File outFile) {
        if (outFile.isFile()) {
            //TODO: Create backup (remove any previous backups)
        }
        
        // Optimization usage of storage space, with limited increased code complexity by using file compression (Gzip).
        for (MusicSource musicSource: collections.keySet()) {
            Map<Long, Song> songsBySource = collections.get(musicSource);
            for (Song song: songsBySource.values()) {
                //TODO: dump all songs to file
            }
        }
    }
    
    public static final Map<MusicSource, Map<Long, Song>> read(File inFile) {
        Map<MusicSource, Map<Long, Song>> allSongs = new ConcurrentHashMap<>();
        if (inFile.isFile()) {
            
        }
        return allSongs;
    }
}
