package artStore.dao.impl;

import artStore.dao.GetArtworkDao;
import artStore.entity.ArtworksEntity;
import artStore.entity.ArtworksubjectsEntity;
import artStore.entity.GenresEntity;
import artStore.entity.SubjectsEntity;
import artStore.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/18.
 */
public class GetArtworkDaoImpl implements GetArtworkDao {
    private JdbcUtil utilConnect;

    public GetArtworkDaoImpl() {
        this.utilConnect = new JdbcUtil();
    }

    //    获取全部的作品
    @Override
    public List<ArtworksEntity> getAll() {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择所有作品
        String sql = "SELECT * FROM artworks";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        创建ArtworksEntity实例的List
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
//            若结果含有下一个，执行while
            while (rs.next()) {
//                创建ArtworksEntity的实例，并加入List
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据作品Id获取相应作品，用作Detail页面
    @Override
    public List<ArtworksEntity> getOne(String artworkId) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        根据作家ID，选择其所有作品
        String sql = "SELECT * FROM artworks WHERE ArtWorkID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        创建ArtworksEntity实例的List
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, (parseInt(artworkId)));
            rs = pst.executeQuery();
//            判定结果是否有下一行，若有，执行while
            while (rs.next()) {
//                创建ArtworksEntity实例，并加入List
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    获取根据浏览量排序的作品
    @Override
    public List<ArtworksEntity> getByBrowse() {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择作品，并按浏览量排序
        String sql = "SELECT * FROM artworks ORDER BY amount DESC ";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        创建ArtworksEntity实例的List
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
//            判定结果是否有下一行，若有，执行while
            while (rs.next()) {
//                创建ArtworksEntity实例并加入List
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据作家ID获取该作家的所有作品
    @Override
    public List<ArtworksEntity> getByArtistID(int artistID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择该作家的所有作品
        String sql = "SELECT * FROM artworks WHERE ArtistID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        创建ArtworksEntity实例的List
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, artistID);
            rs = pst.executeQuery();
//            判定结果是否有下一行，若有，执行while
            while (rs.next()) {
//                创建ArtworksEntity实例并加入List
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            断开连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据作品ID获取对应的Subject
    @Override
    public List<SubjectsEntity> getSubject(String artworkID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        根据作品ID选择字段
        String sql = "SELECT * FROM artworksubjects WHERE ArtWorkID=?";
//        创建SubjectsEntity实例的List
        List<SubjectsEntity> subjectsEntities = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, (parseInt(artworkID)));
            rs = pst.executeQuery();
            if (rs.next()) {
                ArtworksubjectsEntity artworksubjectsEntity = buildSubject(rs);
//                改变sql语句，设置参数为选择出来的subjectID
                sql = "SELECT * FROM subjects WHERE SubjectId=?";
                pst = null;
                rs = null;
                try {
                    pst = connection.prepareStatement(sql);
                    pst.setInt(1, (artworksubjectsEntity.getSubjectId()));
                    rs = pst.executeQuery();
                    if (rs.next()) {
//                        获取subjectsEntities的实例并加入List
                        subjectsEntities.add(buildSubjectSecond(rs));
                    } else {
                        subjectsEntities = null;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                subjectsEntities = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return subjectsEntities;
    }

    //    根据作品ID获取对应的Genres
    @Override
    public List<GenresEntity> getGenres(String artworkID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        根据作品ID选择字段
        String sql = "SELECT * FROM artworkgenres WHERE ArtWorkID=?";
//        创建GenresEntity实例的List
        List<GenresEntity> genresEntityList = new ArrayList<>();
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, (parseInt(artworkID)));
            rs = pst.executeQuery();
            if (rs.next()) {
//                改变sql语句，设置参数为选择出来的genreID
                int genresID = rs.getInt("GenreID");
                sql = "SELECT * FROM genres WHERE GenreID=?";
                pst = connection.prepareStatement(sql);
                pst.setInt(1, genresID);
                rs = pst.executeQuery();
                if (rs.next()) {
//                    创建genresEntity的实例并加入List
                    genresEntityList.add(buildGenres(rs));
                } else {
                    genresEntityList = null;
                }
            } else {
                genresEntityList = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return genresEntityList;
    }

    //    根据输入的Title选择对应作品
    @Override
    public List<ArtworksEntity> getByTitle(String title, String orderType) {
        //        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
        String sql;
//        判定排序方法，生成不同的sql语句
        if (orderType.equals("cost")) {
            sql = "SELECT * FROM artworks WHERE Title LIKE ? ORDER BY Cost";
        } else if (orderType.equals("data")) {
            sql = "SELECT * FROM artworks WHERE Title LIKE ? ORDER BY YearOfWork";
        } else if (orderType.equals("amount")) {
            sql = "SELECT * FROM artworks WHERE Title LIKE ? ORDER BY amount DESC ";
        } else {
            sql = "SELECT * FROM artworks WHERE Title LIKE ? ORDER BY ArtWorkID";
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + title + "%");
            rs = pst.executeQuery();
//            生成artworksEntit的实例并加入List；
            while (rs.next()) {
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据输入的作家名选择相应作品
    @Override
    public List<ArtworksEntity> getByArtist(String artist, String orderType) {
        //        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
        String sql;
//        判定排序方法，生成不同的sql语句
        if (orderType.equals("cost")) {
            sql = "SELECT * FROM artworks WHERE ArtistName LIKE ? ORDER BY Cost";
        } else if (orderType.equals("data")) {
            sql = "SELECT * FROM artworks WHERE ArtistName LIKE ? ORDER BY YearOfWork";
        } else if (orderType.equals("amount")) {
            sql = "SELECT * FROM artworks WHERE ArtistName LIKE ? ORDER BY amount DESC ";
        } else {
            sql = "SELECT * FROM artworks WHERE ArtistName LIKE ? ORDER BY ArtWorkID";
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + artist + "%");
            rs = pst.executeQuery();
//            生成artworksEntit的实例并加入List；
            while (rs.next()) {
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据输入的描述选择相应作品
    @Override
    public List<ArtworksEntity> getByDescription(String description, String orderType) {
        //        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
        String sql;
//        判定排序方法，生成不同的sql语句
        if (orderType.equals("cost")) {
            sql = "SELECT * FROM artworks WHERE Description LIKE ? ORDER BY Cost";
        } else if (orderType.equals("data")) {
            sql = "SELECT * FROM artworks WHERE Description LIKE ? ORDER BY YearOfWork";
        } else if (orderType.equals("amount")) {
            sql = "SELECT * FROM artworks WHERE Description LIKE ? ORDER BY amount DESC ";
        } else {
            sql = "SELECT * FROM artworks WHERE Description LIKE ? ORDER BY ArtWorkID";
        }
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ArtworksEntity> artworksEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setString(1, "%" + description + "%");
            rs = pst.executeQuery();
//            生成artworksEntit的实例并加入List；
            while (rs.next()) {
                artworksEntityList.add(buildArtwork(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return artworksEntityList;
    }

    //    根据作品ID增加其浏览量
    @Override
    public void amountAdd(int artworkID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
        String sql = "SELECT * FROM artworks WHERE ArtWorkID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, artworkID);
            rs = pst.executeQuery();
            if (rs.next()) {
//                获取该作品的浏览量并加一
                int amount = rs.getInt("amount");
                amount++;
//                更新该作品的浏览量
                sql = "UPDATE artworks SET amount=? WHERE ArtWorkID=?";
                pst = connection.prepareStatement(sql);
                pst.setInt(1, amount);
                pst.setInt(2, artworkID);
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
    }

    //    获取ArtworksEntity的实例
    private ArtworksEntity buildArtwork(ResultSet rs) throws SQLException {
        return new ArtworksEntity(rs.getInt("ArtWorkID"), rs.getInt("ArtistID"), rs.getString("ArtistName"),
                rs.getString("ImageFileName"), rs.getString("Title"), rs.getString("Description"),
                rs.getString("Excerpt"), rs.getInt("ArtWorkType"), rs.getInt("YearOfWork"),
                rs.getInt("Width"), rs.getInt("Height"), rs.getInt("amount"),
                rs.getString("Medium"), rs.getString("OriginalHome"), rs.getInt("GalleryID"),
                rs.getBigDecimal("Cost"), rs.getBigDecimal("MSRP"), rs.getString("ArtWorkLink"),
                rs.getString("GoogleLink"));
    }

    //    获取ArtworksubjectsEntity的实例
    private ArtworksubjectsEntity buildSubject(ResultSet rs) throws SQLException {
        return new ArtworksubjectsEntity(rs.getInt("ArtWorkSubjectID"), rs.getInt("ArtWorkID"),
                rs.getInt("SubjectID"));
    }

    //    获取SubjectsEntity的实例
    private SubjectsEntity buildSubjectSecond(ResultSet rs) throws SQLException {
        return new SubjectsEntity(rs.getInt("SubjectId"), rs.getString("SubjectName"));
    }

    //    获取GenresEntity的实例
    private GenresEntity buildGenres(ResultSet rs) throws SQLException {
        return new GenresEntity(rs.getInt("GenreID"), rs.getString("GenreName"), rs.getInt("Era"),
                rs.getString("Description"), rs.getString("Link"));
    }
}
