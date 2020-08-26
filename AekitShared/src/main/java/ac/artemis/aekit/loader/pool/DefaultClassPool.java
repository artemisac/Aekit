package ac.artemis.aekit.loader.pool;

import ac.artemis.aekit.loader.ClassPool;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Cg.
 */
public class DefaultClassPool<T> implements ClassPool<T> {
    private final Map<String, T> classes;

    public DefaultClassPool() {
        this(new HashMap<>());
    }

    public DefaultClassPool(Map<String, T> classes) {
        this.classes = classes;
    }

    @Override
    public void addClass(String name, T clazz) {
        classes.put(name, clazz);
    }

    @Override
    public T removeClass(String name) {
        return classes.remove(name);
    }

    @Override
    public boolean containsClass(String name) {
        return classes.containsKey(name);
    }

    @Override
    public T getClass(String name) {
        return classes.get(name);
    }

    @Override
    public Map<String, T> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    @Override
    public Set<String> getClassNames() {
        return Collections.unmodifiableSet(classes.keySet());
    }

    @Override
    public int poolSize() {
        return classes.size();
    }

    @Override
    public void clear() {
        classes.clear();
    }

    @Override
    public String toString() {
        return "ClassPool{" +
                "size=" + poolSize() +
                ", classes=" + classes +
                '}';
    }
}
