package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "room_info", schema = "property_manage", catalog = "")
public class RoomInfoEntity {
    private int roomId;
    private String community;
    private int unitNum;
    private int roomNum;
    private int roomArea;
    private double pricePerM2;
    private String isSold;
    private int householdId;

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    @Id
    @Column(name = "room_id")
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Basic
    @Column(name = "community")
    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    @Basic
    @Column(name = "unit_num")
    public int getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(int unitNum) {
        this.unitNum = unitNum;
    }

    @Basic
    @Column(name = "room_num")
    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    @Basic
    @Column(name = "room_area")
    public int getRoomArea() {
        return roomArea;
    }

    public void setRoomArea(int roomArea) {
        this.roomArea = roomArea;
    }

    @Basic
    @Column(name = "price_per_m2")
    public double getPricePerM2() {
        return pricePerM2;
    }

    public void setPricePerM2(double pricePerM2) {
        this.pricePerM2 = pricePerM2;
    }

    @Basic
    @Column(name = "is_sold")
    public String getIsSold() {
        return isSold;
    }

    public void setIsSold(String isSold) {
        this.isSold = isSold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomInfoEntity that = (RoomInfoEntity) o;
        return roomId == that.roomId &&
                unitNum == that.unitNum &&
                roomNum == that.roomNum &&
                roomArea == that.roomArea &&
                Double.compare(that.pricePerM2, pricePerM2) == 0 &&
                Objects.equals(community, that.community) &&
                Objects.equals(isSold, that.isSold);
    }

    @Override
    public int hashCode() {

        return Objects.hash(roomId, community, unitNum, roomNum, roomArea, pricePerM2, isSold);
    }
}
