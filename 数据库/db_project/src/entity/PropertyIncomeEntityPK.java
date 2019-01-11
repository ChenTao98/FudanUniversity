package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PropertyIncomeEntityPK implements Serializable {
    private Timestamp incomeTime;
    private String incomeType;

    @Column(name = "income_time")
    @Id
    public Timestamp getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(Timestamp incomeTime) {
        this.incomeTime = incomeTime;
    }

    @Column(name = "income_type")
    @Id
    public String getIncomeType() {
        return incomeType;
    }

    public void setIncomeType(String incomeType) {
        this.incomeType = incomeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyIncomeEntityPK that = (PropertyIncomeEntityPK) o;
        return Objects.equals(incomeTime, that.incomeTime) &&
                Objects.equals(incomeType, that.incomeType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(incomeTime, incomeType);
    }
}
