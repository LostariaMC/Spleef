package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    private Spleef main;

    public ProjectileHitListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onProjectileHit (ProjectileHitEvent event){
        Projectile projectile = event.getEntity();
        if(projectile.getType() == EntityType.SNOWBALL){
            Block hitBlock = event.getHitBlock();
            if(hitBlock != null && main.getGameManager().isLocationInLayers(hitBlock.getLocation())){
                hitBlock.setType(Material.AIR);
                hitBlock.getWorld().playEffect(hitBlock.getLocation(), Effect.SMOKE, 1);
                for(Player pls : Bukkit.getOnlinePlayers()){
                    pls.playSound(hitBlock.getLocation(), Sound.BLOCK_LAVA_EXTINGUISH, 0.5f, 2f);
                }
            }
        }
    }
}
