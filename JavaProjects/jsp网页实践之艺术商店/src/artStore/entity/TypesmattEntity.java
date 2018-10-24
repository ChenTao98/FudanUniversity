package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "typesmatt", schema = "art", catalog = "")
public class TypesmattEntity {
    private int mattId;
    private String title;
    private String colorCode;

    @Id
    @Column(name = "MattID")
    public int getMattId() {
        return mattId;
    }

    public void setMattId(int mattId) {
        this.mattId = mattId;
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
    @Column(name = "ColorCode")
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TypesmattEntity that = (TypesmattEntity) o;

        if (mattId != that.mattId) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (colorCode != null ? !colorCode.equals(that.colorCode) : that.colorCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mattId;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (colorCode != null ? colorCode.hashCode() : 0);
        return result;
    }
}
