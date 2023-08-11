package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.game.GameVariables;
import org.bukkit.entity.Player;
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

            Player player = event.getPlayer();
            CosmoxPlayer cosmoxPlayer = main.getAPI().getPlayer(player);
            cosmoxPlayer.addStatistic("blocksBreak", 1);
        }else{
            event.setCancelled(true);
        }
    }

}
