package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class PlayerSwapHandItemsListener implements Listener {

    private Spleef main;

    public PlayerSwapHandItemsListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event){
        if(main.getGameManager().getPhase() == SpleefPhase.GAME){
            event.setCancelled(true);
        }
    }
}
