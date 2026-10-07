/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 *  org.bukkit.entity.Player
 */
package rd.dru;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import rd.dru.PlayerManager;
import rd.dru.SuperHarvest;
import rd.dru.config.Config;

public class Commands
implements CommandExecutor,
TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player)sender;
        if (!p.hasPermission("superharvest.use")) {
            p.sendMessage(Config.color(PlayerManager.getLang((Player)p).perms));
            return true;
        }
        if (args.length < 1) {
            PlayerManager.toggle(p);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "toggle": {
                PlayerManager.toggle(p);
                break;
            }
            case "farming": {
                PlayerManager.toggle(p, PlayerManager.OptionType.Farming);
                break;
            }
            case "mining": {
                PlayerManager.toggle(p, PlayerManager.OptionType.Mining);
                break;
            }
            case "logging": {
                PlayerManager.toggle(p, PlayerManager.OptionType.Logging);
                break;
            }
            case "about": {
                p.sendMessage(ChatColor.YELLOW + Config.color(PlayerManager.getLang((Player)p).about.replace("{0}", "Dru_TNT")));
                p.sendMessage(ChatColor.AQUA + "DC: dru_tnt");
                break;
            }
            case "language": {
                if (args.length < 2 || !SuperHarvest.getSuperConfig().getLangs().contains(args[1]) && !args[1].equals("default")) break;
                PlayerManager.setLang(p, args[1]);
                break;
            }
            case "mode": {
                PlayerManager.toggleMode(p);
                break;
            }
            case "notify": {
                PlayerManager.toggleNotify(p);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length > 0 && args[0].equals("language")) {
            List<String> langs = SuperHarvest.getSuperConfig().getLangs().stream().collect(Collectors.toList());
            langs.add("default");
            return langs;
        }
        return Arrays.asList("toggle", "farming", "mining", "logging", "about", "notify", "mode", "language");
    }
}

