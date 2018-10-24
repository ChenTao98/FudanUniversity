package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/20.
 */
@Entity
@Table(name = "orderdetail", schema = "art", catalog = "")
public class OrderdetailEntity {
    private int orderId;
    private int customerId;
    private String dateCreated;
    private int artworkId;
    private String artworkName;
    public OrderdetailEntity(){};
    public OrderdetailEntity(int orderId,int customerId,String dateCreated,int artworkId,String artworkName){
        this.orderId=orderId;
        this.customerId=customerId;
        this.dateCreated=dateCreated;
        this.artworkId=artworkId;
        this.artworkName=artworkName;
    };

    @Id
    @Column(name = "OrderID")
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

        OrderdetailEntity that = (OrderdetailEntity) o;

        if (orderId != that.orderId) return false;
        if (customerId != that.customerId) return false;
        if (artworkId != that.artworkId) return false;
        if (dateCreated != null ? !dateCreated.equals(that.dateCreated) : that.dateCreated != null) return false;
        if (artworkName != null ? !artworkName.equals(that.artworkName) : that.artworkName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = orderId;
        result = 31 * result + customerId;
        result = 31 * result + (dateCreated != null ? dateCreated.hashCode() : 0);
        result = 31 * result + artworkId;
        result = 31 * result + (artworkName != null ? artworkName.hashCode() : 0);
        return result;
    }
}
