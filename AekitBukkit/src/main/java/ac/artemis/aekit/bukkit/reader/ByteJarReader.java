package ac.artemis.aekit.bukkit.reader;

import ac.artemis.aekit.bukkit.jar.BukkitJarFile;
import ac.artemis.aekit.bukkit.resource.DefaultResource;
import ac.artemis.aekit.bukkit.utils.ClassFileUtils;
import ac.artemis.aekit.loader.ClassPool;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.JarReader;
import ac.artemis.aekit.loader.Resource;
import ac.artemis.aekit.loader.pool.ByteClassPool;
import ac.artemis.aekit.loader.pool.DefaultClassPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Cg., Ghast
 */
public class ByteJarReader implements JarReader {

    @Override
    public JarFile loadJarFile(File file) throws IOException {
        return loadJarFile(Files.readAllBytes(file.toPath()));
    }

    @Override
    public JarFile loadJarFile(byte[] fileContent) throws IOException {
        ClassPool<byte[]> classPool = new ByteClassPool();
        Map<String, Resource> resources = new HashMap<>();

        try (ZipInputStream zipIn = new ZipInputStream(new ByteArrayInputStream(fileContent))) {
            ZipEntry entry;
            byte[] buffer = new byte[4096];
            while ((entry = zipIn.getNextEntry()) != null) {
                byte[] entryContent;
                String fileName = entry.getName();
                ByteArrayOutputStream out = new ByteArrayOutputStream();

                int total;
                while ((total = zipIn.read(buffer)) != -1) {
                    out.write(buffer, 0, total);
                }
                entryContent = out.toByteArray();

                if (fileName.endsWith(".class") && ClassFileUtils.isValidClassFile(entryContent)) {
                    classPool.addClass(fileName, entryContent);
                } else if (!entry.isDirectory()) {
                    resources.put(fileName, new DefaultResource(fileName, entryContent, false));
                }
            }
        }
        return new BukkitJarFile(classPool, resources);
    }
}
