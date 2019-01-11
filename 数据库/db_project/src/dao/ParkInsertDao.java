package dao;

import entity.ParkInfoEntity;
import entity.TestHaveRow;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static java.lang.Integer.parseInt;
import static utils.DateUtil.dateAdd;
import static utils.DateUtil.dateToFirstDayMonth;
import static utils.DateUtil.dateToString;
import static utils.InitEntityListUtil.initParkInfoEntityList;
import static utils.SqlUtil.insertIncome;
import static utils.SqlUtil.isHouseholdExits;

public class ParkInsertDao {
    QueryRunner queryRunner = new QueryRunner();
    private JDBCUtils jdbcUtils = new JDBCUtils();
    private String parkType = "停车费用";

    public boolean insertTemp(String community, String carNum, int parkNum, double amount) {
        Connection connection = jdbcUtils.getConnection();
        try {
            ArrayList<TestHaveRow> rowArrayList = getParkTempId(connection, community, parkNum);
            if (rowArrayList.size() == 0) {
                return false;
            }
            Date date = new Date();
            connection.setAutoCommit(false);
            queryRunner.update(connection, "insert into park_temp values(?,?,?,?)", dateToString(date), carNum, amount, parseInt(rowArrayList.get(0).getRow()));
            insertIncome(connection, dateToFirstDayMonth(date), parkType);
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, dateToFirstDayMonth(date), parkType);
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
        return true;
    }

    public int insertRent(String community, int parkNum, int year, int month, int household_id) {
        Connection connection = jdbcUtils.getConnection();
        try {
            ArrayList<ParkInfoEntity> rowParkTempInfo = getParkTempInfo(connection, community, parkNum);
            if (rowParkTempInfo.size() == 0) {
                return 1;
            }
            ArrayList<TestHaveRow> rowArrayList = isHouseholdExits(connection, household_id);
            if (rowArrayList.size() == 0) {
                return 2;
            }
            Date date = new Date();
            String startDay = dateToString(date);
            String endDay = dateAdd(date, year, month);
            double amount = year * 12 * rowParkTempInfo.get(0).getPriceRent() * 0.8 + month * rowParkTempInfo.get(0).getPriceRent();
            connection.setAutoCommit(false);
            queryRunner.update(connection, "insert into park_rent values(?,?,?,?,?)", startDay, endDay, amount, rowParkTempInfo.get(0).getParkId(), household_id);
            insertIncome(connection, dateToFirstDayMonth(date), parkType);
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, dateToFirstDayMonth(date), parkType);
            queryRunner.update(connection, "update park_info set park_type=?,rent_start_time=?,rent_end_time=? where park_id=?",
                    "已租车位", startDay, endDay, rowParkTempInfo.get(0).getParkId());
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                return 1;
            }
            return 1;
        }
        return 0;
    }

    public int insertBuy(String community, int parkNum, int household_id) {
        Connection connection = jdbcUtils.getConnection();
        try {
            ArrayList<ParkInfoEntity> rowParkTempInfo = getParkTempInfo(connection, community, parkNum);
            if (rowParkTempInfo.size() == 0) {
                return 1;
            }
            ArrayList<TestHaveRow> rowArrayList = isHouseholdExits(connection, household_id);
            if (rowArrayList.size() == 0) {
                return 2;
            }
            Date date = new Date();
            String time = dateToString(date);
            connection.setAutoCommit(false);
            queryRunner.update(connection, "insert into park_buy values(?,?,?,?)", time, rowParkTempInfo.get(0).getPriceBuy(), rowParkTempInfo.get(0).getParkId(), household_id);
            insertIncome(connection, dateToFirstDayMonth(date), parkType);
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    rowParkTempInfo.get(0).getPriceBuy(), dateToFirstDayMonth(date), parkType);
            queryRunner.update(connection, "update park_info set park_type=? where park_id=?",
                    "已购车位", rowParkTempInfo.get(0).getParkId());
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                return 1;
            }
            return 1;
        }

        return 0;
    }

    private ArrayList<TestHaveRow> getParkTempId(Connection connection, String community, int parkNum) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        ArrayList<TestHaveRow> rowArrayList = (ArrayList<TestHaveRow>) queryRunner.query(connection,
                "Select park_id as row from park_info where community= ? and park_num= ? and park_type= '临时车位'",
                new BeanListHandler<>(TestHaveRow.class), community, parkNum);
        return rowArrayList;
    }

    private ArrayList<ParkInfoEntity> getParkTempInfo(Connection connection, String community, int parkNum) throws SQLException {
        PreparedStatement pst = connection.prepareStatement("Select * from park_info where community= ? and park_num= ? and park_type= '临时车位'");
        pst.setString(1, community);
        pst.setInt(2, parkNum);
        ResultSet resultSet = pst.executeQuery();
        return initParkInfoEntityList(resultSet);
    }
}
