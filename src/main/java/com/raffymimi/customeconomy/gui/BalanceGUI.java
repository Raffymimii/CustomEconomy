package com.raffymimi.customeconomy.gui;

import com.raffymimi.customeconomy.EconomyPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class BalanceGUI {

    private final EconomyPlugin plugin;

    public BalanceGUI(EconomyPlugin plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        String title = plugin.getMessage("gui-title").replace("&", "§");
        Inventory inventory = Bukkit.createInventory(null, 27, title);

        fillGlass(inventory);

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta headMeta = (SkullMeta) playerHead.getItemMeta();
        headMeta.setOwningPlayer(player);
        headMeta.setDisplayName("§e§l" + player.getName());
        List<String> headLore = new ArrayList<>();
        headLore.add("");
        headLore.add("§7This is your profile");
        headLore.add("");
        headMeta.setLore(headLore);
        playerHead.setItemMeta(headMeta);

        inventory.setItem(4, playerHead);

        double balance = plugin.getEconomyManager().getBalance(player.getUniqueId());
        String formattedBalance = plugin.formatCurrency(balance);

        ItemStack balanceItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta balanceMeta = balanceItem.getItemMeta();
        balanceMeta.setDisplayName("§6§l" + plugin.getMessage("gui-balance-title").replace("&", "§"));
        
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§7" + plugin.getMessage("gui-current-balance").replace("&", "§") + " §e" + formattedBalance);
        lore.add("");
        balanceMeta.setLore(lore);
        balanceItem.setItemMeta(balanceMeta);

        inventory.setItem(13, balanceItem);

        ItemStack infoItem = new ItemStack(Material.BOOK);
        ItemMeta infoMeta = infoItem.getItemMeta();
        infoMeta.setDisplayName("§b§l" + plugin.getMessage("gui-info-title").replace("&", "§"));
        
        List<String> infoLore = new ArrayList<>();
        infoLore.add("");
        infoLore.add("§7" + plugin.getMessage("gui-info-line1").replace("&", "§"));
        infoLore.add("§7" + plugin.getMessage("gui-info-line2").replace("&", "§"));
        infoLore.add("§7" + plugin.getMessage("gui-info-line3").replace("&", "§"));
        infoLore.add("");
        infoMeta.setLore(infoLore);
        infoItem.setItemMeta(infoMeta);

        inventory.setItem(11, infoItem);

        ItemStack closeItem = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = closeItem.getItemMeta();
        closeMeta.setDisplayName("§c§l" + plugin.getMessage("gui-close").replace("&", "§"));
        closeItem.setItemMeta(closeMeta);

        inventory.setItem(22, closeItem);

        player.openInventory(inventory);
    }

    private void fillGlass(Inventory inventory) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        int[] slots = {0, 1, 2, 3, 5, 6, 7, 8, 9, 10, 12, 14, 15, 16, 17, 18, 19, 20, 21, 23, 24, 25, 26};
        for (int slot : slots) {
            inventory.setItem(slot, glass);
        }
    }
}