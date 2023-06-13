package fr.lostaria.spleef.tasks;

import fr.lostaria.spleef.Spleef;
import fr.worsewarn.cosmox.tools.utils.BarAnimation;
import fr.worsewarn.cosmox.tools.utils.Pair;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DestructionLayersTask extends BukkitRunnable {

    private Spleef main;

    private BarAnimation currentBarAnimation = null;

    private List<Pair<Location, Location>> layers;
    private int currentLayer = 0;

    private boolean lockCheck = false;

    public DestructionLayersTask(Spleef main) {
        this.main = main;
        updateBoosbar(false, false);
        this.layers = main.getAPI().getManager().getMap().getCuboids("layers");
    }

    @Override
    public void run() {

        int targetY = layers.get(currentLayer).getLeft().getBlockY();
        int nbPlayersOnY = 0; // Le J en Y

        for(Player pls : Bukkit.getOnlinePlayers()){
            if(pls.getLocation().getBlockY() >= targetY){
                nbPlayersOnY++;
            }
        }

        if(nbPlayersOnY <= 1 && !lockCheck){
            lockCheck = true;
            updateBoosbar(true, false);
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

        Location loc1 = layers.get(currentLayer).getLeft();
        Location loc2 = layers.get(currentLayer).getRight();

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
                    if(l.getBlock().getType() != Material.AIR) {
                        blockLocations.add(l);
                    }
                }
            }
        }

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

                    for(Player pls : Bukkit.getOnlinePlayers()){
                        pls.sendMessage("§7§oPfiou, ça décoiffe..");
                    }

                    updateBoosbar(false, false);
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
