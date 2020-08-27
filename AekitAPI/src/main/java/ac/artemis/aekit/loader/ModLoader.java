package ac.artemis.aekit.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public interface ModLoader {
    JarFile getJarFile();

    ModClassLoader getClassLoader();

    Map<String, Class<?>> getClasses();

    void loadPlugin() throws Exception;

    void unloadPlugin();

    void addDependency(File file) throws MalformedURLException;

    void addDependency(URL url);
}
