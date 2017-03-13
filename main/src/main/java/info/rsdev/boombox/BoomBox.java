/**
 * 
 */
package info.rsdev.boombox;

import info.rsdev.boombox.domain.FilesystemSource;
import info.rsdev.boombox.ui.alternate.AlternateGUI;

import java.awt.EventQueue;
import java.io.File;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * This class reads user preferences and bootstraps the application
 *    
 * @author dschoorl
 */
public class BoomBox {
    
    private static final String SYSTEM_DATADIR = "";
    
    private final Logger logger;
	
	/**
	 * The location where application data is stored. Can be set/changed in user preferences, but
	 * needs an application restart in order to take effect. This construction is chose, in order to
	 * allow overwriting the prefered location through a command line parameter. 
	 */
	private final File dataDir;
	
	private BoomBox(File dataDir) {
		if (dataDir == null) {
			throw new NullPointerException(Messages.getString("BoomBox.0")); //$NON-NLS-1$
		}
		this.dataDir = dataDir;
		logger = Logger.getLogger(BoomBox.class.getName());
		logger.info(String.format("DataDir=%s", dataDir));
	}
	
    /**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
	    //TODO: check if application runs on Java 7 or newer and also if DerbyDB is available and maybe JavaFX?
	    
	    File dataDir = getDataDir(args);
	    System.setProperty(PrefKeys.DATADIR_KEY, dataDir.getAbsolutePath());
	    
	    /* Initialize LogManager to read from logging.properties file in .boombox data directory or else from
	     * logging.properties from this jar
	     */
	    InputStream configStream = null;
	    File configFile = new File(dataDir, "logging.properties");
	    if (!configFile.isFile()) {
	        //read default logging.properties from classpath 
	        configStream = BoomBox.class.getClassLoader().getResourceAsStream("logging.properties");
	    }
	    try {
	        LogManager.getLogManager().readConfiguration(configStream);
        } finally {
            if (configStream != null) {
                configStream.close();
            }
        }
	    
		//create custom classloader so that we can extend the classpath without explicitely setting it
		ClassLoader parent = Thread.currentThread().getContextClassLoader();
		ChildFirstURLClassLoader classloader = new ChildFirstURLClassLoader(parent);
		Thread.currentThread().setContextClassLoader(classloader);
		
		BoomBox boomBoxInstance = new BoomBox(dataDir);
		boomBoxInstance.startGui();
	}

	private void startGui() {
        //TODO: offer multiple UI's that can be choosen via Preferences and instantiated dynamically
        
        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                AlternateGUI gui = new AlternateGUI(new FilesystemSource(new File(Hardcoded.MUSIC_FOLDER)));
                gui.setLookAndFeel();
                gui.setVisible(true);
            }
        });
	}
	
	private static File getDataDir(String[] args) {
		//TODO: provide alternative location of filecache location via commandline options

		File dataDir = null;
		Preferences userPrefs = Preferences.userNodeForPackage(BoomBox.class);
		String preferedPath = userPrefs.get(PrefKeys.DATADIR_KEY, null);
		if (preferedPath == null) {
			//TODO: on windows, make it a hidden file
			dataDir = new File(new File(System.getProperty("user.home")), ".boombox");
			userPrefs.put("DataDir", dataDir.getAbsolutePath()); //$NON-NLS-1$
			try {
				userPrefs.flush();
			} catch (BackingStoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			dataDir = new File(preferedPath);
		}
		
		if (!dataDir.isDirectory() && !dataDir.mkdirs()) {
			throw new RuntimeException(String.format(Messages.getString("BoomBox.7"), dataDir.getAbsolutePath())); //$NON-NLS-1$
		}
		
		return dataDir;
	}
	
}
