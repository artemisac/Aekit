package ac.artemis.aekit.command;

import ac.artemis.aekit.loader.ModLoader;
import ac.artemis.aekit.manager.AekitManager;
import ac.artemis.aekit.manager.Manager;
import ac.artemis.aekit.mod.AekitMod;

import java.util.HashSet;
import java.util.Set;

public class CommandManager extends AekitManager {

    private final Set<Command> commands = new HashSet<>();

    public CommandManager(ModLoader mod) {
        super(mod);
    }

    @Override
    public void init() {
        mod.getJarFile()
    }

    @Override
    public void disinit() {

    }
}
