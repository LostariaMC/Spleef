package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.chat.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
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
            main.getGameManager().eliminePlayer(player);

            for(Player pls : Bukkit.getOnlinePlayers()){
                pls.sendMessage(main.getPrefix() + Messages.BROADCAST_DEATH.formatted(player.getName()) + " §7§o(déconnexion)");
                pls.playSound(pls.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 0.5f, 0.5f);
            }

            main.getGameManager().checkWin();
        }
    }
}
