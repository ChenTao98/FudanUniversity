package artStore.service.impl;


import artStore.dao.GetArtworkDao;
import artStore.dao.impl.GetArtworkDaoImpl;
import artStore.entity.ArtworksEntity;
import artStore.entity.GenresEntity;
import artStore.entity.SubjectsEntity;
import artStore.service.GetArtworkService;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/17.
 */
public class GetArtworkServiceImpl implements artStore.service.GetArtworkService {
    //    获取全部的作品
    @Override
    public List<ArtworksEntity> getAll() {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        List<ArtworksEntity> artworksEntityList = getArtworkDao.getAll();
        return artworksEntityList;
    }

    //    根据作品Id获取相应作品，用作Detail页面
    @Override
    public List<ArtworksEntity> getOne(String artworkId) {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        List<ArtworksEntity> artworksEntityList = getArtworkDao.getOne(artworkId);
        return artworksEntityList;
    }

    //    获取根据浏览量排序的作品
    @Override
    public List<ArtworksEntity> getByBrowse() {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        List<ArtworksEntity> artworksEntityList = getArtworkDao.getByBrowse();
        return artworksEntityList;
    }

    //    根据作家ID获取该作家的所有作品
    @Override
    public List<ArtworksEntity> getByArtistID(String artistID) {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        return getArtworkDao.getByArtistID(parseInt(artistID));
    }

    //    根据作品ID获取对应的Subject
    @Override
    public List<SubjectsEntity> getSubject(String artworkID) {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        List<SubjectsEntity> subjectsEntityList = getArtworkDao.getSubject(artworkID);
        return subjectsEntityList;
    }

    //    根据获取的搜索类型，分别调用不同的排序方法
    @Override
    public List<ArtworksEntity> getArtworkSearch(String type, String orderType, String inputString) {
        GetArtworkDao getArtworkDao=new GetArtworkDaoImpl();
        List<ArtworksEntity> artworksEntityList=new ArrayList<>();
        if(type.equals("title")){
            artworksEntityList=getArtworkDao.getByTitle(inputString,orderType);
        }else if(type.equals("artist")){
            artworksEntityList=getArtworkDao.getByArtist(inputString,orderType);
        }else {
            artworksEntityList=getArtworkDao.getByDescription(inputString,orderType);
        }
        return artworksEntityList;
    }

    //    根据作品ID获取对应的Genres
    @Override
    public List<GenresEntity> getGenres(String artworkID) {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        List<GenresEntity> genresEntityList = getArtworkDao.getGenres(artworkID);
        return genresEntityList;
    }

    //    根据作品ID增加其浏览量
    @Override
    public void amountAdd(int artworkID) {
        GetArtworkDao getArtworkDao = new GetArtworkDaoImpl();
        getArtworkDao.amountAdd(artworkID);
    }
}
