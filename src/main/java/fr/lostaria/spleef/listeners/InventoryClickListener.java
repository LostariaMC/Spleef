package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    private Spleef main;

    public InventoryClickListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        if(main.getGameManager().getPhase() == SpleefPhase.GAME){
            event.setCancelled(true);
        }
    }

}
