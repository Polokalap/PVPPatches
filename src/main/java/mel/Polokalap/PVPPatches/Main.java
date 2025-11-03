package mel.Polokalap.PVPPatches;

import mel.Polokalap.PVPPatches.Commands.PVPPatchesCommand;
import mel.Polokalap.PVPPatches.Listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Main extends JavaPlugin {

    private static Main instance;
    public Map<UUID, Float> attackCooldowns = new HashMap<>();

    @Override
    public void onEnable() {

        instance = this;

        getConfig().options().copyDefaults(true);
        saveConfig();

        register_stuff();

        getLogger().info(getConfig().getString("console.startup"));

        new BukkitRunnable() {

             @Override
            public void run() {

                 for (Player players : Bukkit.getOnlinePlayers()) {

                     attackCooldowns.put(players.getUniqueId(), players.getAttackCooldown());

                 }

             }

        }.runTaskTimer(this, 0L, 1L);

    }


    private void register_stuff() {

        // Command: getCommand("command").setExecutor(new Class());
        getCommand("pvppatches").setExecutor(new PVPPatchesCommand());

        // Listener: getServer().getPluginManager().registerEvents(new Class(), this);
        getServer().getPluginManager().registerEvents(new HitListener(), this);
        getServer().getPluginManager().registerEvents(new ShieldListener(), this);
        getServer().getPluginManager().registerEvents(new PearlListener(), this);

    }

    @Override
    public void onDisable() {

        getLogger().info(getConfig().getString("console.shutdown"));

    }

    public static Main getInstance() {

        return instance;

    }

}