package ac.artemis.aekit.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public interface ModLoader {
    void loadPlugin() throws Exception;

    void unloadPlugin();

    void addDependency(File file) throws MalformedURLException;

    void addDependency(URL url);
}
