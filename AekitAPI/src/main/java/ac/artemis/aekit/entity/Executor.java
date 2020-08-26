package ac.artemis.aekit.entity;

public interface Executor extends Entity {
    boolean hasPermission(String permission);

}
