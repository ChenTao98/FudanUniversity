package artStore.entity;

import javax.persistence.*;

/**
 * Created by Bing Chen on 2017/7/17.
 */
@Entity
@Table(name = "artworksubjects", schema = "art", catalog = "")
public class ArtworksubjectsEntity {
    private int artWorkSubjectId;
    private Integer artWorkId;
    private Integer subjectId;
    public ArtworksubjectsEntity(){};
    public ArtworksubjectsEntity(int artWorkSubjectId,Integer artWorkId,Integer subjectId){
        this.artWorkSubjectId=artWorkSubjectId;
        this.artWorkId=artWorkId;
        this.subjectId=subjectId;
    }

    @Id
    @Column(name = "ArtWorkSubjectID")
    public int getArtWorkSubjectId() {
        return artWorkSubjectId;
    }

    public void setArtWorkSubjectId(int artWorkSubjectId) {
        this.artWorkSubjectId = artWorkSubjectId;
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
    @Column(name = "SubjectID")
    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArtworksubjectsEntity that = (ArtworksubjectsEntity) o;

        if (artWorkSubjectId != that.artWorkSubjectId) return false;
        if (artWorkId != null ? !artWorkId.equals(that.artWorkId) : that.artWorkId != null) return false;
        if (subjectId != null ? !subjectId.equals(that.subjectId) : that.subjectId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = artWorkSubjectId;
        result = 31 * result + (artWorkId != null ? artWorkId.hashCode() : 0);
        result = 31 * result + (subjectId != null ? subjectId.hashCode() : 0);
        return result;
    }
}
