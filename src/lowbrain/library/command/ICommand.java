package lowbrain.library.command;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ICommand implements Registrable, Permissible {
    protected HashMap<String, Command> subs = new HashMap<>();
    protected boolean ignoreCase = true;
    protected ArrayList<String> permissions = new ArrayList<>();
    protected boolean onlyPlayer = false;

    @Override
    public HashMap<String, Command> getSubs() {
        return subs;
    }

    @Override
    public HashMap<String, Command> setSubs(HashMap<String, Command> subs) {
        return this.subs = subs;
    }

    /**
     * set ignore case value
     * @param set value
     * @return return ignore case
     */
    @Override
    public boolean ignoreCase(boolean set) {
        return ignoreCase = set;
    }

    /**
     * is ignore case
     * @return is ignore case
     */
    @Override
    public boolean ignoreCase() {
        return ignoreCase;
    }

    /**
     * get permissions list
     * @return list of permissions
     */
    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean onlyPlayer() {
        return this.onlyPlayer;
    }

    @Override
    public boolean onlyPlayer(boolean set) {
        return this.onlyPlayer = set;
    }
}
