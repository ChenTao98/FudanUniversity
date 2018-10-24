package artStore.dao.impl;

import artStore.dao.GetArtistDao;
import artStore.entity.ArtistsEntity;
import artStore.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public class GetArtistDaoImpl implements GetArtistDao {
    private JdbcUtil utilConnect;

    public GetArtistDaoImpl() {
        this.utilConnect = new JdbcUtil();
    }

    //    获取全部作家信息
    @Override
    public List<ArtistsEntity> getAll() {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择所有作家
        String sql = "SELECT * FROM artists";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        建立ArtistsEntity实例的list
        List<ArtistsEntity> artistsEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            rs = pst.executeQuery();
//            判定结果是否有下一个，有则执行while
            while (rs.next()) {
//                生成ArtistsEntity实例并加入List
                artistsEntityList.add(buildArtist(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
//        返回List
        return artistsEntityList;
    }

    //    作家详细页根据作家Id获取单个作家信息
    @Override
    public List<ArtistsEntity> getOne(String artistID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择对应ID的作家
        String sql = "SELECT * FROM artists WHERE ArtistID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<ArtistsEntity> artistsEntityList = new ArrayList<>();
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, (parseInt(artistID)));
            rs = pst.executeQuery();
//            建立ArtistsEntity实例并加入List
            while (rs.next()) {
                artistsEntityList.add(buildArtist(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            断开连接
            utilConnect.close(rs, pst, connection);
        }
//        返回list
        return artistsEntityList;
    }

    //    建立ArtistsEntity的一个实例
    private ArtistsEntity buildArtist(ResultSet rs) throws SQLException {
        return new ArtistsEntity(rs.getInt("ArtistID"), rs.getString("FirstName"),
                rs.getString("LastName"), rs.getString("Nationality"), rs.getInt("YearOfBirth"),
                rs.getInt("YearOfDeath"), rs.getString("Details"), rs.getString("ArtistLink"));
    }
}
