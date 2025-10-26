package com.raffymimi.customeconomy.commands;

import com.raffymimi.customeconomy.EconomyManager;
import com.raffymimi.customeconomy.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BalanceCommand implements CommandExecutor {

    private final EconomyPlugin plugin;

    public BalanceCommand(EconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage(plugin.getPrefix() + "§cOnly players can check their own balance.");
                return true;
            }

            showBalance(sender, player.getUniqueId(), player.getName(), true);
            return true;
        }

        if (!sender.hasPermission("customecon.balance.others")) {
            sender.sendMessage(plugin.getPrefix() + "§cYou don't have permission to check other players' balances.");
            return true;
        }

        String targetName = args[0];
        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);

        if (target == null) {
            Player onlineTarget = Bukkit.getPlayer(targetName);
            if (onlineTarget != null) {
                target = onlineTarget;
            }
        }

        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("player-not-found"));
            return true;
        }

        showBalance(sender, target.getUniqueId(), target.getName(), false);
        return true;
    }

    private void showBalance(CommandSender sender, UUID uuid, String playerName, boolean isSelf) {
        EconomyManager manager = plugin.getEconomyManager();
        manager.ensureInit(uuid, plugin.getStartBalance());

        double balance = manager.getBalance(uuid);
        String formattedAmount = plugin.formatCurrency(balance);

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("amount", formattedAmount);
        placeholders.put("player", playerName);

        String messageKey = isSelf ? "balance-self" : "balance-others";
        String message = plugin.getMessage(messageKey, placeholders);

        sender.sendMessage(plugin.getPrefix() + message);
    }
}