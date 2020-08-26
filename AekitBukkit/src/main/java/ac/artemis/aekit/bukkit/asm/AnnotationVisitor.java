package ac.artemis.aekit.bukkit.asm;

import org.objectweb.asm.ClassVisitor;

/**
 * @author Ghast
 */
public class AnnotationVisitor extends ClassVisitor {
    public AnnotationVisitor(int api) {
        super(api);
    }

    public AnnotationVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public org.objectweb.asm.AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        return super.visitAnnotation(descriptor, visible);
    }
}
