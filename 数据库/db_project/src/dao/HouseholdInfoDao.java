package dao;

import entity.HouseholdInfoEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.InitEntityListUtil.initHouseholdInfoEntityList;

public class HouseholdInfoDao {
    JDBCUtils jdbcUtils = new JDBCUtils();

    public int insertHousehold(String cardNum, String name, String phoneNum) {
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = jdbcUtils.getConnection();
        try {
            int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from household_info where (household_id_card=? or household_phone=?) and is_valid=?",
                    new ScalarHandler(1), cardNum, phoneNum, "1").toString());
            if (count > 0) {
                return 0;
            }
            queryRunner.update(connection, "insert into household_info (household_id_card,household_name,household_phone,is_valid) values(?,?,?,?)",
                    cardNum, name, phoneNum, "1");
            int result = Integer.valueOf(queryRunner.query(connection, "select LAST_INSERT_ID()", new ScalarHandler(1)).toString());
            connection.close();
            return result;
        } catch (SQLException e) {
            return 0;
        }
    }

    public boolean delete(int household_id) {
        QueryRunner queryRunner = new QueryRunner();
        Connection connection = jdbcUtils.getConnection();
        try {
            connection.setAutoCommit(false);
            queryRunner.update(connection, "update household_info set is_valid='0'where household_id=?", household_id);
            queryRunner.update(connection, "update room_info set household_id=null,is_sold='0' where household_id=?", household_id);
            queryRunner.update(connection, "update park_info set park_type='临时车位' where park_id in (select park_id from park_rent where household_id=?)", household_id);
            queryRunner.update(connection, "update park_info set park_type='临时车位' where park_id in (select park_id from park_buy where household_id=?)", household_id);
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                return false;
            }
            return false;
        }
    }

    public ArrayList<HouseholdInfoEntity> getHouseInfo() {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from household_info order by is_valid desc,household_id ASC  ");
            ResultSet resultSet = pst.executeQuery();
            ArrayList<HouseholdInfoEntity> arrayList = initHouseholdInfoEntityList(resultSet);
            connection.close();
            return arrayList;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public int[] getCount() {
        int[] intArray = new int[2];
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT count(*) from household_info group by is_valid");
            ResultSet resultSet = pst.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                intArray[i] = resultSet.getInt(1);
                i++;
            }
        } catch (SQLException e) {
            return new int[2];
        }
        return intArray;
    }
}
