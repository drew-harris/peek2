package org.huminlabs.huminlabplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.Objective.ObjectiveStorage;
import org.huminlabs.huminlabplugin.Objective.PlayerPointer;

public final class Commands implements CommandExecutor {
    private final HuMInLabPlugin plugin;

    public Commands(HuMInLabPlugin plugin) {
        this.plugin = plugin;
    }

    // === Commands ===
    //
    //    getstage
    //    setstage <unit> <lessonID>
    //    nextstage
    //    prevstage

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        System.out.println("Sender " + sender.getName() + " issued command " + command.getName() + " with args " + args);
        if (sender instanceof Player) {
            Player target = (Player) sender;
            PlayerPointer pointer = ObjectiveStorage.getPlayerPointer(target);
            if (pointer != null) {
                switch (command.getName()) {
                    case "getstage":
                        getStage(target, pointer);
                        break;
                    case "setstage":
                        if (!(args.length == 2 || args.length == 3)) {
                            target.sendMessage("Error: Invalid number of arguments");
                            return false;
                        }
                        if (args.length == 3) {
                            target = plugin.getServer().getPlayer(args[2]);
                        }
                        String unit = args[0];
                        String lessonID = args[1];

                        setStage(target, pointer, unit, lessonID);
                        break;
                    case "nextstage":
                        nextStage(target, pointer);
                        break;
                    case "prevstage":
                        prevStage(target, pointer);
                        break;
                }
            }

        } else {
            if (args.length == 3) {
                Player target = plugin.getServer().getPlayer(args[0]);
                PlayerPointer pointer = ObjectiveStorage.getPlayerPointer(target);
                if (pointer != null) {
                    switch (command.getName()) {
                        case "getstage":
                            getStage(target, pointer);
                            break;
                        case "setstage":
                            if (args.length != 3) {
                                target.sendMessage("Error: Invalid number of arguments");
                                return false;
                            }
                            setStage(target, pointer, args[1], args[2]);
                    }
                }
            }
        }
        return true;
    }


    // === Command Methods ===
    private void getStage(Player player, PlayerPointer pointer) {
        player.sendMessage("Your current stage is: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }


    public static void setStage(Player player, PlayerPointer pointer, String unit, String lessonID) {
        if (ObjectiveStorage.getObjective(lessonID, unit) != null) {
            pointer.setObjective(unit, lessonID);
            HuMInLabPlugin.objectiveStorage.updateObjective(player);
            //  player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
            try {
                ObjectiveStorage.savePlayerPointers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (lessonID.equals("0.0")) {
            pointer.setObjective(unit, lessonID);
            HuMInLabPlugin.objectiveStorage.updateObjective(player);
        } else {
            player.sendMessage("Error: Stage not found" + unit + " " + lessonID);
        }
    }

    private void nextStage(Player player, PlayerPointer pointer) {
        ObjectiveStorage.setNextObjective(player);
        player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }

    private void prevStage(Player player, PlayerPointer pointer) {
        ObjectiveStorage.setPrevObjective(player);
        player.sendMessage("Stage set: " + pointer.getUnit() + " " + pointer.getObjectiveID());
    }


}
