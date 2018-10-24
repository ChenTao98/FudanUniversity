package artStore.dao;

import artStore.entity.ArtworksEntity;
import artStore.entity.GenresEntity;
import artStore.entity.SubjectsEntity;

import java.util.List;

/**
 * Created by Bing Chen on 2017/7/18.
 */
public interface GetArtworkDao {
    //    获取全部的作品
    List<ArtworksEntity> getAll();

    //    根据作品Id获取相应作品，用作Detail页面
    List<ArtworksEntity> getOne(String artworkId);

    //    获取根据浏览量排序的作品
    List<ArtworksEntity> getByBrowse();

    //    根据作家ID获取该作家的所有作品
    List<ArtworksEntity> getByArtistID(int artistID);

    //    根据作品ID获取对应的Subject
    List<SubjectsEntity> getSubject(String artworkID);

    //    根据作品ID获取对应的Genres
    List<GenresEntity> getGenres(String artworkID);

    //    根据输入的Title选择对应作品
    List<ArtworksEntity> getByTitle(String title,String orderType);

    //    根据输入的作家名选择相应作品
    List<ArtworksEntity> getByArtist(String artist,String orderType);

    //    根据输入的描述选择相应作品
    List<ArtworksEntity> getByDescription(String description,String orderType);

    //    根据作品ID增加其浏览量
    void amountAdd(int artworkID);
}
