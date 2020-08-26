package ac.artemis.aekit.bukkit.mod;

import org.bukkit.plugin.InvalidPluginException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Cg.
 */
public interface PluginLoader {
    void loadPlugin() throws InvalidPluginException;

    void unloadPlugin();

    void addDependency(File file) throws MalformedURLException;

    void addDependency(URL url);
}
