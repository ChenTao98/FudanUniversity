package dao;

import entity.ParkInfoEntity;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.InitEntityListUtil.*;

public class ParkInfoQueryDao {
    JDBCUtils jdbcUtils = new JDBCUtils();

    public ArrayList<ParkInfoEntity> query(String type) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * from park_info WHERE park_type=?");
            pst.setString(1, type);
            ResultSet resultSet = pst.executeQuery();
            return initParkInfoEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public int[] getAmount(String type) {
        int[] nums = new int[3];
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select count(*) from park_info where park_type='临时车位'");
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                nums[0] = resultSet.getInt(1);
            }
            pst = connection.prepareStatement("select count(*) from park_info where park_type='已租车位'");
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                nums[1] = resultSet.getInt(1);
            }
            pst = connection.prepareStatement("select count(*) from park_info where park_type='已购车位'");
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                nums[2] = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            return new int[3];
        }
        return nums;
    }
}
