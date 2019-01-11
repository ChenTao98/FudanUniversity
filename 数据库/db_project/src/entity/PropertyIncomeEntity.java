package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "property_income", schema = "property_manage", catalog = "")
@IdClass(PropertyIncomeEntityPK.class)
public class PropertyIncomeEntity {
    private Timestamp incomeTime;
    private String incomeType;
    private double incomeAmount;

    @Id
    @Column(name = "income_time")
    public Timestamp getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(Timestamp incomeTime) {
        this.incomeTime = incomeTime;
    }

    @Id
    @Column(name = "income_type")
    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    @Basic
    @Column(name = "income_amount")
    public double getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(double incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyIncomeEntity that = (PropertyIncomeEntity) o;
        return Double.compare(that.incomeAmount, incomeAmount) == 0 &&
                Objects.equals(incomeTime, that.incomeTime) &&
                Objects.equals(incomeType, that.incomeType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(incomeTime, incomeType, incomeAmount);
    }
}
