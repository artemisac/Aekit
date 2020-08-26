package ac.artemis.aekit.mod;

import ac.artemis.aekit.loader.ModLoader;
import lombok.Getter;

@Getter
public abstract class AekitMod implements Mod {
    private final ModInfo info = this.getClass().getAnnotation(ModInfo.class);
    private final ModLoader modLoader;

    public AekitMod(ModLoader modLoader) {
        this.modLoader = modLoader;
    }

    @Override
    public void load() {
        this.checkDependencies();
    }

    private void checkDependencies() {
        if ()
    }

    @Override
    public void init() {

    }

    @Override
    public void disinit() {

    }

    public abstract void onInit();
    public abstract void onDisinit();
    public abstract void onLoad();
}
