package entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "device_info", schema = "property_manage", catalog = "")
public class DeviceInfoEntity {
    private int deviceType;
    private String deviceName;
    private double deviceCharge;
    private String isIndoor;

    @Id
    @Column(name = "device_type")
    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Basic
    @Column(name = "device_name")
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Basic
    @Column(name = "device_charge")
    public double getDeviceCharge() {
        return deviceCharge;
    }

    public void setDeviceCharge(double deviceCharge) {
        this.deviceCharge = deviceCharge;
    }

    @Basic
    @Column(name = "is_indoor")
    public String getIsIndoor() {
        return isIndoor;
    }

    public void setIsIndoor(String isIndoor) {
        this.isIndoor = isIndoor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceInfoEntity that = (DeviceInfoEntity) o;
        return deviceType == that.deviceType &&
                Double.compare(that.deviceCharge, deviceCharge) == 0 &&
                Objects.equals(deviceName, that.deviceName) &&
                Objects.equals(isIndoor, that.isIndoor);
    }

    @Override
    public int hashCode() {

        return Objects.hash(deviceType, deviceName, deviceCharge, isIndoor);
    }
}
