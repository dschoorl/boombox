/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package info.rsdev.boombox.ui.alternate;

import info.rsdev.boombox.actions.swing.QuerySongs;
import info.rsdev.boombox.actions.swing.QuerySongsCallback;
import info.rsdev.boombox.api.TagConstants;
import info.rsdev.boombox.domain.FilesystemSource;
import info.rsdev.boombox.domain.PlayList;
import info.rsdev.boombox.domain.PlayerControls;
import info.rsdev.boombox.domain.Song;
import info.rsdev.boombox.domain.events.ISongSelectionListener;
import info.rsdev.boombox.util.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;


/**
 * This UI displays player controls as buttons with text. It has a split pain. The left side is filled with the {@link Song}s
 * of the 
 *
 * @author dschoorl
 */
public class AlternateGUI extends javax.swing.JFrame {

    public static final Logger logger = Logger.getLogger(AlternateGUI.class.getName());
    
    private static final long serialVersionUID = 1L;

    private static final String[] COLUMN_TITLES = new String [] {
        "Nr", "Title", "Artist", "Rating", "Time", "Year", "Album"
    };

    private FilesystemSource musicSource = null;
    
    private final PlayerControls playerControls = new PlayerControls();
    
    private final SongTableEditor doubleClickToPlayTrigger;
    
    private final LinkedList<ISongSelectionListener> songSelectionListeners = new LinkedList<>();
    
    private Song currentSongSelected = null;
    
    private SelectedDetailsPanel selectedDetailsPanel = null;
    
    private PlayingDetailsPanel playingdetailsPanel = null;
    
