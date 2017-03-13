package info.rsdev.boombox.api;

import info.rsdev.boombox.domain.Factory;
import info.rsdev.boombox.domain.Factory.Stat;

import java.util.LinkedList;
import java.util.logging.Logger;

public interface TagConstants {
    
    public static final Logger logger = Logger.getLogger(Factory.class.getName());
    public static final String ARTIST_KEY = "performingArtist";
    public static final String SONG_TITLE_KEY = "title";
    public static final String ALBUM_TITLE_KEY = "album";
    public static final String DISC_KEY = "discNr";
    public static final String DISC_TOTAL_KEY = "discTotal";
    public static final String TRACK_KEY = "trackNr";
    public static final String TRACK_TOTAL_KEY = "trackTotal";
    public static final String YEAR_KEY = "year";
    public static final String TIME_IN_SECONDS_KEY = "timeInSeconds";
    public static final String LAST_PLAYED_KEY = "lastPlayed";
    public static final String PLAYCOUNT_KEY = "playCount";
    public static final String SONG_RATING_KEY = "songRating";
    public static final String VARIABLE_BITRATE_KEY = "vbr";
    public static final String BITRATE_KEY = "bitrate";
    public static final String ENCODING_NAME_KEY = "encodingName";
    public static final String COMMON_EXTENSION_NAME_KEY = "extensionName";
    public static final String CHANNEL_KEY = "channels";
    public static final String SAMPLE_RATE_KEY = "sampleRate"; //in Hz
    public static final String SAMPLE_SIZE_KEY = "sampleSize"; //in Bits
    public static final String STREAM_BYTESIZE_KEY = "streamBytes";
    
}