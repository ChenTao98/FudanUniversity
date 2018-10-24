package artStore.dao.impl;

import artStore.dao.CartDoDao;
import artStore.entity.OrderdetailEntity;
import artStore.entity.OrderrecordEntity;
import artStore.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Integer.parseInt;

/**
 * Created by Bing Chen on 2017/7/20.
 */
public class CartDoDaoImpl implements CartDoDao {
    private JdbcUtil utilConnect;

    public CartDoDaoImpl() {
        // 构造参数调用时创建数据库连接
        this.utilConnect = new JdbcUtil();
    }

    //通过用户ID获取该用户的所有购物车订单
    @Override
    public List<OrderdetailEntity> cartGetByCustomerID(int customerID) {
//        建立数据库连接，初始化sql语句
        Connection connection = utilConnect.getConnection();
//        根据用户ID选择其所有订单
        String sql = "SELECT * FROM orderdetail WHERE CustomerID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
//        新建OrderdetailEntity实例的List
        List<OrderdetailEntity> orderdetailEntityList = new ArrayList<>();
//        执行sql语句，并catach异常，选择用户的所有购物车订单
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, customerID);
            rs = pst.executeQuery();
//            判定选择出的结果是否含有下一行，有的话建立实例并加入List
            while (rs.next()) {
                orderdetailEntityList.add(buildOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
//        返回结果
        return orderdetailEntityList;
    }

    //通过用户ID与订单ID完成购物车的删除工作
    @Override
    public boolean deleteByDoubleID(int customerID, int orderID) {
//        建立数据库连接，初始化sql语句
        Connection connection = utilConnect.getConnection();
        String sql = "DELETE FROM orderdetail WHERE OrderID=? AND CustomerID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
//        执行sql语句，并catach异常，删除该用户的该商品的订单
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, orderID);
            pst.setInt(2, customerID);
            pst.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return result;
    }

    //用户购买时通过商品ID删除所有该商品的订单，并删除商品，以做到商品只能被购买一次
    @Override
    public boolean deleteByArtworkID(int artworkID) {
//        建立数据库连接，初始化sql语句
        Connection connection = utilConnect.getConnection();
//        删除订单中所有该商品的订单
        String sql = "DELETE FROM orderdetail WHERE artworkID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
//        执行sql语句，并catach异常
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, artworkID);
            pst.executeUpdate();
//            删除图片中的该商品
            sql = "DELETE FROM artworks WHERE ArtWorkID=?";
            pst = connection.prepareStatement(sql);
            pst.setInt(1, artworkID);
            pst.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return result;
    }

    //购买时操作，获取通过含有商品ID 的数组与用户ID完成购买操作，函数执行期间调用deleteByArtworkID与addToRecord函数
    @Override
    public boolean deleteByList(String[] artworkIDArray, int customerID) {
//        建立数据库连接，初始化sql语句
        Connection connection = utilConnect.getConnection();
        String sql = "";
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
//            遍历传入的ID数组，对每一个ID进行操作
            for (int i = 0; i < artworkIDArray.length; i++) {
                int artworkID = parseInt(artworkIDArray[i]);
//                通过sql语句，获取该用户的该商品的订单详细
                sql = "SELECT * FROM orderdetail WHERE artworkID=? AND CustomerID=?";
                pst = connection.prepareStatement(sql);
                pst.setInt(1, artworkID);
                pst.setInt(2, customerID);
                rs = pst.executeQuery();
                if (rs.next()) {
                    String dataComplete = new Date().toString();
//                    通过获取的商品信息，加入完成订单的表格
                    result = addToRecord(customerID, rs.getString("DateCreated"), artworkID, rs.getString("artworkName"), dataComplete);
//                    删除该商品的所有订单
                    result = deleteByArtworkID(artworkID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return result;
    }

    // 通过获取的数据，添加购物车订单
    @Override
    public boolean addToCart(int customerID, Date date, int artworkID, String artworkName) {
//        建立数据库连接，初始化sql语句
        Connection connection = utilConnect.getConnection();
//        从订单中选择用户的该对应商品
        String sql = "SELECT * FROM orderdetail WHERE CustomerID=? AND artworkID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
//        执行语句，catch异常
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, customerID);
            pst.setInt(2, artworkID);
            rs = pst.executeQuery();
//            是否有结果选择出来
            if (rs.next()) {
//                若有结果，说明该商品已经加入该用户购物车，无需重复添加，直接返回
                result = true;
            } else {
//                若无结果，插入该订单，并返回
                sql = "INSERT INTO orderdetail (CustomerID, artworkID, artworkName,DateCreated) VALUES (?,?,?,?)";
                String dataString = date.toString();
                pst = connection.prepareStatement(sql);
//                设置参数
                pst.setInt(1, customerID);
                pst.setInt(2, artworkID);
                pst.setString(3, artworkName);
                pst.setString(4, dataString);
                pst.executeUpdate();
                result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return result;
    }

    //购买时通过传入的参数往完成订单的表中插入数据
    @Override
    public boolean addToRecord(int customerID, String dateCreate, int artworkID, String artworkName, String dataComplete) {
//        建立连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        插入已完成订单语句
        String sql = "INSERT INTO orderrecord (CustomerID, DateCreated, DateComplete, artworkID, artworkName) VALUES (?,?,?,?,?)";
        PreparedStatement pst = null;
        ResultSet rs = null;
        boolean result = false;
        try {
            pst = connection.prepareStatement(sql);
//            设置参数
            pst.setInt(1, customerID);
            pst.setString(2, dateCreate);
            pst.setString(3, dataComplete);
            pst.setInt(4, artworkID);
            pst.setString(5, artworkName);
            pst.executeUpdate();
            result = true;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return result;
    }

    //通过用户ID，获取该用户所有已完成订单
    @Override
    public List<OrderrecordEntity> recordGetByCustomerID(int CustomerID) {
//        建立连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        选择用户的所有已完成订单
        String sql = "SELECT * FROM orderrecord WHERE CustomerID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<OrderrecordEntity> orderrecordEntityList = new ArrayList<>();
        try {
//            执行语句设置参数
            pst = connection.prepareStatement(sql);
            pst.setInt(1, CustomerID);
            rs = pst.executeQuery();
//            判定选择结果是否下一个，如果有，建立 orderrecordEntity实例并加入List
            while (rs.next()) {
                orderrecordEntityList.add(buildRecord(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return orderrecordEntityList;
    }

    // 建立购物车订单OrderdetailEntity的实例
    private OrderdetailEntity buildOrder(ResultSet rs) throws SQLException {
        return new OrderdetailEntity(rs.getInt("OrderID"), rs.getInt("CustomerID"),
                rs.getString("DateCreated"), rs.getInt("artworkID"), rs.getString("artworkName"));
    }

    // 建立已完成订单OrderrecordEntity的实例
    private OrderrecordEntity buildRecord(ResultSet rs) throws SQLException {
        return new OrderrecordEntity(rs.getInt("recordID"), rs.getInt("CustomerID"),
                rs.getString("DateCreated"), rs.getString("DateComplete"),
                rs.getInt("artworkID"), rs.getString("artworkName"));
    }
}
