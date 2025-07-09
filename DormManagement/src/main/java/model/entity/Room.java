package model.entity;

public class Room {
    private int roomId;
    private String roomNumber;
    private int buildingId;
    private int floor;
    private int occupancy;
    private int currentOccupants;
    private double price;
    private String status;
    private String buildingName; // Thêm để lưu tên tòa nhà khi lấy thông tin

    public Room() {
    }

    public Room(int roomId, String roomNumber, int buildingId, int floor, int occupancy, int currentOccupants, double price, String status, String buildingName) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.buildingId = buildingId;
        this.floor = floor;
        this.occupancy = occupancy;
        this.currentOccupants = currentOccupants;
        this.price = price;
        this.status = status;
        this.buildingName = buildingName;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public int getCurrentOccupants() {
        return currentOccupants;
    }

    public void setCurrentOccupants(int currentOccupants) {
        this.currentOccupants = currentOccupants;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    
}