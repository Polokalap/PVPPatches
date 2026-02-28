package mel.Polokalap.PVPPatches.Listeners;

import mel.Polokalap.PVPPatches.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DrainListener implements Listener {

    private Main plugin = Main.getInstance();

    @EventHandler
    public void onDrain(PlayerBucketFillEvent event) {

        if (!plugin.getConfig().getBoolean("settings.drain.enabled")) return;

        Player player = event.getPlayer();

        new BukkitRunnable() {

            @Override
            public void run() {

                player.updateInventory();

            }

        }.runTaskLater(plugin, 3L);

    }

}