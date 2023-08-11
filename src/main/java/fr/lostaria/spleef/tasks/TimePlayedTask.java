package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.game.GameVariables;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TimePlayedTask extends BukkitRunnable {

    private Spleef main;

    public TimePlayedTask(Spleef main){
        this.main = main;
    }

    @Override
    public void run() {
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(main.getAPI().getTeamUtils().isInTeam(pls,Team.NO_TEAM)){
                CosmoxPlayer cosmoxPls = main.getAPI().getPlayer(pls);
                cosmoxPls.addStatistic(GameVariables.TIME_PLAYED, 1);
            }
        }

        if(main.getGameManager().getPhase() != SpleefPhase.FINISH){
            cancel();
        }
    }

}
