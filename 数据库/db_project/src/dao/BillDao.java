package dao;

import entity.HouseholdBillEntity;
import entity.HouseholdInfoEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static utils.DateUtil.dateToFirstDayMonth;
import static utils.DateUtil.dateToString;
import static utils.InitEntityListUtil.initHouseholdBillEntityList;
import static utils.InitEntityListUtil.initHouseholdInfoEntityList;
import static utils.SqlUtil.insertIncome;

public class BillDao {
    JDBCUtils jdbcUtils = new JDBCUtils();

    public boolean initBill() {
        Date date = new Date();
        QueryRunner queryRunner = new QueryRunner();
        String timeDay = dateToString(date);
        String timeFirst = dateToFirstDayMonth(date);
        Connection connection = jdbcUtils.getConnection();

        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM household_info where is_valid='1'");
            ArrayList<HouseholdInfoEntity> householdInfoEntities = initHouseholdInfoEntityList(pst.executeQuery());
            connection.setAutoCommit(false);
            for (HouseholdInfoEntity householdInfoEntity : householdInfoEntities) {
                int householdId = householdInfoEntity.getHouseholdId();
                int hasInsert = Integer.valueOf(queryRunner.query(connection, "select count(*) from household_bill where household_id=? and bill_time=?",
                        new ScalarHandler(1), householdId, timeFirst).toString());
                if (hasInsert != 0) {
                    continue;
                }
                int countPark = Integer.valueOf(queryRunner.query(connection, "select count(*) from park_buy where household_id=?",
                        new ScalarHandler(1), householdId).toString());
                countPark += Integer.valueOf(queryRunner.query(connection, "select count(*) from park_rent where household_id=? and rent_end_time > ?",
                        new ScalarHandler(1), householdId, timeDay).toString());
                int parkAmount = countPark * 50;
                pst = connection.prepareStatement("select sum(room_area * price_per_m2) from room_info where household_id=?");
                pst.setInt(1, householdId);
                ResultSet resultSet = pst.executeQuery();
                double proAmount = 0;
                while (resultSet.next()) {
                    proAmount = resultSet.getDouble(1);
                }
                String isParkPay = parkAmount == 0 ? "1" : "0";
                queryRunner.update(connection, "insert into household_bill values(?,?,?,?,?,?)", timeFirst, householdId, proAmount, parkAmount, "0", isParkPay);
            }
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }

    }

    public ArrayList<HouseholdBillEntity> getBill() {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM household_bill where is_pro_charge_pay='0' or is_park_charge_pay='0'");
            ArrayList<HouseholdBillEntity> billEntities = initHouseholdBillEntityList(pst.executeQuery());
            connection.close();
            return billEntities;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public boolean setPro(int householdId, double amount, String time) {
        Connection connection = jdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            connection.setAutoCommit(false);
            queryRunner.update(connection, "update household_bill set is_pro_charge_pay='1' where bill_time = ? and household_id=?", time, householdId);
            insertIncome(connection, time, "物业费");
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, time, "物业费");
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }

    public boolean setPark(int householdId, double amount, String time) {
        Connection connection = jdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            connection.setAutoCommit(false);
            queryRunner.update(connection, "update household_bill set is_park_charge_pay='1' where bill_time = ? and household_id=?", time, householdId);
            insertIncome(connection, time, "停车费用");
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, time, "停车费用");
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }

    public boolean setProAll() {
        Connection connection = jdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String time = dateToFirstDayMonth(new Date());
        try {
            connection.setAutoCommit(false);
            PreparedStatement pst = connection.prepareStatement("select sum(property_charge) from household_bill where is_pro_charge_pay='0'");
            ResultSet resultSet = pst.executeQuery();
            double amount = 0;
            while (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
            queryRunner.update(connection, "update household_bill set is_pro_charge_pay='1' where is_pro_charge_pay='0'");
            insertIncome(connection, time, "物业费");
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, time, "物业费");
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }

    public boolean setParkAll() {
        Connection connection = jdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        String time = dateToFirstDayMonth(new Date());
        try {
            connection.setAutoCommit(false);
            PreparedStatement pst = connection.prepareStatement("select sum(park_charge) from household_bill where is_park_charge_pay='0'");
            ResultSet resultSet = pst.executeQuery();
            double amount = 0;
            while (resultSet.next()) {
                amount = resultSet.getDouble(1);
            }
            queryRunner.update(connection, "update household_bill set is_park_charge_pay='1' where is_park_charge_pay='0'");
            insertIncome(connection, time, "停车费用");
            queryRunner.update(connection, "update property_income set income_amount= income_amount+? where income_time=? and income_type=?",
                    amount, time, "停车费用");
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }
}
