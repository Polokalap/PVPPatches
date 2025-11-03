package mel.Polokalap.PVPPatches.Listeners;

import mel.Polokalap.PVPPatches.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class PearlListener implements Listener {

    private Main plugin = Main.getInstance();

    private final Map<UUID, Vector> velocities = new HashMap<>();
    private final Set<UUID> thrown = new HashSet<>();

    @EventHandler
    public void onEnderPearlHitGround(PlayerTeleportEvent event) {

        if (!plugin.getConfig().getBoolean("settings.enderpearl.enabled")) return;

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;

        Player player = event.getPlayer();
        UUID id = player.getUniqueId();

        thrown.remove(id);
        Vector velocity = velocities.remove(id);
        if (velocity == null) return;

        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setVelocity(velocity);
        });

    }

    @EventHandler
    public void onThrow(ProjectileLaunchEvent event) {

        if (!plugin.getConfig().getBoolean("settings.enderpearl.enabled")) return;

        if (event.getEntity() instanceof EnderPearl pearl) {

            if (pearl.getShooter() instanceof Player player) {

                UUID id = player.getUniqueId();
                thrown.add(id);

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (!thrown.contains(id)) {

                            cancel();
                            return;

                        }

                        velocities.put(id, player.getVelocity());

                    }

                }.runTaskTimer(plugin, 0L, 1L);

                new BukkitRunnable() {

                    @Override
                    public void run() {

                        if (pearl.isDead() || pearl.isOnGround()) {
                            cancel();
                            return;
                        }

                        double radius = plugin.getConfig().getDouble("settings.enderpearl.hitbox_expansion");
                        for (Entity nearby : pearl.getNearbyEntities(radius, radius, radius)) {

                            if (nearby.getType() == EntityType.WIND_CHARGE) {

                                pearl.wouldCollideUsing(nearby.getBoundingBox());

                                cancel();
                                break;

                            }

                        }

                    }

                }.runTaskTimer(plugin, 10L, 1L);

            }

        }

    }

}