package ac.artemis.aekit.bukkit.reader;

import ac.artemis.aekit.asm.WrappedClass;
import ac.artemis.aekit.bukkit.jar.BukkitJarFile;
import ac.artemis.aekit.bukkit.resource.DefaultResource;
import ac.artemis.aekit.bukkit.utils.ClassFileUtils;
import ac.artemis.aekit.loader.ClassPool;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.JarReader;
import ac.artemis.aekit.loader.Resource;
import ac.artemis.aekit.loader.pool.AsmClassPool;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;

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
public class AsmJarReader implements JarReader {

    @Override
    public JarFile loadJarFile(File file) throws IOException {
        return loadJarFile(Files.readAllBytes(file.toPath()));
    }

    @Override
    public JarFile loadJarFile(byte[] fileContent) throws IOException {
        ClassPool<WrappedClass> classPool = new AsmClassPool();
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
                    ClassReader reader = new ClassReader(entryContent);

                    ClassNode node = new ClassNode();
                    try {
                        reader.accept(node, ClassReader.EXPAND_FRAMES);
                    } catch (Exception e) {
                        reader.accept(node, ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG);
                    }
                    reader = null;
                    classPool.addClass(fileName, new WrappedClass(fileName, node, entryContent));
                } else if (!entry.isDirectory()) {
                    resources.put(fileName, new DefaultResource(fileName, entryContent, false));
                }
            }
        }
        return new BukkitJarFile(classPool, resources);
    }
}
