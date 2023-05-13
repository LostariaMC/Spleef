package fr.lostaria.spleef.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Snowball && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Vector kbDirection = event.getDamager().getLocation().getDirection().multiply(0.5).setY(0.5);
            player.setVelocity(kbDirection);
        }
    }

}
