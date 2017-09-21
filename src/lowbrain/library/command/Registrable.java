package lowbrain.library.command;

import lowbrain.library.main.LowbrainLibrary;
import org.apache.commons.lang.NullArgumentException;
import org.jetbrains.annotations.Contract;

import java.util.Collection;
import java.util.HashMap;

public interface Registrable {
    /**
     * register a single command with a single execution
     * @param cmd command name
     * @param executor command handler
     */
    @Contract("null, _ -> fail")
    default void register(String cmd, Command executor) {
        if (cmd == null || cmd.trim().length() == 0 || executor == null)
            throw new NullArgumentException("Invalid arguments !");

        cmd = ignoreCase() ? cmd.toLowerCase() : cmd;

        if (getSubs().containsKey(cmd))
            LowbrainLibrary.getInstance().getLogger().warning("You have registered a same command twice. The first one will be overwriting : " + cmd);

        getSubs().put(cmd, executor);
    }

    /**
     * register mutiples commands with the same execution
     * @param cmds list of commands
     * @param executor command handler for all commands
     */
    default void register(Collection<String> cmds, Command executor) {
        if (cmds == null || cmds.size() == 0)
            return;

        if (executor == null)
            throw new NullArgumentException("executor");

        for (String c : cmds) {
            String cmd = ignoreCase() ? c.toLowerCase() : c;

            if (getSubs().containsKey(cmd))
                LowbrainLibrary.getInstance().getLogger().warning("You have registered a same command twice. The first one will be overwriting : " + cmd);

            getSubs().put(cmd, executor);
        }

    }

    /**
     * unregister a command
     * @param cmd command name
     */
    @Contract("null -> fail")
    default void unregister(String cmd) {
        if (cmd == null || cmd.trim().length() == 0 )
            throw new NullArgumentException("Invalid arguments !");

        cmd = ignoreCase() ? cmd.toLowerCase() : cmd;

        if (getSubs().containsKey(cmd))
            getSubs().remove(cmd);
    }

    /**
     * unregister multiple commands
     * @param cmds list of commands
     */
    default void unregister(Collection<String> cmds) {
        cmds.forEach(s -> unregister(s));
    }

    HashMap<String, Command> getSubs();
    HashMap<String, Command> setSubs(HashMap<String, Command> subs);
    boolean ignoreCase(boolean set);
    boolean ignoreCase();

}
