package mel.Polokalap.PVPPatches.Listeners;

import io.papermc.paper.event.player.PlayerArmSwingEvent;
import mel.Polokalap.PVPPatches.Main;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HitListener implements Listener {

    private final Main plugin = Main.getInstance();

    private Map<UUID, ItemStack> playerItems = new HashMap<>();

    @EventHandler
    public void onArmSwing(PlayerArmSwingEvent event) {

        if (!plugin.getConfig().getBoolean("settings.offhand_swing_animation_patch.enabled")) return;

        Player player = event.getPlayer();

        float storedCooldown = plugin.attackCooldowns.getOrDefault(player.getUniqueId(), 1.0F);

        if (event.getHand() == EquipmentSlot.OFF_HAND) {

            if (storedCooldown < 0.5f) return;

            AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attackSpeed == null) return;

            attackSpeed.setBaseValue(1000.0f);
            playerItems.put(player.getUniqueId(), player.getInventory().getItemInMainHand());

            new BukkitRunnable() {

                @Override
                public void run() {

                    if (player.getInventory().getItemInMainHand().isSimilar(playerItems.get(player.getUniqueId()))) return;

                    AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

                    if (attackSpeed != null) {

                        attackSpeed.setBaseValue(attackSpeed.getDefaultValue());

                    }

                    playerItems.remove(player.getUniqueId());
                    cancel();

                }

            }.runTaskTimer(plugin, 0L, 1L);

        } else {

            ItemStack storedItem = playerItems.get(player.getUniqueId());
            if (storedItem == null) return;

            AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
            if (attackSpeed == null) return;

            attackSpeed.setBaseValue(attackSpeed.getDefaultValue());

        }

    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {

        if (!plugin.getConfig().getBoolean("settings.offhand_swing_animation_patch.enabled")) return;

        Player player = event.getPlayer();

        AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);

        if (attackSpeed != null) {

            attackSpeed.setBaseValue(attackSpeed.getDefaultValue());

        }

        playerItems.remove(player.getUniqueId());

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        playerItems.remove(player.getUniqueId());

        AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attackSpeed == null) return;

        attackSpeed.setBaseValue(attackSpeed.getDefaultValue());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        playerItems.remove(player.getUniqueId());

        AttributeInstance attackSpeed = player.getAttribute(Attribute.GENERIC_ATTACK_SPEED);
        if (attackSpeed == null) return;

        attackSpeed.setBaseValue(attackSpeed.getDefaultValue());

    }

}