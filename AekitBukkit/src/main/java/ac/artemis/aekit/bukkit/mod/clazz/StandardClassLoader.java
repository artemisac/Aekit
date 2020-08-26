package ac.artemis.aekit.bukkit.mod.clazz;

import ac.artemis.aekit.loader.ModClassLoader;

import java.util.Set;

/**
 * @author Cg.
 */
public abstract class StandardClassLoader extends ClassLoader implements ModClassLoader {
    static {
        ClassLoader.registerAsParallelCapable();
    }

    protected StandardClassLoader(ClassLoader parent) {
        super(parent);
    }

    public abstract Set<String> getClasses();
}
