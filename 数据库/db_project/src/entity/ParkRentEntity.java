package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "park_rent", schema = "property_manage", catalog = "")
public class ParkRentEntity {
    private Timestamp rentStartTime;
    private Timestamp rentEndTime;
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
        ParkRentEntity that = (ParkRentEntity) o;
        return Double.compare(that.charge, charge) == 0 &&
                Objects.equals(rentStartTime, that.rentStartTime) &&
                Objects.equals(rentEndTime, that.rentEndTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rentStartTime, rentEndTime, charge);
    }
}
