package ac.artemis.aekit.bukkit.mod.body;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Cg.
 */
public interface MethodBody extends Opcodes {
    String getMethodName();

    String getMethodDescriptor();

    MethodNode generate();
}
