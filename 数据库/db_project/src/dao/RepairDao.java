package dao;

import entity.RepairByHouseEntity;
import entity.RepairByTypeEntity;
import entity.RepairRecordEntity;
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
import static utils.InitEntityListUtil.initRepairByHouseEntityList;
import static utils.InitEntityListUtil.initRepairByTypeEntityList;
import static utils.InitEntityListUtil.initRepairRecordEntityList;
import static utils.SqlUtil.insertOutcome;

public class RepairDao {
    JDBCUtils jdbcUtils = new JDBCUtils();
    QueryRunner queryRunner = new QueryRunner();

    public boolean insert(String community, int unit, int roomNum, String type, String reason) {
        Connection connection = jdbcUtils.getConnection();
        String time = dateToString(new Date());
        try {
            PreparedStatement pst = connection.prepareStatement("select room_id from room_info where community=? and unit_num=? and room_num=? " +
                    "and household_id is not null");
            pst.setString(1, community);
            pst.setInt(2, unit);
            pst.setInt(3, roomNum);
            ResultSet resultSet = pst.executeQuery();
            if (!resultSet.next()) {
                return false;
            }
            int roomId = resultSet.getInt(1);
            int deviceType = Integer.valueOf(queryRunner.query(connection, "select device_type from device_info where device_name=?",
                    new ScalarHandler(1), type).toString());
            queryRunner.update(connection, "insert into repair_record(device_type,repair_reason,repair_time,is_service,room_id) values(?,?,?,?,?)",
                    deviceType, reason, time, "0", roomId);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<RepairRecordEntity> queryRepair(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM repair_record where repair_time >= ? and repair_time< ? order by is_service");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initRepairRecordEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<RepairByHouseEntity> queryByHouse(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT community,unit_num,room_num,count(*) FROM repair_record natural join room_info " +
                    "where repair_time >= ? and repair_time< ? group by community,unit_num,room_num");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initRepairByHouseEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<RepairByTypeEntity> queryByType(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT device_type,device_name,count(*) FROM repair_record natural join device_info " +
                    "where repair_time >= ? and repair_time< ? group by device_type,device_name");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initRepairByTypeEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public boolean insertService(int type, int repairId) {
        Connection connection = jdbcUtils.getConnection();
        try {
            double amount = Double.valueOf(queryRunner.query(connection, "select device_charge from device_info where device_type=?",
                    new ScalarHandler(1), type).toString());
            String time = dateToFirstDayMonth(new Date());
            connection.setAutoCommit(false);
            queryRunner.update(connection, "update repair_record set is_service='1' where repair_id=?", repairId);
            queryRunner.update(connection, "insert into service_record(service_time,device_type,repair_id) values(?,?,?)",
                    dateToString(new Date()), type, repairId);
            insertOutcome(connection, time, "维修费用");
            queryRunner.update(connection, "update property_outcome set outcome_amount= outcome_amount+? where outcome_time=? and outcome_type=?",
                    amount, time, "维修费用");
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

    ;
}
