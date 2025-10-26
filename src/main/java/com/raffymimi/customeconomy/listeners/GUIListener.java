package com.raffymimi.customeconomy.listeners;

import com.raffymimi.customeconomy.EconomyPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class GUIListener implements Listener {

    private final EconomyPlugin plugin;

    public GUIListener(EconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null) return;
        
        String title = event.getView().getTitle();
        String guiTitle = plugin.getMessage("gui-title").replace("&", "§");
        
        if (title.equals(guiTitle)) {
            event.setCancelled(true);
            
            if (event.getCurrentItem() != null && event.getCurrentItem().getType().toString().equals("BARRIER")) {
                event.getWhoClicked().closeInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle() == null) return;
        
        String title = event.getView().getTitle();
        String guiTitle = plugin.getMessage("gui-title").replace("&", "§");
        
        if (title.equals(guiTitle)) {
            event.setCancelled(true);
        }
    }
}