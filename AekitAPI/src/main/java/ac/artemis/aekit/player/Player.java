package ac.artemis.aekit.player;

import java.net.InetAddress;
import java.util.UUID;

public interface Player {
    UUID getUniqueId();
    String getUsername();
    InetAddress getAddress();
}
