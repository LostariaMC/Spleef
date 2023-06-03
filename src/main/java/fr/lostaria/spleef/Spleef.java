package fr.lostaria.spleef;

import fr.lostaria.spleef.game.GameManager;
import fr.lostaria.spleef.game.SpleefPhase;
import fr.lostaria.spleef.listeners.*;
import fr.worsewarn.cosmox.API;
import fr.worsewarn.cosmox.api.achievements.Achievement;
import fr.worsewarn.cosmox.api.statistics.Statistic;
import fr.worsewarn.cosmox.game.Game;
import fr.worsewarn.cosmox.tools.map.MapLocation;
import fr.worsewarn.cosmox.tools.map.MapLocationType;
import fr.worsewarn.cosmox.tools.map.MapTemplate;
import fr.worsewarn.cosmox.tools.map.MapType;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Spleef extends JavaPlugin {

    private API api;
    private Game game;

    private GameManager gameManager;

    private String prefix = "§3Spleef §7| §r";

    @Override
    public void onEnable() {
        api = API.instance();

        gameManager = new GameManager(this);

        List<Statistic> statistics = new ArrayList<>();
        List<Achievement> achievements = new ArrayList<>();

        List<MapLocation> mapsLocation = new ArrayList<MapLocation>();
        mapsLocation.add(new MapLocation("name", MapLocationType.STRING));
        mapsLocation.add(new MapLocation("authors", MapLocationType.STRING));
        mapsLocation.add(new MapLocation("spawn", MapLocationType.LOCATION));
        mapsLocation.add(new MapLocation("layers", MapLocationType.LIST_CUBOID));

        List<MapTemplate> mapsTemplate = new ArrayList<>();
        mapsTemplate.add(new MapTemplate(MapType.NONE, mapsLocation));

        game = new Game("spleef", "Spleef", ChatColor.DARK_AQUA, Material.SNOWBALL, null, 2, false, true, statistics, achievements, Arrays.asList(" ","§c/!\\ Jeu en développement /!\\"," ","§7Survis le plus longtemps possible","§7en faisant tomber tes adversaires !","§7Le dernier sur la plateforme","§7remporte la partie !"), mapsTemplate);
        game.setGameAuthor("Erpriex");
        game.setTags("Solo");
        game.setShowScoreTablist(true);
        game.setRestrictedGame();
        game.setPreparationTime(10);
        game.setDefaultFriendlyFire(true);
        api.registerNewGame(game);

        gameManager.setPhase(SpleefPhase.START);

        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamageListener(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), this);
        getServer().getPluginManager().registerEvents(new GameListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(this), this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public API getAPI(){
        return api;
    }

    public Game getGame(){
        return game;
    }

    public GameManager getGameManager(){
        return gameManager;
    }

    public String getPrefix(){
        return prefix;
    }
}
