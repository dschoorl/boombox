package info.rsdev.boombox;

/**
 * Class with hardcoded values for my computer. To be made configuration options in the future.
 * This is to make things work quickly.
 */
public abstract class Hardcoded {
    
    /**
     * Do not instantiate this class. It is supposed to hold only constants.
     */
    private Hardcoded() {}

    public static final String MUSIC_FOLDER = "/home/dschoorl/Music";
    
    public static final String USER_EXTENSION_DIR = "/home/dschoorl/.boombox/ext";
    
}
