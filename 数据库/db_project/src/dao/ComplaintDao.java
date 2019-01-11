package dao;

import entity.ComplaintRecordEntity;
import entity.RepairByHouseEntity;
import entity.RepairByTypeEntity;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import static utils.DateUtil.dateToString;
import static utils.InitEntityListUtil.*;

public class ComplaintDao {
    JDBCUtils jdbcUtils = new JDBCUtils();
    QueryRunner queryRunner = new QueryRunner();

    public boolean insert(String community, int unit, int roomNum, String type, String content) {
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
            queryRunner.update(connection, "insert into complaint_record(complaint_time,complaint_type,complaint_content,is_process,room_id)" +
                    " values(?,?,?,?,?)", time, type, content, "0", roomId);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean update(int complaintId, String result, String process) {
        Connection connection = jdbcUtils.getConnection();
        try {
            int count = Integer.valueOf(queryRunner.query(connection, "select count(*) from complaint_record where complaint_id=? " +
                            "and is_process='0'",
                    new ScalarHandler(1), complaintId).toString());
            if (count == 0) {
                return false;
            }
            queryRunner.update(connection, "update complaint_record set is_process='1',process_result=?,how_process=? where complaint_id=?",
                    result, process, complaintId);
            connection.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public ArrayList<RepairByHouseEntity> queryHouse(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT community,unit_num,room_num,count(*) as num FROM complaint_record natural join room_info " +
                    "where complaint_time >= ? and complaint_time< ? group by community,unit_num,room_num order by num desc ");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initRepairByHouseEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<RepairByTypeEntity> queryType(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT complaint_type,count(*) as num FROM complaint_record" +
                    " where complaint_time >= ? and complaint_time< ? group by complaint_type order by num desc ");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            ArrayList<RepairByTypeEntity> arrayList = new ArrayList<>();
            while (resultSet.next()) {
                RepairByTypeEntity repairByTypeEntity = new RepairByTypeEntity();
                repairByTypeEntity.setDeviceName(resultSet.getString(1));
                repairByTypeEntity.setCount(resultSet.getInt(2));
                arrayList.add(repairByTypeEntity);
            }
            return arrayList;
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<ComplaintRecordEntity> queryComplaint(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM complaint_record where complaint_time >= ? and complaint_time< ? order by is_process");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initComplaintRecordEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
