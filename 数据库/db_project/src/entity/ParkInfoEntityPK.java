package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class ParkInfoEntityPK implements Serializable {
    private int parkId;
    private String community;
    private int parkNum;

    @Column(name = "park_id")
    @Id
    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    @Column(name = "community")
    @Id
    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    @Column(name = "park_num")
    @Id
    public int getParkNum() {
        return parkNum;
    }

    public void setParkNum(int parkNum) {
        this.parkNum = parkNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkInfoEntityPK that = (ParkInfoEntityPK) o;
        return parkId == that.parkId &&
                parkNum == that.parkNum &&
                Objects.equals(community, that.community);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parkId, community, parkNum);
    }
}
