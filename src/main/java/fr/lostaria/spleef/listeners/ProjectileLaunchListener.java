package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ProjectileLaunchListener implements Listener {

    private Spleef main;
    public ProjectileLaunchListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event){
        if(event.getEntity().getShooter() == null) return;
        if(event.getEntity().getShooter() instanceof Player){
            Player player = (Player) event.getEntity().getShooter();
            if(event.getEntity().getType() == EntityType.SNOWBALL){
                int snowballs = main.getGameManager().getSnowballsInventory().get(player);
                main.getGameManager().getSnowballsInventory().put(player, snowballs - 1);
                if(snowballs == 0){
                    main.getGameManager().updatePlayerSnowballsInventory(player);
                }
            }
        }
    }

}
