package ac.artemis.aekit.bukkit.mod.clazz;

import ac.artemis.aekit.bukkit.utils.Reflection;
import ac.artemis.aekit.loader.ClassPool;
import ac.artemis.aekit.loader.JarFile;
import ac.artemis.aekit.loader.Resource;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Cg., Ghast
 */
public class BukkitPluginClassLoader extends PluginClassLoader {
    private static final Reflection.MethodInvoker SET_CLASS_METHOD = Reflection.getMethod(JavaPluginLoader.class, "setClass", String.class, Class.class);
    private static final Reflection.MethodInvoker GET_CLASS_BY_NAME_METHOD = Reflection.getMethod(JavaPluginLoader.class, "getClassByName", String.class);
    private static final Reflection.FieldAccessor<Server> GET_SERVER_FIELD = Reflection.getField(JavaPluginLoader.class, "server", Server.class);
    private static final Reflection.MethodInvoker GET_UNSAFE_METHOD;
    private static final Reflection.MethodInvoker PROCESS_CLASS_METHOD;

    private final DependencyClassLoader dependencyClassLoader;
    private final Map<String, Class<?>> classes = new ConcurrentHashMap<>();
    private final JavaPluginLoader loader;
    private final JarFile jarFile;
    private final PluginDescriptionFile description;
    private final JavaPlugin plugin;

    static {
        registerAsParallelCapable();

        Reflection.MethodInvoker processClass = null;
        Reflection.MethodInvoker getUnsafe = null;
        try {
            Method getUnsafeMethod = Server.class.getDeclaredMethod("getUnsafe");
            Class<?> unsafeCls = getUnsafeMethod.getReturnType();
            getUnsafe = Reflection.getMethod(Server.class, "getUnsafe");
            processClass = Reflection.getMethod(unsafeCls, "processClass", PluginDescriptionFile.class, String.class, byte[].class);
        } catch (Exception ignore) {
        }
        GET_UNSAFE_METHOD = getUnsafe;
        PROCESS_CLASS_METHOD = processClass;
    }

    public BukkitPluginClassLoader(JavaPluginLoader loader, DependencyClassLoader parent, PluginDescriptionFile description, File dataFolder, JarFile jarFile) throws InvalidPluginException {
        super(parent);
        this.dependencyClassLoader = parent;
        this.loader = loader;
        this.jarFile = jarFile;
        this.description = description;

        try {
            Class<?> mainClass;
            try {
                mainClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class '" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = mainClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class '" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            Reflection.ConstructorInvoker ctor = Reflection.getConstructor(pluginClass, JavaPluginLoader.class,
                    PluginDescriptionFile.class, File.class, File.class);
            plugin = (JavaPlugin) ctor.invoke(loader, description, dataFolder, null);
        } catch (Exception ex) {
            throw new InvalidPluginException("Failed to load plugin", ex);
        }
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    @Override
    public Set<String> getClasses() {
        return classes.keySet().stream().map(name -> name.replace("/", ".").substring(0, name.lastIndexOf('.'))).collect(Collectors.toSet());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.startsWith("org.bukkit.") || name.startsWith("net.minecraft.")) {
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);
        if (result == null) {
            // Get the class from global
            result = (Class<?>) GET_CLASS_BY_NAME_METHOD.invoke(loader, name);
            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                ClassPool<?> cp = jarFile.getClassPool();

                byte[] classBytes = (byte[]) jarFile.getClassPool().getClass(path);;

                if (classBytes != null) {
                    if (PROCESS_CLASS_METHOD != null) {
                        classBytes = (byte[]) PROCESS_CLASS_METHOD.invoke(GET_UNSAFE_METHOD.invoke(GET_SERVER_FIELD.get(loader)), description, path, classBytes);
                    }
                    // Todo: definePackage
                    result = defineClass(name, classBytes, 0, classBytes.length, null);
                }

                if (result == null) {
                    result = super.findClass(name);
                }

                if (result != null) {
                    SET_CLASS_METHOD.invoke(loader, name, result);
                }
            }
            classes.put(name, result);
        }
        return result;
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        Resource res = jarFile.getResource(name);
        if (res == null) {
            return null;
        }
        byte[] resBytes = res.getContent();
        if (resBytes == null) {
            return null;
        }
        return new ByteArrayInputStream(resBytes);
    }

    public void addDependency(File file) throws MalformedURLException {
        addDependency(file.toURI().toURL());
    }

    public void addDependency(URL url) {
        this.dependencyClassLoader.addURL(url);
    }

    protected void clear() {
        classes.clear();
    }
}