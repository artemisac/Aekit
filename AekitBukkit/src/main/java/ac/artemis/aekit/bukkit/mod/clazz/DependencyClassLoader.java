package ac.artemis.aekit.bukkit.mod.clazz;

import ac.artemis.aekit.loader.ModClassLoader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * @author Cg.
 */
public class DependencyClassLoader extends URLClassLoader implements ModClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public DependencyClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }

    public DependencyClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    @Override
    public void addURL(URL url) {
        super.addURL(url);
    }
}
