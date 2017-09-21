package lowbrain.library.command;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public abstract class CommandHandler extends ICommand implements CommandExecutor  {
    private JavaPlugin plugin;
    private String command;
    private Command.CommandStatus status;

    /**
     * constructor for CommandHandler
     * @param plugin JavaPlugin
     * @param command command name
     */
    public CommandHandler(JavaPlugin plugin, String command) {
        this.plugin = plugin;
        this.command = command;

        plugin.getCommand(this.command).setExecutor(this);
    }

    @Override
    public final boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(this.command))
            return false;

        if (this.onlyPlayer() && !(sender instanceof Player)) {
            sender.sendMessage("Command only accessible by players !");
            return true;
        }

        if (!hasPermission(sender)) {
            sender.sendMessage("Insufficient permission !");
            return true;
        }

        Command.CommandStatus result = Command.CommandStatus.VALID;
        String sub = null;

        if (args != null && args.length > 0)
            sub = ignoreCase ? args[0].toLowerCase() : args[0];

        if (sub != null && this.subs != null && this.subs.containsKey(sub)) {
            String[] n = new String[args.length - 1];
            System.arraycopy(args, 1, n, 0,n.length);
            result = this.subs.get(sub).call(sender, n, sub);
        } else {
            result = this.execute(sender, args);
        }

        setStatus(result);

        return result == Command.CommandStatus.INVALID ? false : true;
    }

    /**
     * execute command
     * @param who command sender
     * @param args list of arguments
     * @return CommandStatus
     */
    public abstract Command.CommandStatus execute(CommandSender who, String[] args);

    public HashMap<String, Command> getSubs() {
        return subs;
    }

    public Command.CommandStatus getStatus() {
        return status;
    }

    public void setStatus(Command.CommandStatus status) {
        this.status = status;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}
