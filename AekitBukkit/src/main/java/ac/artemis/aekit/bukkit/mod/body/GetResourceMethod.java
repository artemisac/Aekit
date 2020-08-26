package ac.artemis.aekit.bukkit.mod.body;

import org.objectweb.asm.Label;
import org.objectweb.asm.tree.MethodNode;

/**
 * @author Cg.
 */
public class GetResourceMethod implements MethodBody {
    private final int classMajorVersion;
    private final String className;

    public GetResourceMethod(int classMajorVersion, String className) {
        this.classMajorVersion = classMajorVersion;
        this.className = className;
    }

    @Override
    public String getMethodName() {
        return "getResource";
    }

    @Override
    public String getMethodDescriptor() {
        return "(Ljava/lang/String;)Ljava/io/InputStream;";
    }

    @Override
    public MethodNode generate() {
        MethodNode methodNode = new MethodNode(ACC_PUBLIC, getMethodName(), getMethodDescriptor(), null, null);
        methodNode.visitCode();
        Label label0 = new Label();
        methodNode.visitLabel(label0);
        methodNode.visitVarInsn(ALOAD, 1);
        Label label1 = new Label();
        methodNode.visitJumpInsn(IFNONNULL, label1);
        Label label2 = new Label();
        methodNode.visitLabel(label2);
        methodNode.visitTypeInsn(NEW, "java/lang/IllegalArgumentException");
        methodNode.visitInsn(DUP);
        methodNode.visitLdcInsn("Filename cannot be null");
        methodNode.visitMethodInsn(INVOKESPECIAL, "java/lang/IllegalArgumentException", "<init>", "(Ljava/lang/String;)V", false);
        methodNode.visitInsn(ATHROW);
        methodNode.visitLabel(label1);
        if (classMajorVersion >= 51) {
            methodNode.visitFrame(F_SAME, 0, null, 0, null);
        }
        methodNode.visitVarInsn(ALOAD, 0);
        methodNode.visitMethodInsn(INVOKEVIRTUAL, className, "getClassLoader", "()Ljava/lang/ClassLoader;", false);
        methodNode.visitVarInsn(ALOAD, 1);
        methodNode.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "getResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;", false);
        methodNode.visitInsn(ARETURN);
        Label label3 = new Label();
        methodNode.visitLabel(label3);
        methodNode.visitMaxs(3, 2);
        methodNode.visitEnd();
        return methodNode;
    }
}
