package me.greaperc4.playerglow;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowCommand implements CommandExecutor {
    PotionEffectType effect = PotionEffectType.GLOWING;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        // in seconds
        int duration = 10;
        Player player = (Player) sender;

        if (!player.hasPermission("playerglow.use")) {
            player.sendMessage("You have no permission to use this command");
            return false;
        }

        if (args.length == 0) {
            if (player.hasPotionEffect(effect)) {
                player.removePotionEffect(effect);
                player.sendMessage("You have removed the glow effect");
            } else {
                this.addPotionEffect(player, duration);
                player.sendMessage(String.format("You have given yourself a glow effect for %s seconds", duration));
            }
        } else if (args.length == 1) {
            try {
                duration = Integer.parseInt(args[0]);

                if (!player.hasPermission("playerglow.length")) {
                    player.sendMessage("You have no permission to change the glow duration");
                    return false;
                }

                this.addPotionEffect(player, duration);

                player.sendMessage(String.format("You have given yourself a glow effect for %s seconds", duration));
                return true;
            } catch (NumberFormatException ignored) {
                // We do not report an error back since we check if the first argument is a player
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(String.format("Player '%s' not found", args[0]));
                return false;
            }

            if (!target.equals(player) && !player.hasPermission("playerglow.other")) {
                player.sendMessage("You have no permission to make other players glow");
                return false;
            }

            if (target.hasPotionEffect(effect)) {
                target.removePotionEffect(effect);
                player.sendMessage(String.format("You have removed the glow effect from %s", target.getName()));
            } else {
                this.addPotionEffect(target, duration);
                player.sendMessage(String.format("You have given %s a glow effect for %s seconds", target.getName(), duration));
            }
        } else if (args.length == 2) {
            if (!player.hasPermission("playerglow.other")) {
                player.sendMessage("You have no permission to make other players glow");
                return false;
            }

            if (!player.hasPermission("playerglow.length")) {
                player.sendMessage("You have no permission to change the glow duration for other players");
                return false;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(String.format("Player '%s' not found", args[0]));
                return false;
            }

            try {
                duration = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {
                player.sendMessage(String.format("Number '%s' is not a valid number", args[1]));
                return false;
            }

            this.addPotionEffect(target, duration);
            player.sendMessage(String.format("You have given %s a glow effect for %s seconds", target.getName(), duration));
        } else {
            player.sendMessage("Too many arguments");
            return false;
        }

        return true;
    }

    private void addPotionEffect(Player player, Integer duration) {
        player.addPotionEffect(new PotionEffect(effect, duration * 20, 0));
    }
}
