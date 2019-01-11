package dao;

import entity.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.InitEntityListUtil.initRoomInfoEntityList;
import static utils.SqlUtil.isHouseholdExits;

public class RoomInfoInsertDao {
    private JDBCUtils jdbcUtils = new JDBCUtils();

    public ArrayList<RoomInfoEntity> queryRoomInfo() {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst;
        try {
            pst = connection.prepareStatement("select * from room_info order by is_sold asc,room_id asc ");
            ResultSet resultSet = pst.executeQuery();
            return initRoomInfoEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public boolean exist(int household_id) {
        Connection connection = jdbcUtils.getConnection();
        ArrayList<TestHaveRow> rowArrayList;
        try {
            rowArrayList = isHouseholdExits(connection, household_id);
            return rowArrayList.size() != 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public int insertRoomInfo(int roomId, int household_id) {
        Connection connection = jdbcUtils.getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            connection.setAutoCommit(false);
            int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from room_info where room_id=? " +
                    "and household_id is not null", new ScalarHandler(1), roomId).toString());
            if (count != 0) {
                return 1;
            }
            PreparedStatement pst1 = connection.prepareStatement("update room_info set household_id= ?,is_sold='1' where room_id =? and household_id is null");
            pst1.setInt(1, household_id);
            pst1.setInt(2, roomId);
            pst1.execute();
            connection.commit();
            connection.close();
            return 0;
        } catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e1) {
                return 1;
            }
            return 1;
        }
    }
}
