package entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "complaint_record", schema = "property_manage", catalog = "")
public class ComplaintRecordEntity {
    private int complaintId;
    private Timestamp complaintTime;
    private String complaintType;
    private String complaintContent;
    private String isProcess;
    private int roomId;
    private String processResult;
    private String howProcess;

    public int getComplaintId() {
        return complaintId;
    }
    public void setComplaintId(int complaintId) {
        this.complaintId = complaintId;
    }
    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    @Basic
    @Column(name = "complaint_time")
    public Timestamp getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(Timestamp complaintTime) {
        this.complaintTime = complaintTime;
    }

    @Basic
    @Column(name = "complaint_type")
    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    @Basic
    @Column(name = "complaint_content")
    public String getComplaintContent() {
        return complaintContent;
    }

    public void setComplaintContent(String complaintContent) {
        this.complaintContent = complaintContent;
    }

    @Basic
    @Column(name = "is_process")
    public String getIsProcess() {
        return isProcess;
    }

    public void setIsProcess(String isProcess) {
        this.isProcess = isProcess;
    }

    @Basic
    @Column(name = "process_result")
    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    @Basic
    @Column(name = "how_process")
    public String getHowProcess() {
        return howProcess;
    }

    public void setHowProcess(String howProcess) {
        this.howProcess = howProcess;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplaintRecordEntity that = (ComplaintRecordEntity) o;
        return Objects.equals(complaintTime, that.complaintTime) &&
                Objects.equals(complaintType, that.complaintType) &&
                Objects.equals(complaintContent, that.complaintContent) &&
                Objects.equals(isProcess, that.isProcess) &&
                Objects.equals(processResult, that.processResult) &&
                Objects.equals(howProcess, that.howProcess);
    }

    @Override
    public int hashCode() {

        return Objects.hash(complaintTime, complaintType, complaintContent, isProcess, processResult, howProcess);
    }
}
