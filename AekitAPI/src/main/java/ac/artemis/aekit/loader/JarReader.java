package ac.artemis.aekit.loader;

import java.io.File;
import java.io.IOException;

/**
 * @author Ghast
 */
public interface JarReader {
    JarFile loadJarFile(File file) throws IOException;

    JarFile loadJarFile(byte[] fileContent) throws IOException;
}
