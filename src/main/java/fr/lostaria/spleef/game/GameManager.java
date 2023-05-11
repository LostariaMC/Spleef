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
import org.bukkit.potion.PotionEffectType;

public class GameManager {

    private Spleef main;

    private GameMap map;

    private SpleefPhase phase;


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
            }else{
                pls.setGameMode(GameMode.SPECTATOR);
            }

            pls.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 1, false, false));

            pls.teleport(map.getLocation("spawn"));

            pls.playSound(pls.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

            main.getAPI().getPlayer(pls).setScoreboard(createScoreboard(pls));
        }

    }

    public void preparationOver() {
        setPhase(SpleefPhase.GAME);
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                ItemStack shovel = new ItemStack(Material.DIAMOND_SHOVEL);
                pls.getInventory().setItem(0, shovel);
                pls.updateInventory();
                pls.sendMessage(main.getPrefix() + "§fEt c'est parti, à vos pelles !");
                pls.playSound(pls.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 1f);
            }
        }
    }

    public CosmoxScoreboard createScoreboard(Player player){
        CosmoxScoreboard scoreboard = new CosmoxScoreboard(player);

        scoreboard.updateTitle("§f§lSPLEEF");

        return scoreboard;
    }

    public GameMap getMap(){
        return map;
    }

    public SpleefPhase getPhase() {
        return phase;
    }

    public void setPhase(SpleefPhase phase) {
        this.phase = phase;
    }

}
