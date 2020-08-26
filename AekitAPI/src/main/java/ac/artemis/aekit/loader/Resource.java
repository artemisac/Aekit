package ac.artemis.aekit.loader;

/**
 * @author Cg.
 */
public interface Resource {
    String getFileName();

    void setFileName(String fileName);

    byte[] getContent();

    void setContent(byte[] content);

    boolean isEmpty();

    boolean isDirectory();
}
