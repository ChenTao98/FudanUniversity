package artStore.dao.impl;

import artStore.dao.CustomerDao;
import artStore.entity.CustomersEntity;
import artStore.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Bing Chen on 2017/7/19.
 */
public class CustomerDaoImpl implements CustomerDao {
    private JdbcUtil utilConnect;

    public CustomerDaoImpl() {
        this.utilConnect = new JdbcUtil();
    }

    //    通过用户名与密码判定是否能够登录成功并返回CustomersEntity实例
    @Override
    public CustomersEntity Login(String user, String pass) {
//        建立连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        判断是否有该用户存在
        String sql = "SELECT * FROM customerlogon WHERE UserName=? AND Pass=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        CustomersEntity customersEntity = null;
        try {
//            执行语句
            pst = connection.prepareStatement(sql);
            pst.setString(1, user);
            pst.setString(2, pass);
            rs = pst.executeQuery();
            if (rs.next()) {
//                从用户详细中选择结果，并返回用户实例
                sql = "SELECT * FROM customers WHERE CustomerID=?";
                pst = connection.prepareStatement(sql);
                pst.setInt(1, rs.getInt("CustomerID"));
                rs = pst.executeQuery();
                if (rs.next()) {
//                    建立用户实例
                    customersEntity = buildCustomer(rs);
                } else {
                    customersEntity = null;
                }
            } else {
//                若无该用户，返回null
                customersEntity = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return customersEntity;
    }

    //    完成注册功能，返回CustomersEntity实例
    @Override
    public CustomersEntity register(String user, String pass, String lastName) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        判定是否含有该用户名
        String sql = "SELECT * FROM customers WHERE LastName=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        CustomersEntity customersEntity = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, lastName);
            rs = pst.executeQuery();
            if (rs.next()) {
//                如果存在，返回null
                customersEntity = null;
            } else {
//                判定是否存在该邮箱
                sql = "SELECT * FROM customerlogon WHERE UserName=?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, user);
                rs = pst.executeQuery();
                if (rs.next()) {
//                    如果存在该邮箱，返回null
                    customersEntity = null;
                } else {
//                    用户名与邮箱无重复，添加用户信息
                    sql = "INSERT INTO customerlogon (UserName, Pass) VALUES (?,?)";
                    pst = connection.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, pass);
                    pst.executeUpdate();
//                    选择刚刚添加的用户信息，获取用户ID
                    sql = "SELECT * FROM customerlogon WHERE UserName=? AND Pass=?";
                    pst = connection.prepareStatement(sql);
                    pst.setString(1, user);
                    pst.setString(2, pass);
                    rs = pst.executeQuery();
                    rs.next();
//                    通过获取的用户ID在customer表中插入信息
                    sql = "INSERT INTO customers (CustomerID, LastName,Email) VALUES (?,?,?)";
                    pst = connection.prepareStatement(sql);
                    pst.setInt(1, rs.getInt("CustomerID"));
                    pst.setString(2, lastName);
                    pst.setString(3, user);
                    pst.executeUpdate();
//                    调用登录函数，返回customersEntity实例
                    customersEntity = Login(user, pass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return customersEntity;
    }

    //    修改用户信息
    @Override
    public CustomersEntity update(int CustomerId, String lastName, String city, String country, String Postal, String phone, String email, String address) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        判定除了该用户外，是否含有相同输入的用户名或邮箱
        String sql = "SELECT * FROM customers WHERE (LastName=? OR Email=?) AND CustomerID!=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        CustomersEntity customersEntity = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, lastName);
            pst.setString(2, email);
            pst.setInt(3, CustomerId);
            rs = pst.executeQuery();
//            如果除了该用户外，含有相同输入的用户名或邮箱，返回null，提示用户
            if (rs.next()) {
                customersEntity = null;
            } else {
//                设置参数，修改用户详细信息
                sql = "UPDATE customers SET LastName=?,City=?,Country=?,Postal=?,Phone=?,Email=?,Address=? WHERE CustomerID=?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, lastName);
                pst.setString(2, city);
                pst.setString(3, country);
                pst.setString(4, Postal);
                pst.setString(5, phone);
                pst.setString(6, email);
                pst.setString(7, address);
                pst.setInt(8, CustomerId);
                pst.executeUpdate();
//                设置参数，修改customerlogon中的邮箱
                sql = "UPDATE customerlogon SET UserName=? WHERE CustomerID=?";
                pst = connection.prepareStatement(sql);
                pst.setString(1, email);
                pst.setInt(2, CustomerId);
                pst.executeUpdate();
//                返回用户新的用户实例
                customersEntity = getById(CustomerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return customersEntity;
    }

    //    通过用户ID获取用户
    @Override
    public CustomersEntity getById(int CustomerID) {
//        创建连接，初始化语句
        Connection connection = utilConnect.getConnection();
//        根据用户ID选择对应用户
        String sql = "SELECT * FROM customers WHERE CustomerID=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        CustomersEntity customersEntity = null;
        try {
//            执行sql语句
            pst = connection.prepareStatement(sql);
            pst.setInt(1, CustomerID);
            rs = pst.executeQuery();
            if (rs.next()) {
//                获取customersEntity实例
                customersEntity = buildCustomer(rs);
            } else {
                customersEntity = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
//            关闭连接
            utilConnect.close(rs, pst, connection);
        }
        return customersEntity;
    }

    //    创建CustomersEntity实例并返回
    private CustomersEntity buildCustomer(ResultSet rs) throws SQLException {
        return new CustomersEntity(rs.getInt("CustomerID"), rs.getString("FirstName"),
                rs.getString("LastName"), rs.getString("Address"),
                rs.getString("City"), rs.getString("Region"),
                rs.getString("Country"), rs.getString("Postal"),
                rs.getString("Phone"), rs.getString("Email"),
                rs.getString("Privacy"));
    }
}
