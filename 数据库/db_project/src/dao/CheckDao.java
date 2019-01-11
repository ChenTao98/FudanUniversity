package dao;

import entity.CheckRecordEntity;
import org.apache.commons.dbutils.QueryRunner;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static utils.DateUtil.dateToFirstDayMonth;
import static utils.DateUtil.dateToString;
import static utils.InitEntityListUtil.initCheckRecordEntityList;
import static utils.SqlUtil.insertOutcome;

public class CheckDao {
    JDBCUtils jdbcUtils = new JDBCUtils();
    QueryRunner queryRunner = new QueryRunner();

    public boolean insert(String type, String isNeed) {
        Connection connection = jdbcUtils.getConnection();
        String isIndoor = type.equals("室内") ? "1" : "0";
        try {
            queryRunner.update(connection, "insert into check_record(is_indoor,check_time,is_need_service) values(?,?,?)",
                    isIndoor, dateToString(new Date()), isNeed);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int insertService(String type, int checkId) {
        Connection connection = jdbcUtils.getConnection();
        try {
            Date date = new Date();
            PreparedStatement pst = connection.prepareStatement("select device_type,device_charge,is_indoor from device_info where device_name=?");
            pst.setString(1, type);
            ResultSet resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                return 3;
            }
            int deviceType = resultSet.getInt(1);
            double amount = resultSet.getDouble(2);
            String isIndoor = resultSet.getString(3);

            pst = connection.prepareStatement("select is_indoor from check_record where check_id=? and is_need_service='1'");
            pst.setInt(1, checkId);
            resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                return 1;
            }

            if (!isIndoor.equals(resultSet.getString(1))) {
                return 2;
            }

            String time = dateToFirstDayMonth(date);
            String timeTemp = dateToString(date);
            connection.setAutoCommit(false);
            queryRunner.update(connection, "insert into service_record(service_time,device_type,check_id) values(?,?,?)",
                    timeTemp, deviceType, checkId);
            insertOutcome(connection, time, "维修费用");
            queryRunner.update(connection, "update property_outcome set outcome_amount= outcome_amount+? where outcome_time=? and outcome_type=?",
                    amount, time, "维修费用");
            connection.commit();
            connection.close();
            return 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return 3;
            }
            return 3;
        }
    }

    public ArrayList<CheckRecordEntity> query(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from check_record where check_time>=?" +
                    " and check_time<?  order by check_time desc ");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initCheckRecordEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
