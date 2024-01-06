package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.chat.MessageBuilder;
import fr.worsewarn.cosmox.tools.chat.Messages;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class EntityDamageListener implements Listener {

    private Spleef main;

    public EntityDamageListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            event.setCancelled(true);
            return;
        }

        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(player.getHealth() <= event.getDamage()){
                event.setCancelled(true);
                player.setGameMode(GameMode.SPECTATOR);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.setFireTicks(0);
                player.setExp(0);
                player.setLevel(0);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                main.getGameManager().eliminePlayer(player);

                player.teleport(main.getGameManager().getMap().getLocation("spawn"));

                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f, 1f);

                MessageBuilder titleDeathBroadcastTitle = new MessageBuilder(main.getPrefix() + Messages.BROADCAST_DEATH_LANG).formatted(player.getName());
                for(Player pls : Bukkit.getOnlinePlayers()){
                    titleDeathBroadcastTitle.sendMessage(pls);
                    pls.playSound(pls.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 0.5f, 0.5f);
                }

                CosmoxPlayer cosmoxPlayer = main.getAPI().getPlayer(player);
                MessageBuilder consolationPrizeMolecules = new MessageBuilder("§c@lang/spleef.molecules_death/");
                cosmoxPlayer.addMolecules(4, consolationPrizeMolecules.toString(player));

                MessageBuilder titleDeathTitle = new MessageBuilder("@lang/spleef.title_death_title/");
                MessageBuilder subtitleDeathTitle = new MessageBuilder("@lang/spleef.title_death_subtitle/");
                player.sendTitle(ChatColor.RED + "§e☠ §c" + titleDeathTitle.toString(player) + " §e☠", ChatColor.GRAY + subtitleDeathTitle.toString(player), 20, 60, 20);

                main.getGameManager().checkWin();
            }
        }
    }
}
