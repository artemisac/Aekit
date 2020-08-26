package ac.artemis.aekit.command;

import ac.artemis.aekit.entity.Executor;

import java.util.Set;

public interface Command {
    String getName();
    String getPermission();
    Set<Command> getSubCommands();

    void onCommand(Executor executor, String... args);
    void onNoPermission(Executor executor);
}
