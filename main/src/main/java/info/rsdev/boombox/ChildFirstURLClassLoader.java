package info.rsdev.boombox;

import info.rsdev.boombox.domain.Factory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

/**
 * Classloader written by Yoni, code published on http://stackoverflow.com/a/6424879. Slightly adopted,
 * to let the classpath be determined by the classloader and not externally. 
 */
public class ChildFirstURLClassLoader extends URLClassLoader {
    
    public static final Logger logger = Logger.getLogger(Factory.class.getName());
    
    private ClassLoader system;

    public ChildFirstURLClassLoader(ClassLoader parent) {
        super(getClassPath(), parent);
        system = getSystemClassLoader();
    }

    @Override
    protected synchronized Class<?> loadClass(String name, boolean resolve)throws ClassNotFoundException {
        // First, check if the class has already been loaded
        Class<?> c = findLoadedClass(name);
        if (c == null) {
            if (system != null) {
                try {
                    // checking system: jvm classes, endorsed, cmd classpath, etc.
                    c = system.loadClass(name);
                }
                catch (ClassNotFoundException ignored) {
                }
            }
            if (c == null) {
                try {
                    // checking local
                    c = findClass(name);
                } catch (ClassNotFoundException e) {
                    // checking parent
                    // This call to loadClass may eventually call findClass again, in case the parent doesn't find anything.
                    c = super.loadClass(name, resolve);
                }
            }
        }
        if (resolve) {
            resolveClass(c);
        }
        return c;
    }

    @Override
    public URL getResource(String name) {
        URL url = null;
        if (system != null) {
            url = system.getResource(name); 
        }
        if (url == null) {
            url = findResource(name);
            if (url == null) {
                // This call to getResource may eventually call findResource again, in case the parent doesn't find anything.
                url = super.getResource(name);
            }
        }
        return url;
    }

    @Override
    public Enumeration<URL> getResources(String name) throws IOException {
        /**
        * Similar to super, but local resources are enumerated before parent resources
        */
        Enumeration<URL> systemUrls = null;
        if (system != null) {
            systemUrls = system.getResources(name);
        }
        Enumeration<URL> localUrls = findResources(name);
        Enumeration<URL> parentUrls = null;
        if (getParent() != null) {
            parentUrls = getParent().getResources(name);
        }
        final List<URL> urls = new ArrayList<URL>();
        if (systemUrls != null) {
            while(systemUrls.hasMoreElements()) {
                urls.add(systemUrls.nextElement());
            }
        }
        if (localUrls != null) {
            while (localUrls.hasMoreElements()) {
                urls.add(localUrls.nextElement());
            }
        }
        if (parentUrls != null) {
            while (parentUrls.hasMoreElements()) {
                urls.add(parentUrls.nextElement());
            }
        }
        return new Enumeration<URL>() {
            Iterator<URL> iter = urls.iterator();

            public boolean hasMoreElements() {
                return iter.hasNext(); 
            }
            public URL nextElement() {
                return iter.next();
            }
        };
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        URL url = getResource(name);
        try {
            return url != null ? url.openStream() : null;
        } catch (IOException e) {
        }
        return null;
    }
    
    /**
     * Dynamically determine the classpath, based on the user preference settings for extension directories
     * @return 
     */
    private static URL[] getClassPath() {
    	Preferences userPrefs = Preferences.userNodeForPackage(BoomBox.class);
    	File userExtDir = new File(userPrefs.get("", Hardcoded.USER_EXTENSION_DIR));
    	List<URL> classpath = Collections.emptyList();
    	File[] jarFiles = null;
    	if (userExtDir.isDirectory()) {
    	    jarFiles = userExtDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && pathname.getName().toLowerCase().endsWith(".jar");
                }
            });
    	}
    	if ((jarFiles != null) && (jarFiles.length > 0)) {
    	    classpath = new ArrayList<>(jarFiles.length);
    	    for (int i = 0; i < jarFiles.length; i++) {
    	        try {
                    classpath.add(jarFiles[i].toURI().toURL());
                    logger.info(String.format("Adding %s to classpath", jarFiles[i]));
                } catch (MalformedURLException e) {
                    logger.log(Level.SEVERE, String.format("Ommitting user supplied jarfile %s", jarFiles[i]), e);
                }
    	    }
    	}
//    	String systemExtDir = userPrefs.get("", "");   //TODO or not TODO, that's the question
    	return classpath.toArray(new URL[classpath.size()]);
    }

}