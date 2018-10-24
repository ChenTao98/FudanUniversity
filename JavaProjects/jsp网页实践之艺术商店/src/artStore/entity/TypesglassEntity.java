package artStore.entity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "typesglass", schema = "art", catalog = "")
public class TypesglassEntity {
    private int glassId;
    private String title;
    private String description;
    private BigDecimal price;

    @Id
    @Column(name = "GlassID")
    public int getGlassId() {
        return glassId;
    }

    public void setGlassId(int glassId) {
        this.glassId = glassId;
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
    @Column(name = "Description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "Price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypesglassEntity that = (TypesglassEntity) o;

        if (glassId != that.glassId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = glassId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
