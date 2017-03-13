package info.rsdev.boombox.ui.alternate;

import info.rsdev.boombox.api.TagConstants;
import info.rsdev.boombox.domain.Song;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class SongDetailsPanel extends JPanel implements TagConstants {

    private static final long serialVersionUID = 1L;
    
    /**
     * The meta data from the music source in the order that we want them displayed. All other attributes (if any) are
     * dumped after these ones.
     */
    private static final List<String> PREDEFINED_ATTRIBUTES = Arrays.asList(ARTIST_KEY, SONG_TITLE_KEY, ALBUM_TITLE_KEY, 
            DISC_KEY, DISC_TOTAL_KEY, TRACK_KEY, TRACK_TOTAL_KEY, YEAR_KEY, SONG_RATING_KEY);
    
    private boolean isEditMode = false;
    
    public SongDetailsPanel() {
        initComponents();
    }
    
    public void setSong(Song newSong) {
        clearUI();
        if (newSong == null) {
            initNullUI();
        } else {
            initUIWithSong(newSong, isEditMode);
        }
    }
    
    private void initComponents() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initNullUI();
    }
    
    /**
     * If we are on a tabbed container, make sure that the tab that we are on is the selected tab. If not
     * on a tabbed container, do nothing. 
     */
    protected void selectThisTab() {
        Container parent = this;
        Component tabComponent = null;
        JTabbedPane tabContainer = null;
        while (parent.getParent() != null) {
            tabComponent = (Component)parent;
            parent = parent.getParent();
            if (parent instanceof JTabbedPane) {
                tabContainer = (JTabbedPane)parent;
                break;
            }
        }
        if (tabContainer != null) {
            tabContainer.setSelectedComponent(tabComponent);
            tabComponent.revalidate();
        }
    }
    
    public boolean getEditMode() {
        return this.isEditMode;
    }
    
    public void setEditMode(boolean newEditMode) {
        this.isEditMode = newEditMode;
    }
    
    private void initUIWithSong(Song newSong, boolean editMode) {
        Map<String, Object> properties = new HashMap<>(newSong.getProperties());
        for (String mandatoryAttribute: PREDEFINED_ATTRIBUTES) {
            add(new JLabel(mandatoryAttribute));
            Object value = properties.remove(mandatoryAttribute);
            JTextField valueField = new JTextField(value==null?null:value.toString());
            valueField.setEnabled(editMode);
            add(valueField);
        }
        
        //dump remaining attributes
        for (java.util.Map.Entry<String, Object> entry: properties.entrySet()) {
            add(new JLabel(entry.getKey()));
            JTextField valueField = new JTextField(entry.getValue().toString());
            valueField.setEnabled(editMode);
            add(valueField);
        }
        
        //finally: dump URL to song resource
        add(new JLabel("Resource URL"));
        add(new JLabel(newSong.getUri().toString())); //TODO: work with URL's in Song
    }
    
    private void initNullUI() {
        add(new JLabel("None"));
    }
    
    private void clearUI() {
        removeAll();
    }
    
}
