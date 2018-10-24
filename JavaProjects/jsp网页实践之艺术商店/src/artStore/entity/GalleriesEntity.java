package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "galleries", schema = "art", catalog = "")
public class GalleriesEntity {
    private int galleryId;
    private String galleryName;
    private String galleryNativeName;
    private String galleryCity;
    private String galleryCountry;
    private Double latitude;
    private Double longitude;
    private String galleryWebSite;

    @Id
    @Column(name = "GalleryID")
    public int getGalleryId() {
        return galleryId;
    }

    public void setGalleryId(int galleryId) {
        this.galleryId = galleryId;
    }

    @Basic
    @Column(name = "GalleryName")
    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    @Basic
    @Column(name = "GalleryNativeName")
    public String getGalleryNativeName() {
        return galleryNativeName;
    }

    public void setGalleryNativeName(String galleryNativeName) {
        this.galleryNativeName = galleryNativeName;
    }

    @Basic
    @Column(name = "GalleryCity")
    public String getGalleryCity() {
        return galleryCity;
    }

    public void setGalleryCity(String galleryCity) {
        this.galleryCity = galleryCity;
    }

    @Basic
    @Column(name = "GalleryCountry")
    public String getGalleryCountry() {
        return galleryCountry;
    }

    public void setGalleryCountry(String galleryCountry) {
        this.galleryCountry = galleryCountry;
    }

    @Basic
    @Column(name = "Latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "Longitude")
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "GalleryWebSite")
    public String getGalleryWebSite() {
        return galleryWebSite;
    }

    public void setGalleryWebSite(String galleryWebSite) {
        this.galleryWebSite = galleryWebSite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GalleriesEntity that = (GalleriesEntity) o;

        if (galleryId != that.galleryId) return false;
        if (galleryName != null ? !galleryName.equals(that.galleryName) : that.galleryName != null) return false;
        if (galleryNativeName != null ? !galleryNativeName.equals(that.galleryNativeName) : that.galleryNativeName != null)
            return false;
        if (galleryCity != null ? !galleryCity.equals(that.galleryCity) : that.galleryCity != null) return false;
        if (galleryCountry != null ? !galleryCountry.equals(that.galleryCountry) : that.galleryCountry != null)
            return false;
        if (latitude != null ? !latitude.equals(that.latitude) : that.latitude != null) return false;
        if (longitude != null ? !longitude.equals(that.longitude) : that.longitude != null) return false;
        if (galleryWebSite != null ? !galleryWebSite.equals(that.galleryWebSite) : that.galleryWebSite != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = galleryId;
        result = 31 * result + (galleryName != null ? galleryName.hashCode() : 0);
        result = 31 * result + (galleryNativeName != null ? galleryNativeName.hashCode() : 0);
        result = 31 * result + (galleryCity != null ? galleryCity.hashCode() : 0);
        result = 31 * result + (galleryCountry != null ? galleryCountry.hashCode() : 0);
        result = 31 * result + (latitude != null ? latitude.hashCode() : 0);
        result = 31 * result + (longitude != null ? longitude.hashCode() : 0);
        result = 31 * result + (galleryWebSite != null ? galleryWebSite.hashCode() : 0);
        return result;
    }
}
