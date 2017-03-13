package info.rsdev.boombox;

import java.util.prefs.Preferences;

/**
 * A set of constants that the core BoomBox uses as keys into the {@link Preferences}
 */
public abstract class PrefKeys {
    
    /**
     * Do not instantiate. This class only holds contants.
     */
    private PrefKeys() {}
    
    public static final String DATADIR_KEY = "DataDir";
    
}
