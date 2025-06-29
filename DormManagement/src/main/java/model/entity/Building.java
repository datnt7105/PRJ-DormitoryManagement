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
    private int buildingID;
    private String buildingName;
    private int adminID;
    private int numberFloors;
    private String status;
    private String imageUrl;

    public Building() {
    }

    public Building(int buildingID, String buildingName, int adminID, int numberFloors, String status, String imageUrl) {
        this.buildingID = buildingID;
        this.buildingName = buildingName;
        this.adminID = adminID;
        this.numberFloors = numberFloors;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public int getBuildingID() {
        return buildingID;
    }

    public void setBuildingID(int buildingID) {
        this.buildingID = buildingID;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public int getNumberFloors() {
        return numberFloors;
    }

    public void setNumberFloors(int numberFloors) {
        this.numberFloors = numberFloors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

   
    
}
