package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

public class PropertyOutcomeEntityPK implements Serializable {
    private Timestamp outcomeTime;
    private String outcomeType;

    @Column(name = "outcome_time")
    @Id
    public Timestamp getOutcomeTime() {
        return outcomeTime;
    }

    public void setOutcomeTime(Timestamp outcomeTime) {
        this.outcomeTime = outcomeTime;
    }

    @Column(name = "outcome_type")
    @Id
    public String getOutcomeType() {
        return outcomeType;
    }

    public void setOutcomeType(String outcomeType) {
        this.outcomeType = outcomeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyOutcomeEntityPK that = (PropertyOutcomeEntityPK) o;
        return Objects.equals(outcomeTime, that.outcomeTime) &&
                Objects.equals(outcomeType, that.outcomeType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(outcomeTime, outcomeType);
    }
}
