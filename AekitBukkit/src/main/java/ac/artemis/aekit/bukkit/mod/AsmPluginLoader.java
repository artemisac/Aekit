package ac.artemis.aekit.bukkit.mod;

import ac.artemis.aekit.asm.WrappedClass;
import ac.artemis.aekit.bukkit.mod.body.GetResourceMethod;
import ac.artemis.aekit.bukkit.mod.body.MethodBody;
import ac.artemis.aekit.bukkit.mod.clazz.BukkitPluginClassLoader;
import ac.artemis.aekit.bukkit.mod.clazz.DependencyClassLoader;
import ac.artemis.aekit.bukkit.utils.Reflection;
import ac.artemis.aekit.loader.*;
import ac.artemis.aekit.loader.pool.ByteClassPool;
import ac.artemis.aekit.mod.ModInfo;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.PluginCommandYamlParser;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.logging.Level;

/**
 * @author Cg., Ghast
 */
public class AsmPluginLoader implements ModLoader {
    private static final Reflection.MethodInvoker REMOVE_CLASS_METHOD = Reflection.getMethod(JavaPluginLoader.class, "removeClass", String.class);

    private final JavaPluginLoader pluginLoader;
    private final DependencyClassLoader dependencyClassLoader;

    private final JarFile jarFile;
    private final Plugin loaderPlugin;
    private final WrappedClass main;
    private JavaPlugin plugin;

    public AsmPluginLoader(JarFile jarFile, WrappedClass mainClass, Plugin loaderPlugin) {
        this.jarFile = jarFile;
        this.loaderPlugin = loaderPlugin;
        this.pluginLoader = (JavaPluginLoader) loaderPlugin.getPluginLoader();
        this.main = mainClass;
        this.dependencyClassLoader = new DependencyClassLoader(pluginLoader.getClass().getClassLoader());
    }

    @Override
    public void loadPlugin() throws InvalidPluginException {

        ClassPool<?> classPool = jarFile.getClassPool();
        String mainClass = main.getName().replace(".", "/").concat(".class");
        if (!classPool.containsClass(mainClass)) {
            throw new InvalidPluginException("Cannot find main class `" + main.getName() + "'");
        }

        AnnotationNode node = main.getNode().visibleAnnotations.stream()
                .filter(ann -> {
                    String annName = ann.desc;
                    String clazzName = ModInfo.class.getName();
                    System.out.println("ann=" + annName + " clazz=" + clazzName);
                    return annName.equalsIgnoreCase(clazzName);
                })
                .findFirst()
                .orElse(null);

        if (node == null) {
            throw new InvalidPluginException();
        }

        // The values are distributed in this format
        // object's name - object's value - object2's name - object2's value etc...
        String name = (String) node.values.get(1);
        String version = (String) node.values.get(3);
        String[] authors = (String[]) node.values.get(5);

        PluginDescriptionFile description = new PluginDescriptionFile(name, version, mainClass);
        // Todo finish the rest of the description via reflections

        try {
            if (classPool instanceof ByteClassPool) {
                ByteClassPool byteClassPool = ((ByteClassPool) classPool);
                byte[] classContent = byteClassPool.getClass(mainClass);
                byteClassPool.addClass(mainClass, processMainClass(classContent));
            }
        } catch (Exception e) {
            throw new InvalidPluginException(e);
        }

        Server server = loaderPlugin.getServer();
        PluginManager pluginManager = server.getPluginManager();
        File dataFolder = new File(loaderPlugin.getDataFolder().getParentFile(), description.getName());

        for (String pluginName : description.getDepend()) {
            Plugin current = pluginManager.getPlugin(pluginName);
            if (current == null) {
                throw new UnknownDependencyException(pluginName);
            }
        }

        BukkitPluginClassLoader classLoader;
        try {
            classLoader = new BukkitPluginClassLoader(pluginLoader, dependencyClassLoader, description, dataFolder, jarFile);
        } catch (InvalidPluginException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }
        plugin = classLoader.getPlugin();

        List<Command> pluginCommands = PluginCommandYamlParser.parse(plugin);
        if (!pluginCommands.isEmpty()) {
            SimpleCommandMap commandMap = Reflection.getField(pluginManager.getClass(), "commandMap", SimpleCommandMap.class).get(pluginManager);
            commandMap.registerAll(description.getName(), pluginCommands);
        }

        plugin.getLogger().info("Enabling " + plugin.getDescription().getFullName());
        try {
            Reflection.getMethod(JavaPlugin.class, "setEnabled", boolean.class).invoke(plugin, true);
        } catch (Throwable ex) {
            server.getLogger().log(Level.SEVERE, "Error occurred while enabling " + plugin.getDescription().getFullName() + " (Is it up to date?)", ex);
        }

        pluginManager.callEvent(new PluginEnableEvent(plugin));
        HandlerList.bakeAll();
    }

    @Override
    public void unloadPlugin() {
        if (plugin != null && plugin.isEnabled()) {
            PluginManager pluginManager = plugin.getServer().getPluginManager();
            pluginManager.disablePlugin(plugin);

            BukkitPluginClassLoader cl = (BukkitPluginClassLoader) plugin.getClass().getClassLoader();
            for (String className : cl.getClasses()) {
                REMOVE_CLASS_METHOD.invoke(plugin.getPluginLoader(), className);
            }
        }
    }

    @Override
    public void addDependency(File file) throws MalformedURLException {
        addDependency(file.toURI().toURL());
    }

    @Override
    public void addDependency(URL url) {
        this.dependencyClassLoader.addURL(url);
    }

    @Override
    public Map<String, Class<?>> getClasses() {
        Reflection.FieldAccessor<Map> fieldAccessor = Reflection.getField(JavaPluginLoader.class, "classes", Map.class);
        Map<String, Class<?>> map = (Map<String, Class<?>>) fieldAccessor.get(this.pluginLoader);
        return map;
    }
    
    private byte[] processMainClass(byte[] classContent) {
        ClassReader classReader = new ClassReader(classContent);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        MethodBody getResource = new GetResourceMethod(classNode.version, classNode.name);
        for (MethodNode methodNode : classNode.methods) {
            if (getResource.getMethodName().equals(methodNode.name) && getResource.getMethodDescriptor().equals(methodNode.desc)) {
                return classContent;
            }
        }
        classNode.methods.add(getResource.generate());

        ClassWriter classWriter = new ClassWriter(0);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    @Override
    public JarFile getJarFile() {
        return jarFile;
    }

    @Override
    public ModClassLoader getClassLoader() {
        return dependencyClassLoader;
    }
}
