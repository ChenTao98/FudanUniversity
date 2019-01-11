package utils;

import entity.TestHaveRow;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlUtil {
    static QueryRunner queryRunner = new QueryRunner();

    public static void insertIncome(Connection connection, String time, String type) throws SQLException {
        ArrayList<TestHaveRow> rowArrayList = (ArrayList<TestHaveRow>) queryRunner.query(connection, "Select income_type as row from property_income where income_time=? and income_type=?",
                new BeanListHandler<>(TestHaveRow.class), time, type);
        if (rowArrayList.size() == 0) {
            queryRunner.update(connection, "insert into property_income values(?,?,?)", time, type, 0);
        }
    }

    public static void insertOutcome(Connection connection, String time, String type) throws SQLException {
        ArrayList<TestHaveRow> rowArrayList = (ArrayList<TestHaveRow>) queryRunner.query(connection, "Select outcome_type as row from property_outcome where outcome_time=? and outcome_type=?",
                new BeanListHandler<>(TestHaveRow.class), time, type);
        if (rowArrayList.size() == 0) {
            queryRunner.update(connection, "insert into property_outcome values(?,?,?)", time, type, 0);
        }
    }

    public static ArrayList<TestHaveRow> isHouseholdExits(Connection connection, int id) throws SQLException {
        ArrayList<TestHaveRow> rowArrayList = (ArrayList<TestHaveRow>) queryRunner.query(connection, "select household_id as row from household_info where household_id=?",
                new BeanListHandler<>(TestHaveRow.class), id);
        return rowArrayList;
    }
}
