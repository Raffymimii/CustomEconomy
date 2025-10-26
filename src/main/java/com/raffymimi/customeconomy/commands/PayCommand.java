package com.raffymimi.customeconomy.commands;

import com.raffymimi.customeconomy.EconomyManager;
import com.raffymimi.customeconomy.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PayCommand implements CommandExecutor {

    private final EconomyPlugin plugin;

    public PayCommand(EconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cOnly players can use this command.");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(plugin.getPrefix() + "§cUsage: /pay <player> <amount>");
            return true;
        }

        String targetName = args[0];
        String amountStr = args[1].replace(",", ".");

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-amount"));
                return true;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("invalid-amount"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayerIfCached(targetName);
        if (target == null) {
            Player onlineTarget = Bukkit.getPlayer(targetName);
            if (onlineTarget != null) {
                target = onlineTarget;
            }
        }

        if (target == null) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("player-not-found"));
            return true;
        }

        if (target.getUniqueId().equals(player.getUniqueId())) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("no-self-pay"));
            return true;
        }

        EconomyManager manager = plugin.getEconomyManager();
        manager.ensureInit(player.getUniqueId(), plugin.getStartBalance());
        manager.ensureInit(target.getUniqueId(), plugin.getStartBalance());

        if (!manager.hasBalance(player.getUniqueId(), amount)) {
            sender.sendMessage(plugin.getPrefix() + plugin.getMessage("insufficient-funds"));
            return true;
        }

        manager.withdraw(player.getUniqueId(), amount);
        manager.deposit(target.getUniqueId(), amount);

        double newBalanceSender = manager.getBalance(player.getUniqueId());
        double newBalanceTarget = manager.getBalance(target.getUniqueId());

        Map<String, String> placeholdersSender = new HashMap<>();
        placeholdersSender.put("player", target.getName());
        placeholdersSender.put("amount", plugin.formatCurrency(amount));
        placeholdersSender.put("newBalance", plugin.formatCurrency(newBalanceSender));

        sender.sendMessage(plugin.getPrefix() + plugin.getMessage("paid-sender", placeholdersSender));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

        if (target.isOnline() && target instanceof Player targetPlayer) {
            Map<String, String> placeholdersTarget = new HashMap<>();
            placeholdersTarget.put("player", player.getName());
            placeholdersTarget.put("amount", plugin.formatCurrency(amount));
            placeholdersTarget.put("newBalance", plugin.formatCurrency(newBalanceTarget));

            targetPlayer.sendMessage(plugin.getPrefix() + plugin.getMessage("paid-target", placeholdersTarget));
            targetPlayer.playSound(targetPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }

        return true;
    }
}