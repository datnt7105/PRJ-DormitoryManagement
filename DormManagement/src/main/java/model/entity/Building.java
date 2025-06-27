/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

/**
 *
 * @author ngtiendat
 */
public class Building {
    private int BuildingID;
    private String BuildingName;
    private int AdminID;

    public Building() {
    }

    public Building(int BuildingID, String BuildingName, int AdminID) {
        this.BuildingID = BuildingID;
        this.BuildingName = BuildingName;
        this.AdminID = AdminID;
    }

    public int getBuildingID() {
        return BuildingID;
    }

    public void setBuildingID(int BuildingID) {
        this.BuildingID = BuildingID;
    }

    public String getBuildingName() {
        return BuildingName;
    }

    public void setBuildingName(String BuildingName) {
        this.BuildingName = BuildingName;
    }

    public int getAdminID() {
        return AdminID;
    }

    public void setAdminID(int AdminID) {
        this.AdminID = AdminID;
    }
    
    
}
