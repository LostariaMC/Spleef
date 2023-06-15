package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.api.players.CosmoxPlayer;
import fr.worsewarn.cosmox.tools.utils.BarAnimation;
import fr.worsewarn.cosmox.tools.utils.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DestructionLayersTask extends BukkitRunnable {

    private Spleef main;

    private BarAnimation currentBarAnimation = null;

    private int currentLayer = 0;

    private boolean lockCheck = false;

    private List<Player> playersOnY = new ArrayList<>();

    public DestructionLayersTask(Spleef main) {
        this.main = main;
        updateBoosbar(false, false);
    }

    @Override
    public void run() {

        if(currentLayer >= main.getGameManager().getLayers().size()){
            cancel();
            return;
        }

        int targetY = main.getGameManager().getLayers().get(currentLayer).getLeft().getBlockY();

        for(Player pls : Bukkit.getOnlinePlayers()){
            if(pls.getLocation().getBlockY() >= targetY && !lockCheck){
                if(!playersOnY.contains(pls)){
                    playersOnY.add(pls);
                }
            }
        }

        if(playersOnY.size() <= 1 && !lockCheck && currentLayer < main.getGameManager().getLayers().size() - 1){
            lockCheck = true;
            updateBoosbar(true, false);
        }
        if (!lockCheck) {
            playersOnY.clear();
        }
    }


    private void updateBoosbar(boolean shortTimer, boolean destruction){
        if(currentBarAnimation != null) {
            currentBarAnimation.cancelAnimation();
        }
        if(destruction){
            currentBarAnimation = new BarAnimation(Integer.MAX_VALUE, "§4§kk§c Destruction §4§kk§r", BarColor.RED);
            currentBarAnimation.setForAll(true);
            currentBarAnimation.startAnimation(main.getAPI());
            return;
        }
        currentBarAnimation = new BarAnimation((shortTimer ? 11  : main.getAPI().getGameParameter("destructionCooldown")), "§eProchaine destruction dans %i %s", BarColor.YELLOW);
        currentBarAnimation.setForAll(true);
        currentBarAnimation.startAnimation(main.getAPI());
        currentBarAnimation.setCallBack(this::destroyCurrentLayer);
    }

    private void destroyCurrentLayer(){
        main.getAPI().getUtils().broadcast(main.getPrefix() + "§fLa couche §e" + (currentLayer + 1) + "§f se détruit !");

        List<Location> blockLocations = main.getGameManager().getLayerLocations(currentLayer);

        updateBoosbar(false, true);

        BukkitRunnable clearBlocksTask = new BukkitRunnable() {
            private int blocksRemaining = blockLocations.size();

            private final int minValue = 30;
            private final int maxValue = 70;
            private final int minBlocks = 2000;
            private final int maxBlocks = 6000;

            double ratio = (double)(blocksRemaining - minBlocks) / (maxBlocks - minBlocks);
            private final int blocksPerIteration = (int)(minValue + ratio * (maxValue - minValue));


            @Override
            public void run() {
                if (blockLocations.isEmpty()) {
                    this.cancel();

                    for(Player pls : playersOnY){
                        CosmoxPlayer cosmoxPlayer = main.getAPI().getPlayer(pls);
                        List<String> interjections = Arrays.asList("Pfiou, ça décoiffe..", "Wow, ça a fait vibrer mes chaussettes..", "Wow, c'était l'essorage ou quoi !");
                        pls.sendMessage("§7§o" + interjections.get(new Random().nextInt(interjections.size())));
                        cosmoxPlayer.addMolecules(1, "§eSurvivant");
                    }
                    playersOnY.clear();

                    if(currentLayer < main.getGameManager().getLayers().size() - 1){
                        updateBoosbar(false, false);
                    }else{
                        currentBarAnimation.cancelAnimation();
                    }

                    lockCheck = false;
                    currentLayer++;
                    return;
                }

                int blocksToBreak = Math.min(blocksPerIteration, blocksRemaining);

                for (int i = 0; i < blocksToBreak; i++) {
                    int index = new Random().nextInt(blockLocations.size());
                    Location blockLocation = blockLocations.remove(index);
                    Block block = blockLocation.getBlock();
                    block.setType(Material.AIR);

                    block.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, blockLocation, 1, 0, 0, 0, 0);
                    for(Player pls : Bukkit.getOnlinePlayers()){
                        pls.playSound(blockLocation, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 1f);
                    }
                }

                blocksRemaining -= blocksToBreak;
            }
        };

        clearBlocksTask.runTaskTimer(main, 0, 0);
    }



}
