package fr.lostaria.spleef.game;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.tasks.DamageTask;
import fr.lostaria.spleef.tasks.DestructionLayersTask;
import fr.lostaria.spleef.tasks.GameTimeTask;
import fr.lostaria.spleef.tasks.IncrementPlayerSnowballTask;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.api.scoreboard.CosmoxScoreboard;
import fr.worsewarn.cosmox.game.GameVariables;
import fr.worsewarn.cosmox.game.Phase;
import fr.worsewarn.cosmox.game.events.GameStartEvent;
import fr.worsewarn.cosmox.game.teams.Team;
import fr.worsewarn.cosmox.tools.chat.Messages;
import fr.worsewarn.cosmox.tools.map.GameMap;
import fr.worsewarn.cosmox.tools.utils.Pair;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameManager {

    private Spleef main;

    private GameMap map;

    private SpleefPhase phase;

    private Map<Player, Integer> snowballsInventory = new HashMap<>();

    private List<Pair<Location, Location>> layers;
    private List<List<Location>> layersLocations = new ArrayList<>();


    public GameManager(Spleef main){
        this.main = main;
    }

    public void startGame(GameStartEvent event){
        if(!event.getGame().equals(main.getGame())) return;
        map = event.getMap();

        main.getAPI().getManager().setPhase(Phase.GAME);

        layers = main.getAPI().getManager().getMap().getCuboids("layers");

        for(Pair<Location, Location> layer : layers){
            Location loc1 = layer.getLeft();
            Location loc2 = layer.getRight();

            int minX = Math.min(loc1.getBlockX(), loc2.getBlockX());
            int minY = Math.min(loc1.getBlockY(), loc2.getBlockY());
            int minZ = Math.min(loc1.getBlockZ(), loc2.getBlockZ());

            int maxX = Math.max(loc1.getBlockX(), loc2.getBlockX());
            int maxY = Math.max(loc1.getBlockY(), loc2.getBlockY());
            int maxZ = Math.max(loc1.getBlockZ(), loc2.getBlockZ());

            int x = minX;
            int y = minY;
            int z = minZ;

            List<Location> blockLocations = new ArrayList<>();
            for (int i = minX; i <= maxX; i++) {
                for (int j = minY; j <= maxY; j++) {
                    for (int k = minZ; k <= maxZ; k++) {
                        Location l = new Location(loc1.getWorld(), i, j, k);
                        blockLocations.add(l);
                    }
                }
            }
            layersLocations.add(blockLocations);
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            pls.getInventory().clear();
            pls.updateInventory();
            for(PotionEffect effect : pls.getActivePotionEffects()) {
                pls.removePotionEffect(effect.getType());
            }

            CosmoxPlayer cosmoxPlayer = main.getAPI().getPlayer(pls);

            if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                pls.setGameMode(GameMode.SURVIVAL);
                snowballsInventory.put(pls, 0);
                main.getAPI().getPlayer(pls).setTeam(Team.NO_TEAM);
                cosmoxPlayer.addStatistic(GameVariables.GAMES_PLAYED, 1);
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
                ItemMeta shovelMeta = shovel.getItemMeta();
                shovelMeta.setUnbreakable(true);
                shovel.setItemMeta(shovelMeta);
                shovel.addEnchantment(Enchantment.DIG_SPEED, 5);
                pls.getInventory().setItem(0, shovel);

                updatePlayerSnowballsInventory(pls);

                pls.sendMessage(main.getPrefix() + "§fEt c'est parti, à vos pelles !");
                pls.playSound(pls.getLocation(), Sound.ITEM_GOAT_HORN_SOUND_0, 1f, 1f);
            }
        }

        for(Player pls : Bukkit.getOnlinePlayers()){
            if(main.getAPI().getTeamUtils().isInTeam(pls, Team.NO_TEAM)){
                IncrementPlayerSnowballTask incrementSnowballTask = new IncrementPlayerSnowballTask(main, pls);
                incrementSnowballTask.runTaskTimer(main, 20, 20);
            }
        }

        DestructionLayersTask destructionLayersTask = new DestructionLayersTask(main);
        destructionLayersTask.runTaskTimer(main, 20, 20);

        DamageTask damageTask = new DamageTask(main);
        damageTask.runTaskTimer(main, 10, 10);

        GameTimeTask gameTimeTask = new GameTimeTask(main);
        gameTimeTask.runTaskTimer(main, 20, 20);
    }

    public void eliminePlayer(Player player){
        main.getAPI().getPlayer(player).setTeam(Team.SPEC);
        for(Player pls : Bukkit.getOnlinePlayers()){
            main.getAPI().getPlayer(pls).getScoreboard().updateLine(1, "§7| " + net.md_5.bungee.api.ChatColor.of("#E8AA14") + "Joueurs restants §f━ " + net.md_5.bungee.api.ChatColor.of("#E8AA14") + getNbPlayersInGame());
        }
    }

    public void checkWin(){
        int players = 0;
        Player winner = null;
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                players++;
                winner = pls;
            }
        }

        if(players == 1){
            win(winner);
        }
    }

    public int getNbPlayersInGame(){
        int players = 0;
        for(Player pls : Bukkit.getOnlinePlayers()){
            if(!main.getAPI().getTeamUtils().isInTeam(pls, Team.SPEC)){
                players++;
            }
        }
        return players;
    }

    public void win(Player player){
        setPhase(SpleefPhase.FINISH);

        player.getInventory().clear();
        player.setGameMode(GameMode.CREATIVE);

        for(Player pls : Bukkit.getOnlinePlayers()){
            pls.playSound(pls.getLocation(), Sound.MUSIC_DISC_STAL, SoundCategory.AMBIENT, 20f, 1.4f);
            pls.sendMessage(main.getPrefix() + Messages.BROADCAST_WIN.formatted(player.getName()));
        }

        CosmoxPlayer cosmoxPlayer = main.getAPI().getPlayer(player);
        cosmoxPlayer.addMolecules(8, "§aVictoire");
        cosmoxPlayer.addStatistic(GameVariables.WIN, 1);

        main.getAPI().getManager().setPhase(Phase.END);

        main.getAPI().getManager().getGame().addToResume(Messages.SUMMARY_WIN.formatted(player.getName()));
    }

    public CosmoxScoreboard createScoreboard(Player player){
        CosmoxScoreboard scoreboard = new CosmoxScoreboard(player);
        scoreboard.updateTitle("§f§lSPLEEF §7--:--");
        scoreboard.updateLine(0, "§8");
        scoreboard.updateLine(1, "§7| " + net.md_5.bungee.api.ChatColor.of("#E8AA14") + "Joueurs restants §f━ " + net.md_5.bungee.api.ChatColor.of("#E8AA14") + getNbPlayersInGame());
        scoreboard.updateLine(2, "§d");
        scoreboard.updateLine(3, "§a");
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

    public Map<Player, Integer> getSnowballsInventory() {
        return snowballsInventory;
    }

    public void updatePlayerSnowballsInventory(Player player){
        if(!snowballsInventory.containsKey(player)) return;
        int snowballs = snowballsInventory.get(player);
        if(snowballs <= 0){
            player.getInventory().setItemInOffHand(new ItemStack(Material.FIREWORK_STAR, 1));
        }else{
            player.getInventory().setItemInOffHand(new ItemStack(Material.SNOWBALL, snowballs));
        }
        player.updateInventory();
    }

    public List<Pair<Location, Location>> getLayers() {
        return layers;
    }

    public List<List<Location>> getLayersLocations() {
        return layersLocations;
    }

    public List<Location> getLayerLocations(int layer){
        return layersLocations.get(layer);
    }

    public boolean isLocationInLayers(Location location){
        for(List<Location> layer : layersLocations){
            if(layer.contains(location)) return true;
        }
        return false;
    }

}
