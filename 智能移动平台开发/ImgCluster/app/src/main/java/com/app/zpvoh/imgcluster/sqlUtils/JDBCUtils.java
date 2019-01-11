package com.app.zpvoh.imgcluster.sqlUtils;

import android.util.Log;

import java.sql.*;

public class JDBCUtils {

//    private final static String URL = "jdbc:mysql://47.100.106.153:3306/as_db?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=UTF8";
    private final static String URL = "jdbc:mysql://47.100.106.153:3306/as_db?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true";
    private final static String USER = "root";
    private final static String PASSWORD = "123456";

    public JDBCUtils() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 得到连接
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            Log.d("connectFail","fail");
            e.printStackTrace();
        }
        return conn;
    }

    // 关闭结果集、语句和连接
    public void close(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

