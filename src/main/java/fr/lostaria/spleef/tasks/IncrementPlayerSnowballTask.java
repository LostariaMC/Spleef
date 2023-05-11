package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class IncrementPlayerSnowballTask extends BukkitRunnable {

    private Spleef main;

    private int initTimer = 4;
    private int timer = initTimer;

    public IncrementPlayerSnowballTask(Spleef main){
        this.main = main;
    }

    @Override
    public void run() {
        if(timer == 0){
            for(Player pls : Bukkit.getOnlinePlayers()){
                if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                    int snowballs = main.getGameManager().getSnowballsInventory().get(pls);
                    if(snowballs < 6){
                        main.getGameManager().getSnowballsInventory().put(pls, snowballs + 1);
                        main.getGameManager().updatePlayerSnowballsInventory(pls);
                    }
                }
            }
            timer = initTimer + 1;
        }
        timer--;
    }

}
