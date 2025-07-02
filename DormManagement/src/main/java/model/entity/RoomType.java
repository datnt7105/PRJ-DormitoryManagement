package model.entity;

/**
 * Entity cho bảng RoomType
 */
public class RoomType {
    private int roomTypeId;
    private String roomTypeName;
    private int occupancy;
    private double price;

    public RoomType() {
    }

    public RoomType(int roomTypeId, String roomTypeName, int occupancy, double price) {
        this.roomTypeId = roomTypeId;
        this.roomTypeName = roomTypeName;
        this.occupancy = occupancy;
        this.price = price;
    }

    // Getters and Setters
    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "RoomType{" +
                "roomTypeId=" + roomTypeId +
                ", roomTypeName='" + roomTypeName + '\'' +
                ", occupancy=" + occupancy +
                ", price=" + price +
                '}';
    }
}
