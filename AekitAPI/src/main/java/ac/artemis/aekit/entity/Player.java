package ac.artemis.aekit.entity;

import java.net.InetAddress;
import java.util.UUID;

public interface Player extends Executor {

    String getUsername();
    InetAddress getAddress();
}
