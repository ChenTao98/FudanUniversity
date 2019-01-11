package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "service_record", schema = "property_manage", catalog = "")
public class ServiceRecordEntity {
    private Timestamp serviceTime;
    private int deviceType;
    private int checkId;
    private int repairId;

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public int getCheckId() {
        return checkId;
    }

    public void setCheckId(int checkId) {
        this.checkId = checkId;
    }

    public int getRepairId() {
        return repairId;
    }

    public void setRepairId(int repairId) {
        this.repairId = repairId;
    }
    //    private DeviceInfoEntity deviceInfoByDeviceType;
//    private CheckRecordEntity checkRecordByCheckId;
//    private RepairRecordEntity repairRecordByRepairId;

    @Basic
    @Column(name = "service_time")
    public Timestamp getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Timestamp serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRecordEntity that = (ServiceRecordEntity) o;
        return Objects.equals(serviceTime, that.serviceTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(serviceTime);
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
//
//    @ManyToOne
//    @JoinColumn(name = "check_id", referencedColumnName = "check_id")
//    public CheckRecordEntity getCheckRecordByCheckId() {
//        return checkRecordByCheckId;
//    }
//
//    public void setCheckRecordByCheckId(CheckRecordEntity checkRecordByCheckId) {
//        this.checkRecordByCheckId = checkRecordByCheckId;
//    }
//
//    @ManyToOne
//    @JoinColumn(name = "repair_id", referencedColumnName = "repair_id")
//    public RepairRecordEntity getRepairRecordByRepairId() {
//        return repairRecordByRepairId;
//    }
//
//    public void setRepairRecordByRepairId(RepairRecordEntity repairRecordByRepairId) {
//        this.repairRecordByRepairId = repairRecordByRepairId;
//    }
}
