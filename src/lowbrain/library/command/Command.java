package lowbrain.library.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public abstract class Command extends ICommand {
    protected String command;
    protected CommandStatus status = CommandStatus.VALID;

    public Command (String command) {
        this.command = command;
    }

    public final CommandStatus call(CommandSender who, String[] args, String cmd) {
        String sub = null;

        if (this.onlyPlayer() && !(who instanceof Player)) {
            who.sendMessage("Command only accessible by players !");
            return CommandStatus.VALID;
        }

        if (!hasPermission(who)) {
            who.sendMessage("Insufficient permission !");
            return CommandStatus.INSUFFICIENT_PERMISSION;
        }

        if (args != null && args.length > 0)
            sub = ignoreCase ? args[0].toLowerCase() : args[0];

        if (sub != null && this.subs != null && this.subs.containsKey(sub)) {
            String[] n = new String[args.length - 1];
            System.arraycopy(args, 1, n, 0,n.length);
            return this.subs.get(sub).call(who, n, cmd);
        }

        return this.execute(who, args, cmd);
    }

    public abstract CommandStatus execute(CommandSender who, String[] args, String cmd);

    public CommandStatus getStatus() {
        return status;
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
