package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "check_record", schema = "property_manage", catalog = "")
public class CheckRecordEntity {
    private int checkId;
    private String isIndoor;
    private Timestamp checkTime;
    private String isNeedService;

    @Id
    @Column(name = "check_id")
    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    @Basic
    @Column(name = "is_indoor")
    public String getIsIndoor() {
        return isIndoor;
    }

    public void setIsIndoor(String isIndoor) {
        this.isIndoor = isIndoor;
    }

    @Basic
    @Column(name = "check_time")
    public Timestamp getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Timestamp checkTime) {
        this.checkTime = checkTime;
    }

    @Basic
    @Column(name = "is_need_service")
    public String getIsNeedService() {
        return isNeedService;
    }

    public void setIsNeedService(String isNeedService) {
        this.isNeedService = isNeedService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckRecordEntity that = (CheckRecordEntity) o;
        return checkId == that.checkId &&
                Objects.equals(isIndoor, that.isIndoor) &&
                Objects.equals(checkTime, that.checkTime) &&
                Objects.equals(isNeedService, that.isNeedService);
    }

    @Override
    public int hashCode() {

        return Objects.hash(checkId, isIndoor, checkTime, isNeedService);
    }
}
