package ac.artemis.aekit.bukkit.initiator;

import ac.artemis.aekit.bukkit.reader.ByteJarReader;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.JarReader;
import ac.artemis.aekit.loader.pool.ByteClassPool;
import ac.artemis.aekit.manager.Manager;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.Set;

/**
 * @author Ghast
 */
public class PluginManager implements Manager {

    private final Plugin plugin;
    private JarReader reader;

    public PluginManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        this.reader = new ByteJarReader();
    }

    @Override
    public void disinit() {

    }

    @SneakyThrows
    private Set<File> parsePlugins() {
        for (File rootFile : getPlugins()) {
            JarFile jar = reader.loadJarFile(rootFile);

            ByteClassPool pool = (ByteClassPool) jar.getClassPool();



            if (jar.getResource(plugin.yml))
        }
    }

    private File[] getPlugins() {
        return plugin.getDataFolder().getParentFile().listFiles();
    }
}
