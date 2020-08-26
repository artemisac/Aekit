package ac.artemis.aekit.bukkit.utils;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {

    private URL link;
    private File root;

    public FileDownloader(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.root = new File(System.getProperty("java.io.tmpdir"));

    }

    public FileDownloader(String link, File root) {
        this(link);
        this.root = root;
    }

    public byte[] download() throws IOException {
        InputStream inputStream = null;
        File file = null;
        ByteArrayOutputStream fos = null;
        try {
            file = File.createTempFile("artemis", ".jar"); // new RandomAccessFile(new File(root, "/wct62HD.jar"), "rw")
            fos = new ByteArrayOutputStream();
            URLConnection urlConn = link.openConnection();
            inputStream = urlConn.getInputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                fos.write(buffer, 0, length);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fos.toByteArray();
    }

    public boolean downloadToPath(File file) throws IOException {
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            URLConnection urlConn = link.openConnection();
            inputStream = urlConn.getInputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                fos.write(buffer, 0, length);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return file.exists();
    }


    public URL getLink() {
        return link;
    }

    public File getRoot() {
        return root;
    }
}
