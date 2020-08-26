package ac.artemis.aekit.manager;

import ac.artemis.aekit.mod.AekitMod;
import lombok.Getter;

public abstract class AekitManager implements Manager {

    public final AekitMod mod;
    public final

    public AekitManager(AekitMod mod) {
        this.mod = mod;
    }
}
