package lowbrain.library.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;

public interface Permissible {

    boolean onlyPlayer();
    boolean onlyPlayer(boolean set);

    /**
     * check if command sender has permission
     * @param sender command sender
     * @return true if has permission
     */
    default boolean hasPermission(CommandSender sender) {
        if (getPermissions() == null || getPermissions().size() == 0)
            return true;

        boolean has = true;

        for (String perm : getPermissions()) {
            if (!has)
                break;

            has = sender.hasPermission(perm);
        }

        return has;
    }

    /**
     * unregister a permission
     * @param perm permission
     */
    default void removePermission(String perm) {
        if (getPermissions().indexOf(perm) >= 0)
            getPermissions().remove(perm);
    }

    /**
     * unregister multiples permissions
     * @param perms list of permissions
     */
    default void removePermissions(ArrayList<String> perms) {
        perms.forEach(s -> removePermission(s));
    }

    /**
     * unregister multiples permissions
     * @param perms list of permissions
     */
    default void removePermissions(Collection<String> perms) {
        perms.forEach(s -> removePermission(s));
    }

    /**
     * register multiples permissions
     * @param perms list of permissions
     */
    default void addPermissions(ArrayList<String> perms) {
        perms.forEach(s -> addPermission(s));
    }

    /**
     * register multiples permissions
     * @param perms list of permissions
     */
    default void addPermissions(Collection<String> perms) {
        perms.forEach(s -> addPermission(s));
    }

    /**
     * register a single permission
     * @param perm permission
     */
    default void addPermission(String perm) {
        if (getPermissions().indexOf(perm) < 0)
            getPermissions().add(perm);
    }

    ArrayList<String> getPermissions();
}
