package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.lostaria.spleef.utils.TimeUtils;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.game.GameVariables;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimeTask extends BukkitRunnable {

    private Spleef main;

    private int timer = 0;

    public GameTimeTask(Spleef main){
        this.main = main;
    }

    @Override
    public void run() {

        if(main.getGameManager().getPhase() != SpleefPhase.GAME){
            cancel();
            return;
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            main.getAPI().getPlayer(pls).getScoreboard().updateTitle("§f§lSPLEEF §7" + TimeUtils.formatTime(timer));
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            if(main.getAPI().getTeamUtils().isInTeam(pls, Team.NO_TEAM)){
                CosmoxPlayer cosmoxPls = main.getAPI().getPlayer(pls);
                cosmoxPls.addStatistic(GameVariables.TIME_PLAYED, 1);
            }
        }

        timer++;
    }
}
