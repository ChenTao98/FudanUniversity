package dao;

import entity.ParkBuyEntity;
import entity.ParkRentEntity;
import entity.ParkTempEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import static utils.DateUtil.*;
import static utils.InitEntityListUtil.initParkBuyEntityList;
import static utils.InitEntityListUtil.initParkRentEntityList;
import static utils.InitEntityListUtil.initParkTempEntityList;
import static utils.SqlUtil.insertIncome;

public class ParkQueryDao {
    JDBCUtils jdbcUtils = new JDBCUtils();


    public ArrayList<ParkTempEntity> queryTemp(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("select * from park_temp where park_time >= ? and park_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initParkTempEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<ParkBuyEntity> queryBuy(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("select * from park_buy where buy_time >= ? and buy_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initParkBuyEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<ParkRentEntity> queryRent(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement("select * from park_rent where rent_start_time >= ? and rent_start_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initParkRentEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public double[] getAmount(String startTime, String endTime) {
        double[] doubles = new double[3];
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select sum(charge) from park_temp where park_time >= ? and park_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                doubles[0] = resultSet.getDouble(1);
            }
            pst = connection.prepareStatement("select sum(charge) from park_rent where rent_start_time >= ? and rent_start_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                doubles[1] = resultSet.getDouble(1);
            }
            pst = connection.prepareStatement("select sum(charge) from park_buy where buy_time >= ? and buy_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                doubles[2] = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            return new double[3];
        }
        return doubles;
    }
}
