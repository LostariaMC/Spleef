package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.chat.MessageBuilder;
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
        if(!main.getAPI().getTeamUtils().isInTeam(player, Team.SPEC) && main.getGameManager().getPhase() == SpleefPhase.GAME){
            main.getGameManager().eliminePlayer(player);

            for(Player pls : Bukkit.getOnlinePlayers()){
                MessageBuilder titleDeathBroadcastTitle = new MessageBuilder(main.getPrefix() + Messages.BROADCAST_DEATH_LANG + " §7§o(@lang/spleef.message_pattern_disconnection/)").formatted(player.getName());
                titleDeathBroadcastTitle.sendMessage(pls);
                pls.playSound(pls.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 0.5f, 0.5f);
            }

            main.getGameManager().checkWin();
        }
    }
}
