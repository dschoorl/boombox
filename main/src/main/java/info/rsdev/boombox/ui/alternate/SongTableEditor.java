package info.rsdev.boombox.ui.alternate;

import info.rsdev.boombox.domain.PlayerControls;
import info.rsdev.boombox.domain.Song;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * The purpose of this class is to support song playback when a row is double clicked. It's immutable.
 */
public class SongTableEditor extends AbstractCellEditor implements TableCellEditor {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * An integer specifying the number of clicks needed to start editing.
     * Even if <code>clickCountToStart</code> is defined as zero, it
     * will not initiate until a click occurs.
     */
    protected final int clickCountToStart;
    
    private final PlayerControls playerControls;
    
    public SongTableEditor(int clickCountToStart, PlayerControls playerControls) {
        this.clickCountToStart = clickCountToStart;
        this.playerControls = playerControls;
    }
    
    @Override
    public Object getCellEditorValue() {
        return null;    //there is no editor, because direct table editing is not supported in a table of songs
    }
    
    @Override
    public boolean isCellEditable(EventObject anEvent) {
        if (anEvent instanceof MouseEvent) {
            return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;
        }
        return true;
    }
    
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        Song selectedSong = (Song)table.getModel().getValueAt(table.convertRowIndexToModel(row), 1);
        playerControls.play(selectedSong);
        return null;    //there is no editor component, because direct table editing is not supported in a table of songs
    }
    
    /**
     * Returns the number of clicks needed to start editing.
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }

}
