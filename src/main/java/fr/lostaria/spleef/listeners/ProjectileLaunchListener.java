package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.tasks.IncrementPlayerSnowballTask;
import org.bukkit.Bukkit;
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
                if(snowballs >= 6){
                    IncrementPlayerSnowballTask incrementSnowballTask = new IncrementPlayerSnowballTask(main, player);
                    incrementSnowballTask.runTaskTimer(main, 20, 20);
                }
                if(snowballs <= 1){
                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            main.getGameManager().updatePlayerSnowballsInventory(player);
                        }
                    }, 1);
                }
            }
        }
    }

}
