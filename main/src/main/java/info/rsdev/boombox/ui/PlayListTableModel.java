
package info.rsdev.boombox.ui;

import info.rsdev.boombox.domain.PlayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class PlayListTableModel implements TableModel {
    
    private PlayList playList = null;
    
    private List<String> songAttributesAsColumns = new ArrayList<>();
    
    public PlayListTableModel() {
        songAttributesAsColumns.addAll(Arrays.asList(""));
    }
    
    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public int getColumnCount() {
        return songAttributesAsColumns.size();
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return songAttributesAsColumns.get(columnIndex);
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void removeTableModelListener(TableModelListener l) {
        // TODO Auto-generated method stub
        
    }
    
}
