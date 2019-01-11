package dao;

import entity.PropertyIncomeEntity;
import entity.PropertyOutcomeEntity;
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
import static utils.InitEntityListUtil.initPropertyIncomeEntityList;
import static utils.InitEntityListUtil.initPropertyOutcomeEntityList;
import static utils.SqlUtil.insertIncome;

public class PropertyInAndOutDao {
    private JDBCUtils jdbcUtils = new JDBCUtils();

    public ArrayList<PropertyIncomeEntity> getIncome(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from property_income where income_time >= ? and income_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initPropertyIncomeEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<PropertyOutcomeEntity> getOutcome(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from property_outcome where outcome_time >= ? and outcome_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initPropertyOutcomeEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public double getOutcomeType(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select sum(outcome_amount) from property_outcome where outcome_time >= ? and outcome_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            resultSet.next();
            return resultSet.getDouble(1);
        } catch (SQLException e) {
            return 0;
        }
    }

    public double[] getIncomeType(String startTime, String endTime) {
        double[] amount = new double[4];
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select income_type,sum(income_amount) from property_income " +
                    "where income_time >= ? and income_time < ? group by income_type");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                switch (resultSet.getString(1)) {
                    case "停车费用":
                        amount[0] = resultSet.getDouble(2);
                        break;
                    case "物业费":
                        amount[1] = resultSet.getDouble(2);
                        break;
                    case "广告费用":
                        amount[2] = resultSet.getDouble(2);
                        break;
                    case "其他收入":
                        amount[3] = resultSet.getDouble(2);
                        break;
                }
            }
            return amount;
        } catch (SQLException e) {
            return new double[4];
        }
    }

    public boolean insert(String type, double amount) {
        QueryRunner queryRunner = new QueryRunner();
        Date date = new Date();
        String time = dateToFirstDayMonth(date);
        Connection connection = jdbcUtils.getConnection();
        try {
            insertIncome(connection, time, type);
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?", amount, time, type);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
