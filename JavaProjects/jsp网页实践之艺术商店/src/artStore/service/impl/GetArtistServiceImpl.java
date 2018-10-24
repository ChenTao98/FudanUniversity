package artStore.service.impl;

import artStore.dao.GetArtistDao;
import artStore.dao.impl.GetArtistDaoImpl;
import artStore.entity.ArtistsEntity;
import artStore.service.GetArtistService;

import java.util.List;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public class GetArtistServiceImpl implements GetArtistService {
    //    获取全部作家信息
    @Override
    public List<ArtistsEntity> getAll() {
        GetArtistDao getArtistDao = new GetArtistDaoImpl();
        List<ArtistsEntity> artistsEntityList = getArtistDao.getAll();
        return artistsEntityList;
    }

    //    作家详细页根据作家Id获取单个作家信息
    @Override
    public List<ArtistsEntity> getOne(String artistID) {
        GetArtistDao getArtistDao = new GetArtistDaoImpl();
        return getArtistDao.getOne(artistID);
    }
}
