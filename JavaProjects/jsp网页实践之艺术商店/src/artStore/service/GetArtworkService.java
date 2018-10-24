package artStore.service;

import artStore.entity.ArtworksEntity;
import artStore.entity.GenresEntity;
import artStore.entity.SubjectsEntity;

import java.util.List;

/**
 * Created by Bing Chen on 2017/7/18.
 */
public interface GetArtworkService {
    //    获取全部的作品
    List<ArtworksEntity> getAll();

    //    根据作品Id获取相应作品，用作Detail页面
    List<ArtworksEntity> getOne(String artworkId);

    //    获取根据浏览量排序的作品
    List<ArtworksEntity> getByBrowse();

    //    根据作家ID获取该作家的所有作品
    List<ArtworksEntity> getByArtistID(String artistID);

    //    根据作品ID获取对应的Genres
    List<GenresEntity> getGenres(String artworkID);

    //    根据作品ID获取对应的Subject
    List<SubjectsEntity> getSubject(String artworkID);
    //    根据获取的搜索类型，分别调用不同的排序方法
    List<ArtworksEntity> getArtworkSearch(String type,String orderType,String inputString);

    //    根据作品ID增加其浏览量
    void amountAdd(int artworkID);
}
