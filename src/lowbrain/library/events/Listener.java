package lowbrain.library.events;

import lowbrain.library.main.LowbrainLibrary;
import org.bukkit.entity.Animals;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class Listener implements org.bukkit.event.Listener {
    public static LowbrainLibrary plugin;

    public Listener(LowbrainLibrary instance) {
        plugin = instance;
    }

    /**
     * called when create spawn in the world
     * we only set no tick damage if needed
     * @param e CreatureSpawnEvent
     */
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent e){

        if (plugin.config.getBoolean("features.disable_no_tick_damange", true))
            e.getEntity().setNoDamageTicks(0);

        // animal spawned from breeding
        if (e.getEntity() instanceof Animals
                && e.getSpawnReason() == CreatureSpawnEvent.SpawnReason.BREEDING) {

            double limit = plugin.config.getDouble("features.animals.reduce_spawn_from_breeding", 0.25);

            if (limit < Math.random())
                e.setCancelled(true); // cancel spawning
        }
    }
}
