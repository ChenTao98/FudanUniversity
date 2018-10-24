package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "artworkgenres", schema = "art", catalog = "")
public class ArtworkgenresEntity {
    private int artWorkGenreId;
    private Integer artWorkId;
    private Integer genreId;

    @Id
    @Column(name = "ArtWorkGenreID")
    public int getArtWorkGenreId() {
        return artWorkGenreId;
    }

    public void setArtWorkGenreId(int artWorkGenreId) {
        this.artWorkGenreId = artWorkGenreId;
    }

    @Basic
    @Column(name = "ArtWorkID")
    public Integer getArtWorkId() {
        return artWorkId;
    }

    public void setArtWorkId(Integer artWorkId) {
        this.artWorkId = artWorkId;
    }

    @Basic
    @Column(name = "GenreID")
    public Integer getGenreId() {
        return genreId;
    }

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtworkgenresEntity that = (ArtworkgenresEntity) o;

        if (artWorkGenreId != that.artWorkGenreId) return false;
        if (artWorkId != null ? !artWorkId.equals(that.artWorkId) : that.artWorkId != null) return false;
        if (genreId != null ? !genreId.equals(that.genreId) : that.genreId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artWorkGenreId;
        result = 31 * result + (artWorkId != null ? artWorkId.hashCode() : 0);
        result = 31 * result + (genreId != null ? genreId.hashCode() : 0);
        return result;
    }
}
