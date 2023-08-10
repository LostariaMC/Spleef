package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.teams.Team;
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
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setCancelled(true);
        }

        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(player.getHealth() <= event.getDamage()){
                event.setDamage(0);
                player.setGameMode(GameMode.SPECTATOR);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setSaturation(20);
                player.setFireTicks(0);
                player.setExp(0);
                player.setLevel(0);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);

                main.getAPI().getPlayer(player).setTeam(Team.SPEC);

                player.teleport(main.getGameManager().getMap().getLocation("spawn"));

                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 1f, 1f);

                for(Player pls : Bukkit.getOnlinePlayers()){
                    pls.sendMessage(main.getPrefix() + Messages.BROADCAST_DEATH.formatted(player.getName()));
                    pls.playSound(pls.getLocation(), Sound.ENTITY_VILLAGER_DEATH, 0.5f, 0.5f);
                }

                player.sendTitle(ChatColor.RED + "§e☠ §cVous êtes mort §e☠", ChatColor.GRAY + "Vous êtes désormais spectateur", 20, 60, 20);

                main.getGameManager().checkWin();
            }
        }
    }
}
