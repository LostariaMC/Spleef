package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.tools.chat.MessageBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

    private Spleef main;

    public PlayerInteractListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getItem() == null) return;
        if(event.getItem().getType() == Material.FIREWORK_STAR){
            MessageBuilder emptySnowballActionbar = new MessageBuilder("§c@lang/spleef.actionbar_snowball_empty/");
            emptySnowballActionbar.sendActionbar(player);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
        }
    }
}
