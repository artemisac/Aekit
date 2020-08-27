package ac.artemis.aekit.manager;

import ac.artemis.aekit.loader.ModLoader;
import ac.artemis.aekit.mod.AekitMod;
import lombok.Getter;

public abstract class AekitManager implements Manager {

    public final ModLoader mod;
    public AekitManager(ModLoader mod) {
        this.mod = mod;
    }
}
