package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "park_temp", schema = "property_manage", catalog = "")
public class ParkTempEntity {
    private Timestamp parkTime;
    private String licenseNum;
    private double charge;
    private int parkId;
    public int getParkId() {
        return parkId;
    }

    public void setParkId(int parkId) {
        this.parkId = parkId;
    }



    @Basic
    @Column(name = "park_time")
    public Timestamp getParkTime() {
        return parkTime;
    }

    public void setParkTime(Timestamp parkTime) {
        this.parkTime = parkTime;
    }

    @Basic
    @Column(name = "license_num")
    public String getLicenseNum() {
        return licenseNum;
    }

    public void setLicenseNum(String licenseNum) {
        this.licenseNum = licenseNum;
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
        ParkTempEntity that = (ParkTempEntity) o;
        return Double.compare(that.charge, charge) == 0 &&
                Objects.equals(parkTime, that.parkTime) &&
                Objects.equals(licenseNum, that.licenseNum);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parkTime, licenseNum, charge);
    }
}
