package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.lostaria.spleef.game.SpleefPhase;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class IncrementPlayerSnowballTask extends BukkitRunnable {

    private Spleef main;
    private Player player;

    private int initTimer = 4;
    private int timer = initTimer;

    public IncrementPlayerSnowballTask(Spleef main, Player player){
        this.main = main;
        this.player = player;
        progressBar(player);
    }

    @Override
    public void run() {
        if(main.getGameManager().getPhase() != SpleefPhase.GAME){
            cancel();
            return;
        }

        if(timer == 0){
            int snowballs = main.getGameManager().getSnowballsInventory().get(player);
            if(snowballs < 6){
                main.getGameManager().getSnowballsInventory().put(player, snowballs + 1);
                main.getGameManager().updatePlayerSnowballsInventory(player);
                if(snowballs < 5){
                    progressBar(player);
                }else{
                    cancel();
                }
            }
            timer = initTimer + 1;
        }
        timer--;
    }

    private void progressBar(Player player){
        long duration = TimeUnit.SECONDS.toNanos(5);
        long start = System.nanoTime();
        new BukkitRunnable() {
            public void run() {
                long diff = System.nanoTime() - start;
                if (diff > duration) {
                    this.cancel();
                    player.setExp(0);
                    return;
                }
                float value = (float) diff / (float) duration;
                player.setExp((value > 1F ? 1F : value));
            }
        }.runTaskTimer(main, 0, 1);
    }

}
