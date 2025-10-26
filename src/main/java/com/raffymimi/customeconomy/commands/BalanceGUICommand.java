package com.raffymimi.customeconomy.commands;

import com.raffymimi.customeconomy.EconomyPlugin;
import com.raffymimi.customeconomy.gui.BalanceGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BalanceGUICommand implements CommandExecutor {

    private final EconomyPlugin plugin;
    private final BalanceGUI balanceGUI;

    public BalanceGUICommand(EconomyPlugin plugin) {
        this.plugin = plugin;
        this.balanceGUI = new BalanceGUI(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getPrefix() + "§cOnly players can use this command.");
            return true;
        }

        plugin.getEconomyManager().ensureInit(player.getUniqueId(), plugin.getStartBalance());
        balanceGUI.open(player);
        return true;
    }
}