package mel.Polokalap.PVPPatches.Commands;

import mel.Polokalap.PVPPatches.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PVPPatchesCommand implements CommandExecutor, TabCompleter {

    private Main plugin = Main.getInstance();
    private FileConfiguration config = plugin.getConfig();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!sender.hasPermission("pvppatches.admin")) {

            sender.sendMessage(config.getString("player.permission").replaceAll("&", "§"));
            return true;

        }

        if (args.length == 0) {

            sender.sendMessage(config.getString("player.args").replaceAll("&", "§"));

        }

        switch (args[0]) {

            case "reload":

                plugin.reloadConfig();
                config = plugin.getConfig();
                sender.sendMessage(config.getString("player.reloaded").replaceAll("&", "§"));

                break;

            default:

                sender.sendMessage(config.getString("player.args").replaceAll("&", "§"));

                break;

        }

        return true;

    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {

        if (!sender.hasPermission("pvppatches.admin")) return List.of();

        if (args.length == 1) return List.of("reload");

        return List.of();

    }

}
