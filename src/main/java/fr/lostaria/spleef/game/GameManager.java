package fr.lostaria.spleef.game;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.api.scoreboard.CosmoxScoreboard;
import fr.worsewarn.cosmox.game.Phase;
import fr.worsewarn.cosmox.game.events.GameStartEvent;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.map.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.Map;
import java.util.UUID;

public class GameManager {

    private Spleef main;

    private GameMap map;


    public GameManager(Spleef main){
        this.main = main;
    }

    public void startGame(GameStartEvent event){
        if(!event.getGame().equals(main.getGame())) return;
        map = event.getMap();

        main.getAPI().getManager().setPhase(Phase.GAME);

        for(Player pls : Bukkit.getOnlinePlayers()){
            pls.getInventory().clear();
            pls.updateInventory();
            for(PotionEffect effect : pls.getActivePotionEffects()) {
                pls.removePotionEffect(effect.getType());
            }

            if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                pls.setGameMode(GameMode.SURVIVAL);

                // TODO
            }else{
                pls.setGameMode(GameMode.SPECTATOR);
            }

            teleportPlayerToSpawnTeam(pls);

            pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            //main.getAPI().getPlayer(pls).setScoreboard(createScoreboard(pls));
        }

    }

    public void teleportPlayerToSpawnTeam(Player player){
        switch(main.getAPI().getTeamUtils().getTeam(player)){
            case RED:
                player.teleport(map.getLocation("spawnTeamRed"));
                break;

            case BLUE:
                player.teleport(map.getLocation("spawnTeamBlue"));
                break;

            case YELLOW:
                player.teleport(map.getLocation("spawnTeamYellow"));
                break;

            case GREEN:
                player.teleport(map.getLocation("spawnTeamGreen"));
                break;

            default:
                player.teleport(map.getLocation("center"));
        }
    }

    public CosmoxScoreboard createScoreboard(Player player){
        CosmoxScoreboard scoreboard = new CosmoxScoreboard(player);

        scoreboard.updateTitle("§f§lSPLEEF");
        scoreboard.updateLine(0, "§1 ");
        scoreboard.updateLine(1, "§7| §ePvP §f▪ §e05:00");
        scoreboard.updateLine(2, " §8» §2Emeraudes §f0");
        scoreboard.updateLine(3, " §8» §eBois §f0");
        scoreboard.updateLine(4, " §8» §ePierres §f0");
        scoreboard.updateLine(5, " §8» §eFers §f0");
        scoreboard.updateLine(6, " §8» §eDiamants §f0");
        scoreboard.updateLine(7, "§2 ");
        scoreboard.updateLine(8, "§7| §6Objectif §f▪ §e15000 pts");
        scoreboard.updateLine(9, " §8» §cRouge §7⬈ §e0 pts");
        scoreboard.updateLine(10, " §8» §9Bleu §7⬈ §e0 pts");
        scoreboard.updateLine(11, " §8» §eJaune §7⬈ §e0 pts");
        scoreboard.updateLine(12, " §8» §aVert §7⬈ §e0 pts");
        scoreboard.updateLine(13, "§3 ");
        scoreboard.updateLine(14, "§4 ");

        return scoreboard;
    }

    public GameMap getMap(){
        return map;
    }

}
