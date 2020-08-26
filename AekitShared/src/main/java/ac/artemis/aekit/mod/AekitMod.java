package ac.artemis.aekit.mod;

import ac.artemis.aekit.command.CommandManager;
import ac.artemis.aekit.dependency.Dependency;
import ac.artemis.aekit.loader.ModLoader;
import lombok.Getter;
import lombok.SneakyThrows;

import java.net.URL;

@Getter
public abstract class AekitMod implements Mod {
    private final ModInfo info = this.getClass().getAnnotation(ModInfo.class);
    private final ModLoader modLoader;
    private CommandManager commandManager;

    public AekitMod(ModLoader modLoader) {
        this.modLoader = modLoader;
    }

    @Override
    public void load() {
        this.loadDepends();
        this.loadCommands();
        onLoad();
    }

    @SneakyThrows
    private void loadDepends() {
        if (!this.getClass().isAnnotationPresent(Dependency.class)) return;

        Dependency dependency = this.getClass().getAnnotation(Dependency.class);

        for (String uri : dependency.url()) {
            URL url = new URL(uri);
            this.modLoader.addDependency(url);
        }
    }

    private void loadCommands() {
        this.commandManager = new CommandManager(this);
        this.commandManager.init();
    }

    @Override
    public void init() {
        onInit();
    }

    @Override
    public void disinit() {
        onDisinit();
    }

    public abstract void onInit();
    public abstract void onDisinit();
    public abstract void onLoad();
}
