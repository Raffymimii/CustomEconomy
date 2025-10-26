package com.raffymimi.customeconomy;

import com.raffymimi.customeconomy.commands.BalanceCommand;
import com.raffymimi.customeconomy.commands.BalanceGUICommand;
import com.raffymimi.customeconomy.commands.PayCommand;
import com.raffymimi.customeconomy.listeners.GUIListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class EconomyPlugin extends JavaPlugin {

    private EconomyManager economyManager;
    private String currencySymbol;
    private boolean currencyDecimals;
    private double startBalance;
    private Map<String, String> messages;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfiguration();

        economyManager = new EconomyManager(this);
        economyManager.load();

        for (Player player : Bukkit.getOnlinePlayers()) {
            economyManager.ensureInit(player.getUniqueId(), startBalance);
        }

        registerCommands();
        registerListeners();

        getLogger().info("CustomEconomy has been enabled!");
    }

    @Override
    public void onDisable() {
        if (economyManager != null) {
            economyManager.save();
        }
        getLogger().info("CustomEconomy has been disabled!");
    }

    private void loadConfiguration() {
        FileConfiguration config = getConfig();
        currencySymbol = config.getString("currency.symbol", "$");
        currencyDecimals = config.getBoolean("currency.decimals", true);
        startBalance = config.getDouble("start-balance", 100.0);

        messages = new HashMap<>();
        if (config.getConfigurationSection("messages") != null) {
            for (String key : config.getConfigurationSection("messages").getKeys(false)) {
                messages.put(key, config.getString("messages." + key, ""));
            }
        }
    }

    private void registerCommands() {
        PluginCommand balanceCmd = getCommand("balance");
        if (balanceCmd != null) {
            balanceCmd.setExecutor(new BalanceCommand(this));
        }

        PluginCommand payCmd = getCommand("pay");
        if (payCmd != null) {
            payCmd.setExecutor(new PayCommand(this));
        }

        PluginCommand balanceGuiCmd = getCommand("balancegui");
        if (balanceGuiCmd != null) {
            balanceGuiCmd.setExecutor(new BalanceGUICommand(this));
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
    }

    public EconomyManager getEconomyManager() {
        return economyManager;
    }

    public String formatCurrency(double amount) {
        if (currencyDecimals) {
            return String.format("%s%.2f", currencySymbol, amount);
        } else {
            return String.format("%s%.0f", currencySymbol, amount);
        }
    }

    public String getMessage(String key) {
        return translateColor(messages.getOrDefault(key, ""));
    }

    public String getMessage(String key, Map<String, String> placeholders) {
        String message = getMessage(key);
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            message = message.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return message;
    }

    public String getPrefix() {
        return getMessage("prefix");
    }

    public double getStartBalance() {
        return startBalance;
    }

    private String translateColor(String text) {
        return text.replace("&", "§");
    }
}