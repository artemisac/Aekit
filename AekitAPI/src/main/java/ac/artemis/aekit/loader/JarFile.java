package ac.artemis.aekit.loader;

import java.util.Map;

public interface JarFile {
    Map<String, Resource> getResources();
    Resource getResource(String fileName);
    void addResource(Resource resource);
    Resource removeResource(String fileName);
    Resource removeResource(Resource resource);
    ClassPool<?> getClassPool();
}
