package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "park_buy", schema = "property_manage", catalog = "")
public class ParkBuyEntity {
    private Timestamp buyTime;
    private double charge;
    private int parkId;
    private int householdId;

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }

    @Basic
    @Column(name = "buy_time")
    public Timestamp getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Timestamp buyTime) {
        this.buyTime = buyTime;
    }

    @Basic
    @Column(name = "charge")
    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkBuyEntity that = (ParkBuyEntity) o;
        return Double.compare(that.charge, charge) == 0 &&
                Objects.equals(buyTime, that.buyTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(buyTime, charge);
    }
}
