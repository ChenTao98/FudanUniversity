package artStore.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "typesshippers", schema = "art", catalog = "")
public class TypesshippersEntity {
    private int shipperId;
    private String shipperName;
    private String shipperDescription;
    private String shipperAvgTime;
    private Integer shipperClass;
    private BigDecimal shipperBaseFee;
    private BigDecimal shipperWeightFee;

    @Id
    @Column(name = "shipperID")
    public int getShipperId() {
        return shipperId;
    }

    public void setShipperId(int shipperId) {
        this.shipperId = shipperId;
    }

    @Basic
    @Column(name = "shipperName")
    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    @Basic
    @Column(name = "shipperDescription")
    public String getShipperDescription() {
        return shipperDescription;
    }

    public void setShipperDescription(String shipperDescription) {
        this.shipperDescription = shipperDescription;
    }

    @Basic
    @Column(name = "shipperAvgTime")
    public String getShipperAvgTime() {
        return shipperAvgTime;
    }

    public void setShipperAvgTime(String shipperAvgTime) {
        this.shipperAvgTime = shipperAvgTime;
    }

    @Basic
    @Column(name = "shipperClass")
    public Integer getShipperClass() {
        return shipperClass;
    }

    public void setShipperClass(Integer shipperClass) {
        this.shipperClass = shipperClass;
    }

    @Basic
    @Column(name = "shipperBaseFee")
    public BigDecimal getShipperBaseFee() {
        return shipperBaseFee;
    }

    public void setShipperBaseFee(BigDecimal shipperBaseFee) {
        this.shipperBaseFee = shipperBaseFee;
    }

    @Basic
    @Column(name = "shipperWeightFee")
    public BigDecimal getShipperWeightFee() {
        return shipperWeightFee;
    }

    public void setShipperWeightFee(BigDecimal shipperWeightFee) {
        this.shipperWeightFee = shipperWeightFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypesshippersEntity that = (TypesshippersEntity) o;

        if (shipperId != that.shipperId) return false;
        if (shipperName != null ? !shipperName.equals(that.shipperName) : that.shipperName != null) return false;
        if (shipperDescription != null ? !shipperDescription.equals(that.shipperDescription) : that.shipperDescription != null)
            return false;
        if (shipperAvgTime != null ? !shipperAvgTime.equals(that.shipperAvgTime) : that.shipperAvgTime != null)
            return false;
        if (shipperClass != null ? !shipperClass.equals(that.shipperClass) : that.shipperClass != null) return false;
        if (shipperBaseFee != null ? !shipperBaseFee.equals(that.shipperBaseFee) : that.shipperBaseFee != null)
            return false;
        if (shipperWeightFee != null ? !shipperWeightFee.equals(that.shipperWeightFee) : that.shipperWeightFee != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = shipperId;
        result = 31 * result + (shipperName != null ? shipperName.hashCode() : 0);
        result = 31 * result + (shipperDescription != null ? shipperDescription.hashCode() : 0);
        result = 31 * result + (shipperAvgTime != null ? shipperAvgTime.hashCode() : 0);
        result = 31 * result + (shipperClass != null ? shipperClass.hashCode() : 0);
        result = 31 * result + (shipperBaseFee != null ? shipperBaseFee.hashCode() : 0);
        result = 31 * result + (shipperWeightFee != null ? shipperWeightFee.hashCode() : 0);
        return result;
    }
}
