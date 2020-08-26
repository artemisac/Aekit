package ac.artemis.aekit.loader;

import java.util.Map;
import java.util.Set;

/**
 * @author Cg.
 */
public interface ClassPool<T> {
    void addClass(String name, T clazz);

    T removeClass(String name);

    boolean containsClass(String name);

    T getClass(String name);

    Map<String, T> getClasses();

    Set<String> getClassNames();

    int poolSize();

    void clear();
}