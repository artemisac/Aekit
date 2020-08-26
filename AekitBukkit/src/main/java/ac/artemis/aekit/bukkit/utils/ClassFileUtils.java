package ac.artemis.aekit.bukkit.utils;

import java.util.Arrays;

/**
 * Utilities for Java class file
 *
 * @author Cg.
 */
public class ClassFileUtils {
    /**
     * Class file magic number: 0xCAFEBABE :)
     */
    public static final byte[] MAGIC = new byte[]{(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};

    /**
     * Checks whether a valid Java class file
     *
     * @param classBytes Class content
     * @return {@code true} if the given Java class file is valid
     */
    public static boolean isValidClassFile(byte[] classBytes) {
        return classBytes.length > 4 && Arrays.equals(Arrays.copyOf(classBytes, 4), ClassFileUtils.MAGIC);
    }
}
