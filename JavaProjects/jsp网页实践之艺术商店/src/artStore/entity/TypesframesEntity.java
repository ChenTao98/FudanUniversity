package artStore.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "typesframes", schema = "art", catalog = "")
public class TypesframesEntity {
    private int frameId;
    private String title;
    private BigDecimal price;
    private String color;
    private String syle;

    @Id
    @Column(name = "FrameID")
    public int getFrameId() {
        return frameId;
    }

    public void setFrameId(int frameId) {
        this.frameId = frameId;
    }

    @Basic
    @Column(name = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "Price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "Color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Basic
    @Column(name = "Syle")
    public String getSyle() {
        return syle;
    }

    public void setSyle(String syle) {
        this.syle = syle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypesframesEntity that = (TypesframesEntity) o;

        if (frameId != that.frameId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (color != null ? !color.equals(that.color) : that.color != null) return false;
        if (syle != null ? !syle.equals(that.syle) : that.syle != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = frameId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (color != null ? color.hashCode() : 0);
        result = 31 * result + (syle != null ? syle.hashCode() : 0);
        return result;
    }
}
