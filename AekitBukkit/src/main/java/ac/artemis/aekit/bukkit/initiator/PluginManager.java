package ac.artemis.aekit.bukkit.initiator;

import ac.artemis.aekit.asm.WrappedClass;
import ac.artemis.aekit.bukkit.mod.AsmPluginLoader;
import ac.artemis.aekit.bukkit.mod.BukkitPluginLoader;
import ac.artemis.aekit.bukkit.reader.AsmJarReader;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.JarReader;
import ac.artemis.aekit.loader.ModLoader;
import ac.artemis.aekit.loader.pool.AsmClassPool;
import ac.artemis.aekit.manager.Manager;
import ac.artemis.aekit.mod.ModInfo;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Ghast
 */
public class PluginManager implements Manager {

    private final Plugin plugin;
    private final List<ModLoader> mods = new ArrayList<>();
    private JarReader reader;

    public PluginManager(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void init() {
        this.reader = new AsmJarReader();
        initPlugins();
    }

    @Override
    public void disinit() {

    }

    @SneakyThrows
    private void initPlugins() {
        Map<File, WrappedClass> mods = parsePlugins();

        for (Map.Entry<File, WrappedClass> mod : mods.entrySet()) {
            ModLoader loader = new AsmPluginLoader(reader.loadJarFile(mod.getKey()),
                    mod.getValue(), plugin);
            this.mods.add(loader);
        }
    }

    @SneakyThrows
    private Map<File, WrappedClass> parsePlugins() {
        Map<File, WrappedClass> valid = new WeakHashMap<>();
        for (File rootFile : getPlugins()) {
            JarFile jar = reader.loadJarFile(rootFile);

            AsmClassPool pool = (AsmClassPool) jar.getClassPool();

            Map.Entry<String, WrappedClass> classEntry = iterateClasses(pool.getClasses());
            if (classEntry != null) {
                valid.put(rootFile, classEntry.getValue());
            }
        }
        return valid;
    }

    private Map.Entry<String, WrappedClass> iterateClasses(Map<String, WrappedClass> nodeMap) {
        return nodeMap
                // Iterate every class
                .entrySet().parallelStream()
                // If any class annotate
                .filter(entry -> {
                    // Get the node
                    ClassNode node = entry.getValue().getNode();

                    // Check if the node is annotated by ModInfo
                    final boolean flag = node.visibleAnnotations.stream()
                            .anyMatch(ann -> {
                                String annName = ann.desc;
                                String clazzName = ModInfo.class.getName();
                                System.out.println("ann=" + annName + " clazz=" + clazzName);
                                return annName.equalsIgnoreCase(clazzName);
                            });
                    System.out.println("entry=" + entry.getKey() + " var=" + flag);
                    return flag;
                })

                // Find the first
                .findFirst()
                // Or return null
                .orElse(null);
    }

    private File[] getPlugins() {
        return plugin.getDataFolder().getParentFile().listFiles();
    }
}
