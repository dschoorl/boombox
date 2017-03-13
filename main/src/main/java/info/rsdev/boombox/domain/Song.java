package info.rsdev.boombox.domain;

import info.rsdev.boombox.api.MusicSource;
import info.rsdev.boombox.api.TagConstants;

import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Song {
    
    private Long persistenceId = null;
    
    private MusicSource parentSource = null;
    
    private String relativeUrl = null;
    
    private Date lastPlayed = null;
    
    private Map<String, Object> properties = null;
    
    public Song(Long id, MusicSource parentSource, String relativeUrl) {
        this.persistenceId = id;
        this.relativeUrl = relativeUrl;
        this.parentSource = parentSource;
    }
    
    public Long getPersistenceId() {
        return this.persistenceId;
    }
    
    public URI getUri() {
        return parentSource.getUri(relativeUrl);
    }
    
    public String getRelativeUrl() {
        return this.relativeUrl;
    }
    
    public Date getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
    
    public Map<String, Object> getProperties() {
        if (this.properties == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(this.properties);
    }
    
    public String getProperty(String propertyName) {
        Object property = properties.get(propertyName);
        return property==null? null: property.toString();
    }
    
    public void setProperties(Map<String, Object> newProperties) {
        if (newProperties == null) {
            this.properties = null;
        } else {
            this.properties = new HashMap<>(newProperties);
        }
    }

    @Override
    public String toString() {
        return getProperty(TagConstants.SONG_TITLE_KEY);
    }

    public MusicSource getMusicSource() {
        return this.parentSource;
    }
    
}
