package org.huminlabs.huminlabplugin.Objective;
import org.bukkit.entity.Player;
import org.huminlabs.huminlabplugin.HuMInLabPlugin;

public class PlayerPointer {
    private String uuid;
    private String objectiveID;
    private String unit;


    public PlayerPointer(String uuid) {
        this.uuid = uuid;
        this.objectiveID = "0.0";
        this.unit = "0";
    }

    public PlayerPointer(String uuid, String unit, String objectiveID) {
        this.uuid = uuid;
        this.objectiveID = objectiveID;
        this.unit = unit;
    }

    public String getUUID() {
        return uuid;
    }

    public String getObjectiveID() {
        return objectiveID;
    }
    public String getUnit() {
        return unit;
    }

    public void setObjective(String unit, String objectiveID) {
        this.unit = unit;
        this.objectiveID = objectiveID;
        HuMInLabPlugin.backendRequestHandler.objectiveUpdate(uuid, unit, objectiveID);

        if(objectiveID.equals("0.0")){
            this.unit = "0";
        }
    }


//    public void setObjectiveID(String objectiveID) {
//        this.objectiveID = objectiveID;
//    }
//    public void setUnit(String unit) {
//        this.unit = unit;
//    }



}