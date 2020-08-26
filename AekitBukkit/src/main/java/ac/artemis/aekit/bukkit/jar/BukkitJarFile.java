package ac.artemis.aekit.bukkit.jar;

import ac.artemis.aekit.loader.ClassPool;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.Resource;
import ac.artemis.aekit.loader.pool.ByteClassPool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ghast
 */
public class BukkitJarFile implements JarFile {
    private final ClassPool<?> classPool;
    private final Map<String, Resource> resourceFiles;

    public BukkitJarFile() {
        this(new ByteClassPool(), new HashMap<>());
    }

    public BukkitJarFile(ClassPool<?> classPool, Map<String, Resource> resourceFiles) {
        this.resourceFiles = resourceFiles;
        this.classPool = classPool;
    }

    public Map<String, Resource> getResources() {
        return Collections.unmodifiableMap(resourceFiles);
    }

    public Resource getResource(String fileName) {
        return this.resourceFiles.get(fileName);
    }

    public void addResource(Resource resource) {
        if (this.resourceFiles.containsKey(resource.getFileName())) {
            throw new IllegalStateException("resource already exists");
        }
        this.resourceFiles.put(resource.getFileName(), resource);
    }

    public Resource removeResource(String fileName) {
        return this.resourceFiles.remove(fileName);
    }

    public Resource removeResource(Resource resource) {
        return this.removeResource(resource.getFileName());
    }

    public ClassPool<?> getClassPool() {
        return this.classPool;
    }
}
