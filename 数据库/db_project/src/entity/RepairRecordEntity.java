package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "repair_record", schema = "property_manage", catalog = "")
public class RepairRecordEntity {
    private int repairId;
    private int deviceId;
    private String repairReason;
    private Timestamp repairTime;
    private String isService;
    private int roomId;
//    private DeviceInfoEntity deviceInfoByDeviceType;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Id
    @Column(name = "repair_id")
    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }

    @Basic
    @Column(name = "repair_reason")
    public String getRepairReason() {
        return repairReason;
    }

    public void setRepairReason(String repairReason) {
        this.repairReason = repairReason;
    }

    @Basic
    @Column(name = "repair_time")
    public Timestamp getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Timestamp repairTime) {
        this.repairTime = repairTime;
    }

    @Basic
    @Column(name = "is_service")
    public String getIsService() {
        return isService;
    }

    public void setIsService(String isService) {
        this.isService = isService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RepairRecordEntity that = (RepairRecordEntity) o;
        return repairId == that.repairId &&
                Objects.equals(repairReason, that.repairReason) &&
                Objects.equals(repairTime, that.repairTime) &&
                Objects.equals(isService, that.isService);
    }

    @Override
    public int hashCode() {

        return Objects.hash(repairId, repairReason, repairTime, isService);
    }

//    @ManyToOne
//    @JoinColumn(name = "device_type", referencedColumnName = "device_type", nullable = false)
//    public DeviceInfoEntity getDeviceInfoByDeviceType() {
//        return deviceInfoByDeviceType;
//    }
//
//    public void setDeviceInfoByDeviceType(DeviceInfoEntity deviceInfoByDeviceType) {
//        this.deviceInfoByDeviceType = deviceInfoByDeviceType;
//    }
}
