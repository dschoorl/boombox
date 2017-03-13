package info.rsdev.boombox.domain;

import info.rsdev.boombox.api.MusicSource;
import info.rsdev.boombox.api.TagConstants;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public final class Factory implements TagConstants {
    
    private static AtomicInteger counter = new AtomicInteger(1);
    
    //TODO: remove - this is just temp stuff during development phase
    public static final LinkedList<Stat> stats = new LinkedList<>();
    
    private Factory() {}
    
    public static final class Stat {
        //This class is not threadsafe!
        public String type = null;
        public long count = 0;
        public long min = Long.MAX_VALUE;
        public long max = 0;
        public long duration = 0;
        public void setDuration(long duration) {
            count++;
            this.duration += duration;
            if (duration<min) {
                min = duration;
            }
            if (duration>max) {
                max = duration;
            }
        }
        @Override
        public String toString() {
            return String.format("[type=%s, count=%d, totalSecs=%d, avgMillies=%d, minMillies=%d, maxMillies=%d]", type, count, (duration / 1000), (duration / count), min, max);
        }
    }
    
    public static Song newSong(MusicSource parentSource, String relativePath) {
//        String relativePath = parentSource.
        Song song = new Song(Long.valueOf(counter.getAndIncrement()), parentSource, relativePath);
        
        long start = System.currentTimeMillis();
        Map<String, Object> properties = new HashMap<>();
        //Add tags -- mpc is not supported
        String encodingType = null;
        File musicFile = new File(parentSource.getUri(relativePath));
        try {
            AudioFile audioFile = AudioFileIO.read(musicFile);
            AudioHeader ah = audioFile.getAudioHeader();
            properties.put(BITRATE_KEY, ah.getBitRate());
            properties.put(VARIABLE_BITRATE_KEY, ah.isVariableBitRate());
            properties.put(TIME_IN_SECONDS_KEY, ah.getTrackLength());
            encodingType = ah.getEncodingType();
            
            Tag tags = audioFile.getTag();
            if (tags != null) {
    //            Iterator<TagField> tagIterator = tags.getFields();
    //            while (tagIterator.hasNext()) {
    //                TagField tagField = tagIterator.next();
    //                String id = tagField.getId();
    //                tagField.getRawContent();
    //            }
                
                properties.put(ARTIST_KEY, tags.getFirst(FieldKey.ARTIST));
                properties.put(SONG_TITLE_KEY, tags.getFirst(FieldKey.TITLE));
                properties.put(ALBUM_TITLE_KEY, tags.getFirst(FieldKey.ALBUM));
                properties.put(DISC_KEY, tags.getFirst(FieldKey.DISC_NO));
                properties.put(DISC_TOTAL_KEY, tags.getFirst(FieldKey.DISC_TOTAL));
                properties.put(TRACK_KEY, tags.getFirst(FieldKey.TRACK));
                properties.put(TRACK_TOTAL_KEY, tags.getFirst(FieldKey.TRACK_TOTAL));
                properties.put(YEAR_KEY, tags.getFirst(FieldKey.YEAR));
                properties.put(SONG_RATING_KEY, tags.getFirst(FieldKey.RATING));
            }
//            properties.put(GENRE_KEY, tags.get(FieldKey.GENRE));  //could be multiple
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
            logger.log(Level.SEVERE, String.format("Error reading tags from %s", musicFile), e);
        }
        
        long duration = System.currentTimeMillis() - start;
        Stat stat = getStat(encodingType);
        stat.setDuration(duration);
        
        song.setProperties(properties);
        return song;
    }
    
//    private static Stat getStat(File file) {
//        String filename = file.getName();
//        int dotIndex = filename.lastIndexOf('.');
//        String extension = null;
//        if ((dotIndex >= 0) && (dotIndex < filename.length())) {
//            extension = filename.substring(dotIndex + 1);
//        }
//        return getStat(extension);
//    }
    
    private static Stat getStat(String extension) {
        for (Stat stat: stats) {
            if (((extension == null) && (stat.type == null)) ||
                    ((extension != null) && extension.equals(stat.type))) {
                return stat;
            };
        }
        
        //there is not already a Stat for this extension
        Stat stat = new Stat();
        stat.type = extension;
        stats.add(stat);
        return stat;
    }

}
