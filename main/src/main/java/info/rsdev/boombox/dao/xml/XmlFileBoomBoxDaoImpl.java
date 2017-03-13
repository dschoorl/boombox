package info.rsdev.boombox.dao.xml;

import info.rsdev.boombox.BoomBox;
import info.rsdev.boombox.PrefKeys;
import info.rsdev.boombox.api.BoomBoxDao;
import info.rsdev.boombox.api.MusicSource;
import info.rsdev.boombox.domain.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is a simplistic implementation that reads an xml file at startup and shares instances of it's data (Songs etc.)
 * with the application. The data is stored back to filesystem, after a (configurable) number of changes or at shutdown
 * of the application.
 */
public class XmlFileBoomBoxDaoImpl implements BoomBoxDao {
    
    /**
     * We need to store songs per MusicSource
     */
    private Map<MusicSource, Map<Long, Song>> allSongs = null;
    
    private AtomicLong nextPersistenceId = null;    //TODO: one id that covers all musicsources?
    
    private boolean hasChanges = false;
    
    public XmlFileBoomBoxDaoImpl() {
        //TODO: do everything on a per-MusicSource basis...
        File pathToXmlFile = getFileLocation();
        this.allSongs = XmlPersistenceUtil.read(pathToXmlFile);
        Runtime.getRuntime().addShutdownHook(new XmlFileDaoShutdownHook(this)); //save on jvm shutdown
        //TODO: find largest Long used as persistenceId (per MusicSource?)
    }
    
    @Override
    public List<Song> getAllSongs() {
        //first determine the size of all songs to limit internal copy operations in result list
        int count = 0;
        for (Map<Long, Song> collection: this.allSongs.values()) {
            count += collection.size();
        }
        List<Song> allSongs = new ArrayList<>(count);
        for (Map<Long, Song> collection: this.allSongs.values()) {
            allSongs.addAll(collection.values());
        }
        return allSongs;
    }
    
    @Override
    public void update(Song song) {
        
        if (song == null) {return; }
        
        this.hasChanges = true;
        MusicSource source = song.getMusicSource();
        Map<Long, Song> songsBySource = this.allSongs.get(source);
        if (songsBySource == null) {
            songsBySource = new ConcurrentHashMap<>();
            this.allSongs.put(source, songsBySource);
        }
        if (song != null) {
            //TODO: insert or update the thingy in the collection
        }
    }
    
    public void remove(Song song) {
        
        if (song == null) {return; }
        
        this.hasChanges = true;
        //TODO: remove the thingy from the collection
    }
    
    private File getFileLocation() {
        //Use location relative to datadir
        File dataDir = new File(System.getProperty(PrefKeys.DATADIR_KEY));
        return new File(new File(dataDir, "persistence"), "catalog.xml.gz");
    }
    
    /**
     * Use JVM shutdownhook to save all changes only once: at JVM shutdown.
     */
    private static class XmlFileDaoShutdownHook extends Thread {
        
        private final XmlFileBoomBoxDaoImpl daoInstance;
        
        private XmlFileDaoShutdownHook(XmlFileBoomBoxDaoImpl daoInstance) {
            this.daoInstance = daoInstance;
        }
        
        @Override
        public void run() {
            if (daoInstance.hasChanges) {
                File targetFile = daoInstance.getFileLocation();
                XmlPersistenceUtil.write(daoInstance.allSongs, targetFile);
                daoInstance.hasChanges = false;
            }
        }
    }
    
}
