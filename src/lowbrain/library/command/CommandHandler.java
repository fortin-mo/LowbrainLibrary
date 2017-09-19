package lowbrain.library.command;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

public abstract class CommandHandler implements CommandExecutor {
    private final JavaPlugin plugin;
    private final String command;
    private HashMap<String, Command> subs;
    private boolean ignoreCase = true;
    private Command.CommandStatus status;

    public CommandHandler(JavaPlugin plugin, String command) {
        this.plugin = plugin;
        this.command = command;
        this.subs = new HashMap<>();

        plugin.getCommand(this.command).setExecutor(this);
    }

    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase(this.command))
            return false;

        Command.CommandStatus result = Command.CommandStatus.VALID;
        String sub = null;

        if (args != null && args.length > 0)
            sub = ignoreCase ? args[0].toLowerCase() : args[0];

        if (sub != null && this.subs != null && this.subs.containsKey(sub)) {
            String[] n = new String[args.length - 1];
            System.arraycopy(args, 1, n, 0,n.length);
            result = this.subs.get(sub).call(sender, n);
        } else {
            result = this.execute(sender, args);
        }

        setStatus(result);

        return result == Command.CommandStatus.INVALID ? false : true;
    }

    public abstract Command.CommandStatus execute(CommandSender who, String[] args);

    @Contract("null, _ -> fail")
    public final void register(String cmd, Command executor) {
        if (cmd == null || cmd.trim().length() == 0 || executor == null)
            throw new NullArgumentException("Invalid arguments !");

        cmd = ignoreCase ? cmd.toLowerCase() : cmd;

        if (getSubs().containsKey(cmd))
            plugin.getLogger().warning("You have registered a same command twice. The first one will be overwriting");

        getSubs().put(cmd, executor);
    }

    @Contract("null -> fail")
    public final void unregister(String cmd) {
        if (cmd == null || cmd.trim().length() == 0 )
            throw new NullArgumentException("Invalid arguments !");

        cmd = ignoreCase ? cmd.toLowerCase() : cmd;

        if (getSubs().containsKey(cmd))
            getSubs().remove(cmd);
    }

    public HashMap<String, Command> getSubs() {
        if (subs == null)
            subs = new HashMap<>();
        return subs;
    }

    public Command.CommandStatus getStatus() {
        return status;
    }

    public void setStatus(Command.CommandStatus status) {
        this.status = status;
    }
}
