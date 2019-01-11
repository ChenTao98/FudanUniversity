package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "park_info", schema = "property_manage", catalog = "")
@IdClass(ParkInfoEntityPK.class)
public class ParkInfoEntity {
    private int parkId;
    private String community;
    private int parkNum;
    private String parkType;
    private int priceRent;
    private int priceBuy;
    private Timestamp rentStartTime;
    private Timestamp rentEndTime;
    private ParkBuyEntity parkBuyByParkId;

    @Id
    @Column(name = "park_id")
    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    @Id
    @Column(name = "community")
    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    @Id
    @Column(name = "park_num")
    public int getParkNum() {
        return parkNum;
    }

    public void setParkNum(int parkNum) {
        this.parkNum = parkNum;
    }

    @Basic
    @Column(name = "park_type")
    public String getParkType() {
        return parkType;
    }

    public void setParkType(String parkType) {
        this.parkType = parkType;
    }

    @Basic
    @Column(name = "price_rent")
    public int getPriceRent() {
        return priceRent;
    }

    public void setPriceRent(int priceRent) {
        this.priceRent = priceRent;
    }

    @Basic
    @Column(name = "price_buy")
    public int getPriceBuy() {
        return priceBuy;
    }

    public void setPriceBuy(int priceBuy) {
        this.priceBuy = priceBuy;
    }

    @Basic
    @Column(name = "rent_start_time")
    public Timestamp getRentStartTime() {
        return rentStartTime;
    }

    public void setRentStartTime(Timestamp rentStartTime) {
        this.rentStartTime = rentStartTime;
    }

    @Basic
    @Column(name = "rent_end_time")
    public Timestamp getRentEndTime() {
        return rentEndTime;
    }

    public void setRentEndTime(Timestamp rentEndTime) {
        this.rentEndTime = rentEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkInfoEntity that = (ParkInfoEntity) o;
        return parkId == that.parkId &&
                parkNum == that.parkNum &&
                priceRent == that.priceRent &&
                priceBuy == that.priceBuy &&
                Objects.equals(community, that.community) &&
                Objects.equals(parkType, that.parkType) &&
                Objects.equals(rentStartTime, that.rentStartTime) &&
                Objects.equals(rentEndTime, that.rentEndTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parkId, community, parkNum, parkType, priceRent, priceBuy, rentStartTime, rentEndTime);
    }

    @ManyToOne
    @JoinColumn(name = "park_id", referencedColumnName = "park_id", nullable = false)
    public ParkBuyEntity getParkBuyByParkId() {
        return parkBuyByParkId;
    }

    public void setParkBuyByParkId(ParkBuyEntity parkBuyByParkId) {
        this.parkBuyByParkId = parkBuyByParkId;
    }
}
