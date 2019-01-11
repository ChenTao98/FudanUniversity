package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "property_outcome", schema = "property_manage", catalog = "")
@IdClass(PropertyOutcomeEntityPK.class)
public class PropertyOutcomeEntity {
    private Timestamp outcomeTime;
    private String outcomeType;
    private double outcomeAmount;

    @Id
    @Column(name = "outcome_time")
    public Timestamp getOutcomeTime() {
        return outcomeTime;
    }

    public void setOutcomeTime(Timestamp outcomeTime) {
        this.outcomeTime = outcomeTime;
    }

    @Id
    @Column(name = "outcome_type")
    public String getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(String outcomeType) {
        this.outcomeType = outcomeType;
    }

    @Basic
    @Column(name = "outcome_amount")
    public double getOutcomeAmount() {
        return outcomeAmount;
    }

    public void setOutcomeAmount(double outcomeAmount) {
        this.outcomeAmount = outcomeAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyOutcomeEntity that = (PropertyOutcomeEntity) o;
        return Double.compare(that.outcomeAmount, outcomeAmount) == 0 &&
                Objects.equals(outcomeTime, that.outcomeTime) &&
                Objects.equals(outcomeType, that.outcomeType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(outcomeTime, outcomeType, outcomeAmount);
    }
}
