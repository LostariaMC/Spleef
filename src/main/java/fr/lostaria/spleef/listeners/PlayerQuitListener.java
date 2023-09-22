package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private Spleef main;

    public PlayerQuitListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(!main.getAPI().getTeamUtils().isInTeam(player, Team.SPEC)){
            main.getAPI().getPlayer(player).setTeam(Team.SPEC);
        }
    }
}
