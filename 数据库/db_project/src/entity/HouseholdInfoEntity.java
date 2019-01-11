package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "household_info", schema = "property_manage", catalog = "")
@IdClass(HouseholdInfoEntityPK.class)
public class HouseholdInfoEntity {
    private int householdId;
    private String householdIdCard;
    private String householdName;
    private String householdPhone;
    private String isValid;
//    private HouseholdBillEntity householdBillByHouseholdId;

    @Id
    @Column(name = "household_id")
    public int getHouseholdId() {
        return householdId;
    }

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
    }

    @Id
    @Column(name = "household_id_card")
    public String getHouseholdIdCard() {
        return householdIdCard;
    }

    public void setHouseholdIdCard(String householdIdCard) {
        this.householdIdCard = householdIdCard;
    }

    @Basic
    @Column(name = "household_name")
    public String getHouseholdName() {
        return householdName;
    }

    public void setHouseholdName(String householdName) {
        this.householdName = householdName;
    }

    @Basic
    @Column(name = "household_phone")
    public String getHouseholdPhone() {
        return householdPhone;
    }

    public void setHouseholdPhone(String householdPhone) {
        this.householdPhone = householdPhone;
    }

    @Basic
    @Column(name = "is_valid")
    public String getIsValid() {
        return isValid;
    }

    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HouseholdInfoEntity that = (HouseholdInfoEntity) o;
        return householdId == that.householdId &&
                Objects.equals(householdIdCard, that.householdIdCard) &&
                Objects.equals(householdName, that.householdName) &&
                Objects.equals(householdPhone, that.householdPhone) &&
                Objects.equals(isValid, that.isValid);
    }

    @Override
    public int hashCode() {

        return Objects.hash(householdId, householdIdCard, householdName, householdPhone, isValid);
    }

//    @ManyToOne
//    @JoinColumn(name = "household_id", referencedColumnName = "household_id", nullable = false)
//    public HouseholdBillEntity getHouseholdBillByHouseholdId() {
//        return householdBillByHouseholdId;
//    }
//
//    public void setHouseholdBillByHouseholdId(HouseholdBillEntity householdBillByHouseholdId) {
//        this.householdBillByHouseholdId = householdBillByHouseholdId;
//    }
}
