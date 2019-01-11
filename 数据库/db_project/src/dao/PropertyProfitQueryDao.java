package dao;


import entity.*;

import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.InitEntityListUtil.*;

public class PropertyProfitQueryDao {
    JDBCUtils jdbcUtils = new JDBCUtils();

    public ArrayList<PropertyIncomeEntity> queryIncome(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement("select income_time, sum(income_amount) from property_income WHERE income_time >= ? AND income_time <= ? group by income_time ");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initPropertyIncomeEntityListProfit(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<PropertyOutcomeEntity> queryOutcome(String startTime, String endTime) {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement("select outcome_time, sum(outcome_amount) from property_outcome WHERE outcome_time >= ? AND outcome_time < ? group by outcome_time");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            return initPropertyOutcomeEntityListProfit(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public double[] queryProfit(String startTime, String endTime) {
        double[] doubles = new double[3];
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT sum(income_amount) FROM property_income WHERE income_time >= ? AND income_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            ResultSet resultSet = pst.executeQuery();
            if (resultSet.next()) {
                doubles[0] = resultSet.getDouble(1);
            }
            pst = connection.prepareStatement("SELECT sum(outcome_amount) FROM property_outcome WHERE outcome_time >= ? AND outcome_time < ?");
            pst.setString(1, startTime);
            pst.setString(2, endTime);
            resultSet = pst.executeQuery();
            if (resultSet.next()) {
                doubles[1] = resultSet.getDouble(1);
            }
            doubles[2] = doubles[0] - doubles[1];
        } catch (SQLException e) {
            return new double[3];
        }
        return doubles;
    }
}
