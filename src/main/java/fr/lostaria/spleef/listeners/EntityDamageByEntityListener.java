package fr.lostaria.spleef.listeners;

import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamageByEntity(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Snowball && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Vector vector = player.getLocation().toVector().subtract(event.getDamager().getLocation().toVector()).normalize().multiply(2).setY(0.6);
            player.setVelocity(vector);
        }
    }

}
