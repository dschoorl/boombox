package info.rsdev.boombox.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.logging.Logger;

public abstract class Utils {
    
    public static final Logger logger = Logger.getLogger(Utils.class.getName());
    
    private Utils() {}
    
    /**
     * Convert a String number, representing whole seconds, into a minute notation (m:ss) -- could it be hours?
     * @param seconds
     * @return
     */
    public static String sec2min(String secondsAsString) {
        int seconds = 0;
        try {
            seconds = Integer.parseInt(secondsAsString);
        } catch (NumberFormatException e) {
            logger.severe(String.format("Invalid number format: %s", secondsAsString));
        }
        return String.format("%d:%02d", (seconds / 60), (seconds % 60));
    }
    
    /**
     * Concatenate two arrays of the same type. The second array is append to the first array.
     * @param first
     * @param second
     * @return
     */
    public static <T> T[] concat(T[] first, T[] second) {
        if (second == null) {return first; } // shortcut for common situation
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    
    public static File[] traverseAndCollect(File rootDir, FileFilter selector) {
        return null;
    }
    
}
