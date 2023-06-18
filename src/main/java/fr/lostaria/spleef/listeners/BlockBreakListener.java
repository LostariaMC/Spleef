package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {

    private Spleef main;

    public BlockBreakListener(Spleef main){
        this.main = main;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

        if(main.getGameManager().isLocationInLayers(event.getBlock().getLocation()) && main.getGameManager().getPhase() == SpleefPhase.GAME){
            event.setDropItems(false);
        }else{
            event.setCancelled(true);
        }
    }

}
