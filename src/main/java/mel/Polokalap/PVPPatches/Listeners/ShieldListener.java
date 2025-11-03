package mel.Polokalap.PVPPatches.Listeners;

import io.papermc.paper.event.player.PlayerShieldDisableEvent;
import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import mel.Polokalap.PVPPatches.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class ShieldListener implements Listener {

    private Main plugin = Main.getInstance();

    public boolean isFacing(Player enemy, Player player, double maxAngleDegrees) {

        Vector toPlayer = player.getLocation().toVector().subtract(enemy.getLocation().toVector()).normalize();

        Vector enemyDir = enemy.getLocation().getDirection().normalize();

        double dot = enemyDir.dot(toPlayer);

        double maxAngleRadians = Math.toRadians(maxAngleDegrees);
        double threshold = Math.cos(maxAngleRadians);

        return dot > threshold;

    }

    @EventHandler
    public void onDisable(PlayerShieldDisableEvent event) {

        if (!plugin.getConfig().getBoolean("settings.shield.enabled")) return;

        Player damager = (Player) event.getDamager();

        Location loc = damager.getLocation();

        damager.playSound(loc, Sound.ITEM_SHIELD_BREAK, Float.parseFloat(plugin.getConfig().getString("settings.shield.volume").replaceAll("%", "")) / 100, 0.3f);

    }

    @EventHandler
    public void onHit(PrePlayerAttackEntityEvent event) {

        if (!plugin.getConfig().getBoolean("settings.shield.enabled")) return;

        Entity attacked = event.getAttacked();
        Player player = event.getPlayer();

        if (!(attacked instanceof Player enemy)) {
            return;
        }

        if (
                enemy.isBlocking() &&
                        !player.getInventory().getItemInMainHand().equals(Material.WOODEN_AXE) &&
                        !player.getInventory().getItemInMainHand().equals(Material.STONE_AXE) &&
                        !player.getInventory().getItemInMainHand().equals(Material.IRON_AXE) &&
                        !player.getInventory().getItemInMainHand().equals(Material.DIAMOND_AXE) &&
                        !player.getInventory().getItemInMainHand().equals(Material.NETHERITE_AXE) &&
                        isFacing(enemy, player, 90)
        ) {

            Location loc = player.getLocation();
            Location loc1 = enemy.getLocation();

            player.playSound(loc, Sound.ITEM_SHIELD_BLOCK, Float.parseFloat(plugin.getConfig().getString("settings.shield.volume").replaceAll("%", "")) / 100, 1.0f);
            enemy.playSound(loc1, Sound.ITEM_SHIELD_BLOCK, Float.parseFloat(plugin.getConfig().getString("settings.shield.volume").replaceAll("%", "")) / 100, 1.0f);

        }

    }

}