package fr.lostaria.spleef;

import fr.lostaria.spleef.game.GameManager;
import fr.lostaria.spleef.listeners.GameListener;
import fr.worsewarn.cosmox.API;
import fr.worsewarn.cosmox.api.achievements.Achievement;
import fr.worsewarn.cosmox.api.statistics.Statistic;
import fr.worsewarn.cosmox.game.Game;
import fr.worsewarn.cosmox.game.teams.Team;
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

    private String prefix = "§6AgeOfEmpire §7| §r";

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
        mapsLocation.add(new MapLocation("center", MapLocationType.LOCATION));

        List<MapTemplate> mapsTemplate = new ArrayList<>();
        mapsTemplate.add(new MapTemplate(MapType.FOUR, mapsLocation));

        game = new Game("spleef", "Spleef", ChatColor.GOLD, Material.SNOWBALL, Arrays.asList(Team.RED, Team.BLUE, Team.YELLOW, Team.GREEN), 2, false, true, statistics, achievements, Arrays.asList("Ouais, pour la description on","verra après"), mapsTemplate);
        game.setGameAuthor("Erpriex");
        game.setTags("Solo");
        game.setShowScoreTablist(true);
        game.setRestrictedGame();
        api.registerNewGame(game);

        getServer().getPluginManager().registerEvents(new GameListener(this), this);


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
