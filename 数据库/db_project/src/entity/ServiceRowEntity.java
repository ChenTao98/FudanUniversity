package entity;

import java.sql.Timestamp;

public class ServiceRowEntity {
    private Timestamp serviceTime;
    private String deviceName;
    private double amount;
    private int checkId;
    private int repairId;

    public Timestamp getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Timestamp serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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


}
