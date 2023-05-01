package fr.lostaria.spleef.listeners;

import fr.lostaria.spleef.Spleef;
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
        main.getGameManager().startGame(event);
    }
}
