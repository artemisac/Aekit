package ac.artemis.aekit.loader.pool;

import java.util.Map;

/**
 * @author Cg.
 */
public class ByteClassPool extends DefaultClassPool<byte[]> {
    public ByteClassPool() {
    }

    public ByteClassPool(Map<String, byte[]> classes) {
        super(classes);
    }
}
