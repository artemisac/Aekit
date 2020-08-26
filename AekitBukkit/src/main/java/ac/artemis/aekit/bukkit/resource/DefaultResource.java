package ac.artemis.aekit.bukkit.resource;

import ac.artemis.aekit.loader.Resource;

/**
 * @author Cg.
 */
public class DefaultResource implements Resource {
    private String fileName;
    private byte[] content;
    private final boolean directory;

    public DefaultResource(String fileName, byte[] content, boolean directory) {
        this.fileName = fileName;
        this.content = content;
        this.directory = directory;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean isEmpty() {
        return this.content.length == 0;
    }

    @Override
    public boolean isDirectory() {
        return this.directory;
    }
}
