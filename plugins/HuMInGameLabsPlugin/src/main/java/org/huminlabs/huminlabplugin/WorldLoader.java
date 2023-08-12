package org.huminlabs.huminlabplugin;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorldLoader {
    static HuMInLabPlugin plugin;
    private static ArrayList<PlayerPointer> pointers = new ArrayList<>();

    static int slots = 10;
    public static World[] worlds = new World[slots];
    public static String[] playerClaims = new String[slots];

    public WorldLoader(HuMInLabPlugin plugin) {
        this.plugin = plugin;
        this.worlds = new World[10];

        //loadWorlds();
    }

    public static void loadWorlds() {


        for (int i = 0; i < 3; i++) {
            // worlds[i] = getWorld("world" + i);
            worlds[i] = getWorld("world" + i);
            playerClaims[i] = null;
        }
        System.out.println("Worlds loaded");
    }

    private static World getWorld(final String name) {
        World world = Bukkit.getWorld(name);
        if (world != null) {
            return world;
        }

        File worldDir = new File("/static/worlds");
        try {
            FileUtils.copyDirectory(worldDir, new File("/static/worlds/" + name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        WorldCreator creator = new WorldCreator(name);
        world = Bukkit.createWorld(creator);

        return world;
    }

    private static void claimWorld(Player player) {
        for (int i = 0; i < slots; i++) {
            if (playerClaims[i] == null) {
                playerClaims[i] = player.getUniqueId().toString();
                teleportPlayer(player, i);
                System.out.println("World " + i + " claimed by " + player.getName());
                return;
            }
        }
        System.out.println("WARNING: No more worlds available");
    }

    public static void findWorld(Player player) {
        for (int i = 0; i < slots; i++) {
            if (playerClaims[i] != null) {
                if (playerClaims[i].equals(player.getUniqueId().toString())) {
                    teleportPlayer(player, i);
                    System.out.println(player.getName() + " returning to world " + i);
                    return;
                }
            }
        }
        claimWorld(player);
    }

    private static void teleportPlayer(Player player, int worldID) {
        Location location = new Location(worlds[worldID], 155.5, 71, -907.5);
        player.teleport(location);
    }


}
