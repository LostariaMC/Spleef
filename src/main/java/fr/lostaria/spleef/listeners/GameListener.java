package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.game.events.GamePreparationOverEvent;
import fr.worsewarn.cosmox.game.events.GameStartEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GameListener implements Listener {

    private Spleef main;

    public GameListener(Spleef main) {
        this.main = main;
    }

    @EventHandler
    public void onGameStart(GameStartEvent event){
        main.getServer().getPluginManager().registerEvents(new BlockBreakListener(main), main);
        main.getServer().getPluginManager().registerEvents(new EntityDamageListener(main), main);
        main.getServer().getPluginManager().registerEvents(new EntityDamageByEntityListener(), main);
        main.getServer().getPluginManager().registerEvents(new InventoryClickListener(main), main);
        main.getServer().getPluginManager().registerEvents(new PlayerInteractListener(main), main);
        main.getServer().getPluginManager().registerEvents(new PlayerJoinListener(main), main);
        main.getServer().getPluginManager().registerEvents(new PlayerSwapHandItemsListener(main), main);
        main.getServer().getPluginManager().registerEvents(new ProjectileHitListener(main), main);
        main.getServer().getPluginManager().registerEvents(new ProjectileLaunchListener(main), main);

        main.getGameManager().startGame(event);
    }

    @EventHandler
    public void onPreparationOver(GamePreparationOverEvent event){
        main.getGameManager().preparationOver();
    }
}
