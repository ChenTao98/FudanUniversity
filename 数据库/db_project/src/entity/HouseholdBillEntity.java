package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "household_bill", schema = "property_manage", catalog = "")
public class HouseholdBillEntity {
    private Timestamp billTime;
    private int householdId;
    private double propertyCharge;
    private double parkCharge;
    private String isProChargePay;
    private String isParkChargePay;

    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    @Basic
    @Column(name = "bill_time")
    public Timestamp getBillTime() {
        return billTime;
    }

    public void setBillTime(Timestamp billTime) {
        this.billTime = billTime;
    }

    @Basic
    @Column(name = "property_charge")
    public double getPropertyCharge() {
        return propertyCharge;
    }

    public void setPropertyCharge(double propertyCharge) {
        this.propertyCharge = propertyCharge;
    }

    @Basic
    @Column(name = "park_charge")
    public double getParkCharge() {
        return parkCharge;
    }

    public void setParkCharge(double parkCharge) {
        this.parkCharge = parkCharge;
    }

    @Basic
    @Column(name = "is_pro_charge_pay")
    public String getIsProChargePay() {
        return isProChargePay;
    }

    public void setIsProChargePay(String isProChargePay) {
        this.isProChargePay = isProChargePay;
    }

    @Basic
    @Column(name = "is_park_charge_pay")
    public String getIsParkChargePay() {
        return isParkChargePay;
    }

    public void setIsParkChargePay(String isParkChargePay) {
        this.isParkChargePay = isParkChargePay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseholdBillEntity that = (HouseholdBillEntity) o;
        return Double.compare(that.propertyCharge, propertyCharge) == 0 &&
                Double.compare(that.parkCharge, parkCharge) == 0 &&
                Objects.equals(billTime, that.billTime) &&
                Objects.equals(isProChargePay, that.isProChargePay) &&
                Objects.equals(isParkChargePay, that.isParkChargePay);
    }

    @Override
    public int hashCode() {

        return Objects.hash(billTime, propertyCharge, parkCharge, isProChargePay, isParkChargePay);
    }
}