    /**
     * Creates new form SpartanGUI
     */
    public AlternateGUI(FilesystemSource musicSource) {
        this.musicSource = musicSource;
        initComponents();
        
        //do some additional GUI stuff
        doubleClickToPlayTrigger = new SongTableEditor(2,  playerControls);
        songsListing.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    ListSelectionModel selectionModel = (ListSelectionModel) e.getSource();
                    int selectedTableRow = selectionModel.getMinSelectionIndex();    //assumption: single selection is set
                    Song selectedSong = (Song)songsListing.getValueAt(selectedTableRow, 1);
                    if ((selectedSong != null) && !selectedSong.equals(currentSongSelected)) {
                        notifySongSelectionChanged(selectedSong);
                    }
                }
            }
        });
        
        selectedDetailsPanel = new SelectedDetailsPanel();
        addSongSelectionListener(selectedDetailsPanel);
        selectedScrollPane.setViewportView(selectedDetailsPanel);
        selectedScrollPane.validate();
        
        playingdetailsPanel = new PlayingDetailsPanel();
        playerControls.addPlayListener(playingdetailsPanel);
        playingScrollpane.setViewportView(playingdetailsPanel);
        playingScrollpane.validate();
        
        //obtain music files and set them on the table (run in separate thread)
        collectAndDisplaySongs();
        
    }

    private void collectAndDisplaySongs() {
        SwingWorker<List<Song>, Void> songCollector = new QuerySongs(musicSource, new QuerySongsCallback() {
            @Override
            public void processSongs(List<Song> songs) {
                setTableModel(songs);
                playerControls.setPlayList(new PlayList(songs));
            }
        });
        songCollector.execute();
    }
    
    public void addSongSelectionListener(ISongSelectionListener listener) {
        if (listener == null) {
            throw new NullPointerException(String.format("%s cannot be null", ISongSelectionListener.class.getSimpleName()));
        }
        if (!songSelectionListeners.contains(listener)) {
            songSelectionListeners.add(listener);
        }
    }
    
    public void removeSongSelectionListener(ISongSelectionListener listener) {
        songSelectionListeners.remove(listener);
    }
    
    private void notifySongSelectionChanged(Song newSelection) {
        for (ISongSelectionListener listener: songSelectionListeners) {
            listener.onSongSelected(newSelection);
        }
    }
    
    private void setTableModel(List<Song> songs) {
        Object[][] content = new Object[songs.size()][7];
        int rowNumber = 0;
        for (Song song: songs) {
            String artist = song.getProperty(TagConstants.ARTIST_KEY);
            String album = song.getProperty(TagConstants.ALBUM_TITLE_KEY);
            String year = song.getProperty(TagConstants.YEAR_KEY);
            String rating = song.getProperty(TagConstants.SONG_RATING_KEY);
            String time = Utils.sec2min(song.getProperty(TagConstants.TIME_IN_SECONDS_KEY));
            content[rowNumber] = new Object[] {song.getPersistenceId(), song, artist, rating, time, year, album};
            rowNumber++;
        }
        songsListing.setModel(new DefaultTableModel(content, COLUMN_TITLES));
        setPlayerControlsEnabled(!songs.isEmpty());
    }
    
    private void setPlayerControlsEnabled(boolean enableControls) {
        this.playButton.setEnabled(enableControls);
        this.stopButton.setEnabled(enableControls);
        this.nextButton.setEnabled(enableControls);
        this.previousButton.setEnabled(enableControls);
        this.randomToggler.setSelected(this.playerControls.isRandomPlay());
        this.randomToggler.setEnabled(enableControls);
        this.repeatToggler.setSelected(this.playerControls.isRepeat());
        this.repeatToggler.setEnabled(enableControls);
        this.singleToggler.setSelected(this.playerControls.isSinglePlay());
        this.singleToggler.setEnabled(enableControls);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterPanel = new javax.swing.JPanel();
        filterLabel = new javax.swing.JLabel();
        filterField = new javax.swing.JTextField();
        playerControlsUI = new javax.swing.JPanel();
        playButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        previousButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        randomToggler = new javax.swing.JToggleButton();
        repeatToggler = new javax.swing.JToggleButton();
        singleToggler = new javax.swing.JToggleButton();
        mainSplitter = new javax.swing.JSplitPane();
        libraryContent = new javax.swing.JScrollPane();
        songsListing = new javax.swing.JTable() {
            private static final long serialVersionUID = 1L;
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                //No cell editing is supported, instead, play the track that is doubleclicked on
                return doubleClickToPlayTrigger;
            }
        };
        detailTabs = new javax.swing.JTabbedPane();
        playingScrollpane = new javax.swing.JScrollPane();
        selectedScrollPane = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("info/rsdev/boombox/ui/alternate/Bundle"); // NOI18N
        filterLabel.setText(bundle.getString("AlternateGUI.filterLabel.text")); // NOI18N

        filterField.setText(bundle.getString("AlternateGUI.filterField.text")); // NOI18N
        filterField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyFilter(evt);
            }
        });

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addComponent(filterLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterField))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(filterField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(filterLabel))
        );

        playButton.setText(bundle.getString("AlternateGUI.playButton.text")); // NOI18N
        playButton.setEnabled(false);
        playButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onPlayPressed(evt);
            }
        });

        stopButton.setText(bundle.getString("AlternateGUI.stopButton.text")); // NOI18N
        stopButton.setEnabled(false);
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onStopPressed(evt);
            }
        });

        previousButton.setText(bundle.getString("AlternateGUI.previousButton.text")); // NOI18N
        previousButton.setEnabled(false);
        previousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onPreviousPressed(evt);
            }
        });

        nextButton.setText(bundle.getString("AlternateGUI.nextButton.text")); // NOI18N
        nextButton.setEnabled(false);
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onNextPressed(evt);
            }
        });

        randomToggler.setText(bundle.getString("AlternateGUI.randomToggler.text")); // NOI18N
        randomToggler.setEnabled(false);
        randomToggler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleRandom(evt);
            }
        });

        repeatToggler.setText(bundle.getString("AlternateGUI.repeatToggler.text")); // NOI18N
        repeatToggler.setEnabled(false);
        repeatToggler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleRepeat(evt);
            }
        });

        singleToggler.setText(bundle.getString("AlternateGUI.singleToggler.text")); // NOI18N
        singleToggler.setEnabled(false);
        singleToggler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleSinglePlay(evt);
            }
        });

        javax.swing.GroupLayout playerControlsUILayout = new javax.swing.GroupLayout(playerControlsUI);
        playerControlsUI.setLayout(playerControlsUILayout);
        playerControlsUILayout.setHorizontalGroup(
            playerControlsUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playerControlsUILayout.createSequentialGroup()
                .addComponent(playButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previousButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(randomToggler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(repeatToggler)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(singleToggler)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playerControlsUILayout.setVerticalGroup(
            playerControlsUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, playerControlsUILayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(playerControlsUILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playButton)
                    .addComponent(stopButton)
                    .addComponent(previousButton)
                    .addComponent(nextButton)
                    .addComponent(randomToggler)
                    .addComponent(repeatToggler)
                    .addComponent(singleToggler)))
        );

        mainSplitter.setResizeWeight(0.8);
        mainSplitter.setOneTouchExpandable(true);

        songsListing.setAutoCreateRowSorter(true);
        songsListing.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nr", "Title", "Artist", "Rating", "Time", "Year", "Album"
            }
        ));
        songsListing.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        songsListing.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        libraryContent.setViewportView(songsListing);
        songsListing.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (songsListing.getColumnModel().getColumnCount() > 0) {
            songsListing.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title0")); // NOI18N
            songsListing.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title1")); // NOI18N
            songsListing.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title2")); // NOI18N
            songsListing.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title3")); // NOI18N
            songsListing.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title4")); // NOI18N
            songsListing.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title5")); // NOI18N
            songsListing.getColumnModel().getColumn(6).setHeaderValue(bundle.getString("AlternateGUI.songsListing.columnModel.title6")); // NOI18N
        }

        mainSplitter.setLeftComponent(libraryContent);

        detailTabs.addTab(bundle.getString("AlternateGUI.playingScrollpane.TabConstraints.tabTitle"), playingScrollpane); // NOI18N
        detailTabs.addTab(bundle.getString("AlternateGUI.selectedScrollPane.TabConstraints.tabTitle"), selectedScrollPane); // NOI18N

        mainSplitter.setRightComponent(detailTabs);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainSplitter)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(playerControlsUI, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(148, 148, 148))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainSplitter, javax.swing.GroupLayout.DEFAULT_SIZE, 264, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(playerControlsUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onPlayPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onPlayPressed
        playerControls.play();
    }//GEN-LAST:event_onPlayPressed

    private void onStopPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onStopPressed
        playerControls.stop();
    }//GEN-LAST:event_onStopPressed

    private void onPreviousPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onPreviousPressed
       playerControls.previous();
    }//GEN-LAST:event_onPreviousPressed

    private void onNextPressed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onNextPressed
        playerControls.next();
    }//GEN-LAST:event_onNextPressed

    private void toggleRandom(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleRandom
        playerControls.setRandomPlay(this.randomToggler.isSelected());
    }//GEN-LAST:event_toggleRandom

    private void toggleRepeat(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleRepeat
        playerControls.setRepeat(this.repeatToggler.isSelected());
    }//GEN-LAST:event_toggleRepeat

    private void toggleSinglePlay(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleSinglePlay
        playerControls.setSinglePlay(this.singleToggler.isSelected());
    }//GEN-LAST:event_toggleSinglePlay

    private void applyFilter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyFilter
        PlayList currentList = playerControls.getPlayList();
        if (currentList != null) {
            String filterValue = filterField.getText();
            List<Song> filteredList = currentList.applyFilter(filterValue);
            setTableModel(filteredList);
        }
    }//GEN-LAST:event_applyFilter

    public void setLookAndFeel() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane detailTabs;
    private javax.swing.JTextField filterField;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JScrollPane libraryContent;
    private javax.swing.JSplitPane mainSplitter;
    private javax.swing.JButton nextButton;
    private javax.swing.JButton playButton;
    private javax.swing.JPanel playerControlsUI;
    private javax.swing.JScrollPane playingScrollpane;
    private javax.swing.JButton previousButton;
    private javax.swing.JToggleButton randomToggler;
    private javax.swing.JToggleButton repeatToggler;
    private javax.swing.JScrollPane selectedScrollPane;
    private javax.swing.JToggleButton singleToggler;
    private javax.swing.JTable songsListing;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
