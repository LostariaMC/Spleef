package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class DamageTask extends BukkitRunnable {

    private Spleef main;

    public DamageTask(Spleef main){
        this.main = main;
    }

    @Override
    public void run() {
        if(main.getGameManager().getPhase() != SpleefPhase.GAME) return;
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(main.getAPI().getTeamUtils().isInTeam(pls, Team.NO_TEAM) && pls.getLocation().getY() < (main.getGameManager().getLayerLocations(main.getGameManager().getLayers().size() - 1).get(0).getY() - 10)){
                pls.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10, 1));
                pls.damage(3);
            }
        }
    }

}
