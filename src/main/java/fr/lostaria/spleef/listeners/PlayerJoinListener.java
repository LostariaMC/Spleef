package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.Phase;
import fr.worsewarn.cosmox.game.teams.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private Spleef main;

    public PlayerJoinListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(main.getAPI().getManager().getPhase() == Phase.GAME || main.getAPI().getManager().getPhase() == Phase.END){
            Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                @Override
                public void run() {
                    main.getAPI().getPlayer(player).setTeam(Team.SPEC);
                    player.teleport(main.getGameManager().getMap().getLocation("spawn"));
                }
            }, 3);
        }
    }
}
