package artStore.dao;

import artStore.entity.ArtistsEntity;

import java.util.List;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public interface GetArtistDao {
    //    获取全部作家信息
    List<ArtistsEntity> getAll();

    //    作家详细页根据作家Id获取单个作家信息
    List<ArtistsEntity> getOne(String artistID);
}
