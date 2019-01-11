package utils;

import entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InitEntityListUtil {
    public static ArrayList<ParkInfoEntity> initParkInfoEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ParkInfoEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ParkInfoEntity parkInfoEntity=new ParkInfoEntity();
            parkInfoEntity.setParkId(resultSet.getInt(1));
            parkInfoEntity.setCommunity(resultSet.getString(2));
            parkInfoEntity.setParkNum(resultSet.getInt(3));
            parkInfoEntity.setParkType(resultSet.getString(4));
            parkInfoEntity.setPriceRent(resultSet.getInt(5));
            parkInfoEntity.setPriceBuy(resultSet.getInt(6));
            parkInfoEntity.setRentStartTime(resultSet.getTimestamp(7));
            parkInfoEntity.setRentEndTime(resultSet.getTimestamp(8));
            rowArrayList.add(parkInfoEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ParkTempEntity> initParkTempEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ParkTempEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ParkTempEntity parkTempEntity=new ParkTempEntity();
            parkTempEntity.setParkTime(resultSet.getTimestamp(1));
            parkTempEntity.setLicenseNum(resultSet.getString(2));
            parkTempEntity.setCharge(resultSet.getDouble(3));
            parkTempEntity.setParkId(resultSet.getInt(4));
            rowArrayList.add(parkTempEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ParkRentEntity> initParkRentEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ParkRentEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ParkRentEntity parkRentEntity=new ParkRentEntity();
            parkRentEntity.setRentStartTime(resultSet.getTimestamp(1));
            parkRentEntity.setRentEndTime(resultSet.getTimestamp(2));
            parkRentEntity.setCharge(resultSet.getDouble(3));
            parkRentEntity.setParkId(resultSet.getInt(4));
            parkRentEntity.setHouseholdId(resultSet.getInt(5));
            rowArrayList.add(parkRentEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ParkBuyEntity> initParkBuyEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ParkBuyEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ParkBuyEntity parkBuyEntity=new ParkBuyEntity();
            parkBuyEntity.setBuyTime(resultSet.getTimestamp(1));
            parkBuyEntity.setCharge(resultSet.getDouble(2));
            parkBuyEntity.setParkId(resultSet.getInt(3));
            parkBuyEntity.setHouseholdId(resultSet.getInt(4));
            rowArrayList.add(parkBuyEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<CheckRecordEntity> initCheckRecordEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<CheckRecordEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            CheckRecordEntity checkRecordEntity=new CheckRecordEntity();
            checkRecordEntity.setCheckId(resultSet.getInt(1));
            checkRecordEntity.setIsIndoor(resultSet.getString(2));
            checkRecordEntity.setCheckTime(resultSet.getTimestamp(3));
            checkRecordEntity.setIsNeedService(resultSet.getString(4));
            rowArrayList.add(checkRecordEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ComplaintRecordEntity> initComplaintRecordEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ComplaintRecordEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ComplaintRecordEntity complaintRecordEntity=new ComplaintRecordEntity();
            complaintRecordEntity.setComplaintId(resultSet.getInt(1));
            complaintRecordEntity.setComplaintTime(resultSet.getTimestamp(2));
            complaintRecordEntity.setComplaintType(resultSet.getString(3));
            complaintRecordEntity.setComplaintContent(resultSet.getString(4));
            complaintRecordEntity.setIsProcess(resultSet.getString(5));
            complaintRecordEntity.setRoomId(resultSet.getInt(6));
            complaintRecordEntity.setProcessResult(resultSet.getString(7));
            complaintRecordEntity.setHowProcess(resultSet.getString(8));
            rowArrayList.add(complaintRecordEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<DeviceInfoEntity> initDeviceInfoEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<DeviceInfoEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            DeviceInfoEntity deviceInfoEntity=new DeviceInfoEntity();
            deviceInfoEntity.setDeviceType(resultSet.getInt(1));
            deviceInfoEntity.setDeviceName(resultSet.getString(2));
            deviceInfoEntity.setDeviceCharge(resultSet.getDouble(3));
            deviceInfoEntity.setIsIndoor(resultSet.getString(4));
            rowArrayList.add(deviceInfoEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<HouseholdInfoEntity> initHouseholdInfoEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<HouseholdInfoEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            HouseholdInfoEntity householdInfoEntity=new HouseholdInfoEntity();
            householdInfoEntity.setHouseholdId(resultSet.getInt(1));
            householdInfoEntity.setHouseholdIdCard(resultSet.getString(2));
            householdInfoEntity.setHouseholdName(resultSet.getString(3));
            householdInfoEntity.setHouseholdPhone(resultSet.getString(4));
            householdInfoEntity.setIsValid(resultSet.getString(5));
            rowArrayList.add(householdInfoEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<HouseholdBillEntity> initHouseholdBillEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<HouseholdBillEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            HouseholdBillEntity householdBillEntity=new HouseholdBillEntity();
            householdBillEntity.setBillTime(resultSet.getTimestamp(1));
            householdBillEntity.setHouseholdId(resultSet.getInt(2));
            householdBillEntity.setPropertyCharge(resultSet.getDouble(3));
            householdBillEntity.setParkCharge(resultSet.getDouble(4));
            householdBillEntity.setIsProChargePay(resultSet.getString(5));
            householdBillEntity.setIsParkChargePay(resultSet.getString(6));
            rowArrayList.add(householdBillEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<PropertyIncomeEntity> initPropertyIncomeEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<PropertyIncomeEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            PropertyIncomeEntity propertyIncomeEntity=new PropertyIncomeEntity();
            propertyIncomeEntity.setIncomeTime(resultSet.getTimestamp(1));
            propertyIncomeEntity.setIncomeType(resultSet.getString(2));
            propertyIncomeEntity.setIncomeAmount(resultSet.getDouble(3));
            rowArrayList.add(propertyIncomeEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<PropertyOutcomeEntity> initPropertyOutcomeEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<PropertyOutcomeEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            PropertyOutcomeEntity propertyOutcomeEntity=new PropertyOutcomeEntity();
            propertyOutcomeEntity.setOutcomeTime(resultSet.getTimestamp(1));
            propertyOutcomeEntity.setOutcomeType(resultSet.getString(2));
            propertyOutcomeEntity.setOutcomeAmount(resultSet.getDouble(3));
            rowArrayList.add(propertyOutcomeEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<RepairRecordEntity> initRepairRecordEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<RepairRecordEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            RepairRecordEntity repairRecordEntity=new RepairRecordEntity();
            repairRecordEntity.setRepairId(resultSet.getInt(1));
            repairRecordEntity.setDeviceId(resultSet.getInt(2));
            repairRecordEntity.setRepairReason(resultSet.getString(3));
            repairRecordEntity.setRepairTime(resultSet.getTimestamp(4));
            repairRecordEntity.setIsService(resultSet.getString(5));
            repairRecordEntity.setRoomId(resultSet.getInt(6));
            rowArrayList.add(repairRecordEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<RoomInfoEntity> initRoomInfoEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<RoomInfoEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            RoomInfoEntity roomInfoEntity=new RoomInfoEntity();
            roomInfoEntity.setRoomId(resultSet.getInt(1));
            roomInfoEntity.setCommunity(resultSet.getString(2));
            roomInfoEntity.setUnitNum(resultSet.getInt(3));
            roomInfoEntity.setRoomNum(resultSet.getInt(4));
            roomInfoEntity.setRoomArea(resultSet.getInt(5));
            roomInfoEntity.setPricePerM2(resultSet.getDouble(6));
            roomInfoEntity.setIsSold(resultSet.getString(7));
            roomInfoEntity.setHouseholdId(resultSet.getInt(8));
            rowArrayList.add(roomInfoEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ServiceRecordEntity> initServiceRecordEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ServiceRecordEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            ServiceRecordEntity serviceRecordEntity=new ServiceRecordEntity();
            serviceRecordEntity.setServiceTime(resultSet.getTimestamp(1));
            serviceRecordEntity.setDeviceType(resultSet.getInt(2));
            serviceRecordEntity.setCheckId(resultSet.getInt(3));
            serviceRecordEntity.setRepairId(resultSet.getInt(4));
            rowArrayList.add(serviceRecordEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<RepairByHouseEntity> initRepairByHouseEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<RepairByHouseEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            RepairByHouseEntity repairByHouseEntity=new RepairByHouseEntity();
            repairByHouseEntity.setCommunity(resultSet.getString(1));
            repairByHouseEntity.setUnit(resultSet.getInt(2));
            repairByHouseEntity.setRoomNum(resultSet.getInt(3));
            repairByHouseEntity.setCount(resultSet.getInt(4));
            rowArrayList.add(repairByHouseEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<RepairByTypeEntity> initRepairByTypeEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<RepairByTypeEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            RepairByTypeEntity repairByTypeEntity=new RepairByTypeEntity();
            repairByTypeEntity.setDeviceType(resultSet.getInt(1));
            repairByTypeEntity.setDeviceName(resultSet.getString(2));
            repairByTypeEntity.setCount(resultSet.getInt(3));
            rowArrayList.add(repairByTypeEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<ServiceRowEntity> initServiceRowEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ServiceRowEntity> arrayList=new ArrayList<>();
        while (resultSet.next()){
            ServiceRowEntity rowEntity=new ServiceRowEntity();
            rowEntity.setServiceTime(resultSet.getTimestamp(1));
            rowEntity.setDeviceName(resultSet.getString(2));
            rowEntity.setAmount(resultSet.getDouble(3));
            rowEntity.setCheckId(resultSet.getInt(4));
            rowEntity.setRepairId(resultSet.getInt(5));
            arrayList.add(rowEntity);
        }
        return arrayList;
    }
    public static ArrayList<ServiceTypeEntity> initServiceTypeEntityList(ResultSet resultSet) throws SQLException {
        ArrayList<ServiceTypeEntity> arrayList=new ArrayList<>();
        while (resultSet.next()){
            ServiceTypeEntity entity=new ServiceTypeEntity();
            entity.setDeviceId(resultSet.getInt(1));
            entity.setDeviceName(resultSet.getString(2));
            entity.setAmount(resultSet.getDouble(3));
            entity.setCount(resultSet.getInt(4));
            arrayList.add(entity);
        }
        return arrayList;
    }
    public static ArrayList<PropertyIncomeEntity> initPropertyIncomeEntityListProfit(ResultSet resultSet) throws SQLException {
        ArrayList<PropertyIncomeEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            PropertyIncomeEntity propertyIncomeEntity=new PropertyIncomeEntity();
            propertyIncomeEntity.setIncomeTime(resultSet.getTimestamp(1));
            propertyIncomeEntity.setIncomeAmount(resultSet.getDouble(2));
            rowArrayList.add(propertyIncomeEntity);
        }
        return rowArrayList;
    }
    public static ArrayList<PropertyOutcomeEntity> initPropertyOutcomeEntityListProfit(ResultSet resultSet) throws SQLException {
        ArrayList<PropertyOutcomeEntity> rowArrayList=new ArrayList<>();
        while (resultSet.next()){
            PropertyOutcomeEntity propertyOutcomeEntity=new PropertyOutcomeEntity();
            propertyOutcomeEntity.setOutcomeTime(resultSet.getTimestamp(1));
            propertyOutcomeEntity.setOutcomeAmount(resultSet.getDouble(2));
            rowArrayList.add(propertyOutcomeEntity);
        }
        return rowArrayList;
    }
}
