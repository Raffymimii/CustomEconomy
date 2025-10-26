package com.raffymimi.customeconomy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class EconomyManager {

    private final EconomyPlugin plugin;
    private final Map<UUID, Double> balances;
    private final File balancesFile;

    public EconomyManager(EconomyPlugin plugin) {
        this.plugin = plugin;
        this.balances = new ConcurrentHashMap<>();
        this.balancesFile = new File(plugin.getDataFolder(), "balances.yml");
    }

    public void load() {
        if (!balancesFile.exists()) {
            try {
                balancesFile.getParentFile().mkdirs();
                balancesFile.createNewFile();
                plugin.getLogger().info("Created new balances.yml file");
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to create balances.yml: " + e.getMessage());
                return;
            }
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(balancesFile);
        balances.clear();

        for (String key : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(key);
                double balance = config.getDouble(key);
                balances.put(uuid, sanitize(balance));
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid UUID in balances.yml: " + key);
            }
        }

        plugin.getLogger().info("Loaded " + balances.size() + " balances from balances.yml");
    }

    public void save() {
        FileConfiguration config = new YamlConfiguration();

        for (Map.Entry<UUID, Double> entry : balances.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            config.save(balancesFile);
            plugin.getLogger().info("Saved " + balances.size() + " balances to balances.yml");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save balances.yml: " + e.getMessage());
        }
    }

    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, 0.0);
    }

    public void setBalance(UUID uuid, double amount) {
        balances.put(uuid, sanitize(amount));
    }

    public boolean deposit(UUID uuid, double amount) {
        if (amount <= 0) {
            return false;
        }
        double newBalance = getBalance(uuid) + amount;
        setBalance(uuid, newBalance);
        return true;
    }

    public boolean withdraw(UUID uuid, double amount) {
        if (amount <= 0) {
            return false;
        }
        double currentBalance = getBalance(uuid);
        if (currentBalance < amount) {
            return false;
        }
        setBalance(uuid, currentBalance - amount);
        return true;
    }

    public boolean hasBalance(UUID uuid, double amount) {
        return getBalance(uuid) >= amount;
    }

    public void ensureInit(UUID uuid, double startBalance) {
        if (!balances.containsKey(uuid)) {
            setBalance(uuid, startBalance);
        }
    }

    private double sanitize(double amount) {
        double clamped = Math.max(0, amount);
        BigDecimal bd = new BigDecimal(Double.toString(clamped));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}