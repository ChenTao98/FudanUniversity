package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/20.
 */
@Entity
@Table(name = "orderrecord", schema = "art", catalog = "")
public class OrderrecordEntity {
    private int recordId;
    private int customerId;
    private String dateCreated;
    private String dateComplete;
    private int artworkId;
    private String artworkName;
    public OrderrecordEntity(){}
    public OrderrecordEntity(int recordId,int customerId,String dateCreated,String dateComplete,int artworkId,String artworkName){
        this.recordId=recordId;
        this.customerId=customerId;
        this.dateComplete=dateComplete;
        this.dateCreated=dateCreated;
        this.artworkId=artworkId;
        this.artworkName=artworkName;
    }
    @Id
    @Column(name = "recordID")
    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    @Basic
    @Column(name = "CustomerID")
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Basic
    @Column(name = "DateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Basic
    @Column(name = "DateComplete")
    public String getDateComplete() {
        return dateComplete;
    }

    public void setDateComplete(String dateComplete) {
        this.dateComplete = dateComplete;
    }

    @Basic
    @Column(name = "artworkID")
    public int getArtworkId() {
        return artworkId;
    }

    public void setArtworkId(int artworkId) {
        this.artworkId = artworkId;
    }

    @Basic
    @Column(name = "artworkName")
    public String getArtworkName() {
        return artworkName;
    }

    public void setArtworkName(String artworkName) {
        this.artworkName = artworkName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderrecordEntity that = (OrderrecordEntity) o;

        if (recordId != that.recordId) return false;
        if (customerId != that.customerId) return false;
        if (artworkId != that.artworkId) return false;
        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        if (dateComplete != null ? !dateComplete.equals(that.dateComplete) : that.dateComplete != null) return false;
        if (artworkName != null ? !artworkName.equals(that.artworkName) : that.artworkName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = recordId;
        result = 31 * result + customerId;
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + (dateComplete != null ? dateComplete.hashCode() : 0);
        result = 31 * result + artworkId;
        result = 31 * result + (artworkName != null ? artworkName.hashCode() : 0);
        return result;
    }
}
