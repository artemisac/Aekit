package ac.artemis.aekit.asm;

import lombok.Getter;
import lombok.Setter;
import org.objectweb.asm.tree.ClassNode;

/**
 * @author Ghast
 */

@Getter
@Setter
public class WrappedClass {
    private final String name;
    private final ClassNode node;
    private final byte[] raw;

    public WrappedClass(String name, ClassNode node, byte[] raw) {
        this.name = name;
        this.node = node;
        this.raw = raw;
    }
}
