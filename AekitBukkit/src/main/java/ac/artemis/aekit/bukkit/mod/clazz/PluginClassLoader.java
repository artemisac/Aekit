package ac.artemis.aekit.bukkit.mod.clazz;

import java.util.Set;

/**
 * @author Cg.
 */
public abstract class PluginClassLoader extends ClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    protected PluginClassLoader(ClassLoader parent) {
        super(parent);
    }

    public abstract Set<String> getClasses();
}
