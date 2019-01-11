package entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class HouseholdInfoEntityPK implements Serializable {
    private int householdId;
    private String householdIdCard;

    @Column(name = "household_id")
    @Id
    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    @Column(name = "household_id_card")
    @Id
    public String getHouseholdIdCard() {
        return householdIdCard;
    }

    public void setHouseholdIdCard(String householdIdCard) {
        this.householdIdCard = householdIdCard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseholdInfoEntityPK that = (HouseholdInfoEntityPK) o;
        return householdId == that.householdId &&
                Objects.equals(householdIdCard, that.householdIdCard);
    }

    @Override
    public int hashCode() {

        return Objects.hash(householdId, householdIdCard);
    }
}
