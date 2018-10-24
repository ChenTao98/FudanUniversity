package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "artists", schema = "art", catalog = "")
public class ArtistsEntity {
    private int artistId;
    private String firstName;
    private String lastName;
    private String nationality;
    private Integer yearOfBirth;
    private Integer yearOfDeath;
    private String details;
    private String artistLink;

    public ArtistsEntity() {
    }

    public ArtistsEntity(int artistId, String firstName, String lastName, String nationality, Integer yearOfBirth, Integer yearOfDeath,
                         String details, String artistLink) {
        this.artistId = artistId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nationality = nationality;
        this.yearOfBirth = yearOfBirth;
        this.yearOfDeath = yearOfDeath;
        this.details = details;
        this.artistLink = artistLink;
    }

    @Id
    @Column(name = "ArtistID")
    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    @Basic
    @Column(name = "FirstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "Nationality")
    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    @Basic
    @Column(name = "YearOfBirth")
    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    @Basic
    @Column(name = "YearOfDeath")
    public Integer getYearOfDeath() {
        return yearOfDeath;
    }

    public void setYearOfDeath(Integer yearOfDeath) {
        this.yearOfDeath = yearOfDeath;
    }

    @Basic
    @Column(name = "Details")
    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Basic
    @Column(name = "ArtistLink")
    public String getArtistLink() {
        return artistLink;
    }

    public void setArtistLink(String artistLink) {
        this.artistLink = artistLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtistsEntity that = (ArtistsEntity) o;

        if (artistId != that.artistId) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (nationality != null ? !nationality.equals(that.nationality) : that.nationality != null) return false;
        if (yearOfBirth != null ? !yearOfBirth.equals(that.yearOfBirth) : that.yearOfBirth != null) return false;
        if (yearOfDeath != null ? !yearOfDeath.equals(that.yearOfDeath) : that.yearOfDeath != null) return false;
        if (details != null ? !details.equals(that.details) : that.details != null) return false;
        if (artistLink != null ? !artistLink.equals(that.artistLink) : that.artistLink != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artistId;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (nationality != null ? nationality.hashCode() : 0);
        result = 31 * result + (yearOfBirth != null ? yearOfBirth.hashCode() : 0);
        result = 31 * result + (yearOfDeath != null ? yearOfDeath.hashCode() : 0);
        result = 31 * result + (details != null ? details.hashCode() : 0);
        result = 31 * result + (artistLink != null ? artistLink.hashCode() : 0);
        return result;
    }
}
