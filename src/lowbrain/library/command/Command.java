package lowbrain.library.command;

import org.bukkit.command.CommandSender;

import java.util.HashMap;

public abstract class Command {
    protected String command;
    protected CommandStatus status = CommandStatus.VALID;
    protected HashMap<String, Command> subs;
    protected boolean ignoreCase = true;

    public Command (String command) {
        this.command = command;
        this.subs = new HashMap<>();
    }

    public final CommandStatus call(CommandSender who, String[] args) {
        String sub = null;

        if (args != null && args.length > 0)
            sub = ignoreCase ? args[0].toLowerCase() : args[0];

        if (sub != null && this.subs != null && this.subs.containsKey(sub)) {
            String[] n = new String[args.length - 1];
            System.arraycopy(args, 1, n, 0,n.length);
            return this.subs.get(sub).call(who, n);
        }

        return this.execute(who, args);
    }

    public abstract CommandStatus execute(CommandSender who, String[] args);

    public CommandStatus getStatus() {
        return status;
    }

    public HashMap<String, Command> getSubs() {
        if (subs == null)
            subs = new HashMap<>();
        return subs;
    }

    public String getCommand() {
        return command;
    }

    public enum CommandStatus {
        FAILED,
        INVALID,
        VALID,
        INSUFFICIENT_PERMISSION
    }
}


