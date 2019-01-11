package dao;

import entity.ServiceRowEntity;
import entity.ServiceTypeEntity;
import org.apache.commons.dbutils.QueryRunner;
import utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static utils.InitEntityListUtil.initServiceRowEntityList;
import static utils.InitEntityListUtil.initServiceTypeEntityList;

public class ServiceDao {
    JDBCUtils jdbcUtils = new JDBCUtils();
    QueryRunner queryRunner = new QueryRunner();

    public ArrayList<ServiceRowEntity> getService(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT service_time,device_name,device_charge,check_id,repair_id" +
                    " from service_record natural join device_info where service_time>=? and service_time<?");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initServiceRowEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }

    public ArrayList<ServiceTypeEntity> getTypeEntity(String start, String end) {
        Connection connection = jdbcUtils.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement("SELECT device_type,device_name,device_charge,count(*) as num" +
                    " from service_record natural join device_info where service_time>=? and service_time<? " +
                    "group by device_type,device_name,device_charge order by num desc");
            pst.setString(1, start);
            pst.setString(2, end);
            ResultSet resultSet = pst.executeQuery();
            return initServiceTypeEntityList(resultSet);
        } catch (SQLException e) {
            return new ArrayList<>();
        }
    }
}
