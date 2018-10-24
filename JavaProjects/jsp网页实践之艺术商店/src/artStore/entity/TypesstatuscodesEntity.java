package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "typesstatuscodes", schema = "art", catalog = "")
public class TypesstatuscodesEntity {
    private int statusId;
    private String status;

    @Id
    @Column(name = "StatusID")
    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    @Basic
    @Column(name = "Status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypesstatuscodesEntity that = (TypesstatuscodesEntity) o;

        if (statusId != that.statusId) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusId;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
