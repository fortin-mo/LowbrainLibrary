package lowbrain.library.main;

import lowbrain.library.command.Command;
import lowbrain.library.command.CommandHandler;
import lowbrain.library.config.YamlConfig;
import lowbrain.library.events.Listener;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

public class LowbrainLibrary extends JavaPlugin {
    public static LowbrainLibrary instance;
    public static YamlConfig config;

    private CommandHandler baseCmdHandler;

    @Override
    public void onEnable() {
        instance = this;

        config = new YamlConfig("config.yml", this, true);

        this.baseCmdHandler = new CommandHandler(this, "lb") {
            @Override
            public Command.CommandStatus execute(CommandSender who, String[] args) {
                return Command.CommandStatus.VALID;
            }
        };

        getServer().getPluginManager().registerEvents(new Listener(this), this);

    }

    public CommandHandler getBaseCmdHandler() {
        return baseCmdHandler;
    }

    @Contract(pure = true)
    public static LowbrainLibrary getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
