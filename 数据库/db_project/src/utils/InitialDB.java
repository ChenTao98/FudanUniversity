package utils;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class InitialDB {
    private static JDBCUtils jdbcUtils = new JDBCUtils();
    private static BufferedWriter bufferedWriter;
    private static String prefixString = "com.mysql.cj.jdbc.ClientPreparedStatement: ";

    public static void main(String[] args) throws SQLException, IOException {
//        bufferedWriter = new BufferedWriter(new FileWriter("init.txt"));
//        initPark();
//        initHousehold();
//        initRoom();
//        initDevice();
//        initBill();
//        initParkBuy();
//        initOut();
//        bufferedWriter.close();

        insert();

    //        test();
    }

    private static void initPark() throws IOException {
        String[] strings = {"A", "B", "C"};
        String[] type = {"临时", "租用", "购买"};
        int[] rent = {200, 250, 300};
        int[] buy = {200000, 220000, 240000};
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= 200; j++) {
                bufferedWriter.write("insert into park_info(community,park_num,park_type,price_rent,price_buy) values('" +
                        strings[i] + "'," + j + ",'临时车位'," + rent[i] + "," + buy[i] + ")\r\n");
            }
        }
    }

    private static void initHousehold() throws IOException {
        Random random = new Random(System.currentTimeMillis());
        /* 598 百家姓 */
        String[] Surname = {"赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许",
                "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜", "戚", "谢", "邹", "章", "云", "苏", "潘", "范", "彭", "司马", "上官", "欧阳",
                "司徒", "司空", "呼延", "端木",};
        String[] name2 = {"白", "歌", "斌", "一凡", "楷", "华建", "居易", "维", "涛", "俊逸",
                "冰冰", "思聪", "天明", "少羽", "渐离", "梦泪", "晓月", "宏达", "长苏",
                "飞流", "冬", "玉", "江", "紫薇", "尔康", "燕", "勇华", "子轩", "筠",
                "启文", "杰", "凯泉", "卫东", "晓明", "明台", "明华", "长卿", "景天", "紫萱", "雪见", "逍遥", "天河", "月如", "梦璃"};

        String[] strings = {"A", "B", "C"};
        String[] ids = {"352202", "310101", "440114"};
        String[] phone = {"187", "159", "181"};
        String[] year = {"1996", "1970", "1975", "1980", "1966", "1985"};
        String[] month = new String[12];
        for (int i = 1; i <= 12; i++) {
            month[i - 1] = i < 10 ? "0" + i : "" + i;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 100; j++) {
                int index = random.nextInt(Surname.length - 1);
                String name = Surname[index]; //获得一个随机的姓氏
                index = random.nextInt(name2.length - 1);
                name += name2[index];
//                name=Surname[(int)(Math.random()*Surname.length)]+name2[(int)(Math.random()*name2.length)];
                String jstr = j < 10 ? ("00" + j) : "0" + j;
                String phoneNum = phone[j % 3] + year[j % 6] + i + jstr;
                String id_card = ids[j % 3] + year[j % 6] + month[j % 12] + 15 + i + jstr;
                bufferedWriter.write("insert into household_info(household_id_card,household_name,household_phone,is_valid) values('" + id_card + "','" + name + "','" + phoneNum + "','1')\r\n");
            }
        }


    }

    private static void initRoom() throws IOException {
        String[] strings = {"A", "B", "C"};
        String[] area = {"120", "100", "90"};
        String[] unit = {"1", "2", "3", "4", "5", "6"};
        String[] price = {"1", "1.5", "2"};
        String[] room = new String[36];
        int count = 0;
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= 4; j++) {
                room[count] = "" + i + "0" + j;
                count++;
            }
        }
        count = 1;
        for (int i = 0; i < 3; i++) {
            String community = strings[i];
            for (int j = 0; j < 6; j++) {
                String unitS = unit[j];
                String areaS = area[j / 2];
                for (int k = 0; k < room.length; k++) {
                    if (count <= 300 && (i * j * room.length + k) % 2 == 0) {
                        bufferedWriter.write("insert into room_info(community,unit_num,room_num,room_area,price_per_m2,is_sold,household_id) values('"
                                + community + "'," + unitS + "," + room[k] + "," + areaS + "," + price[i] + ",'1'," + count + ")\r\n");
                        count++;
                    } else {
                        bufferedWriter.write("insert into room_info(community,unit_num,room_num,room_area,price_per_m2,is_sold) values('"
                                + community + "'," + unitS + "," + room[k] + "," + areaS + "," + price[i] + ",'0')\r\n");
                    }
                }
            }
        }
    }

    private static void initDevice() throws IOException {
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('电梯',500,'1')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('楼道灯',50,'1')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('门禁',100,'1')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('下水道',100,'1')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('消防栓',300,'1')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('健身器材',200,'0')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('公告牌',200,'0')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('照明灯',100,'0')\r\n");
        bufferedWriter.write("insert into device_info(device_name,device_charge,is_indoor) values('草坪',300,'0')\r\n");
    }

    private static void insert() throws IOException, SQLException {
        Connection connection = jdbcUtils.getConnection();
        connection.setAutoCommit(false);
        BufferedReader bufferedReader = new BufferedReader(new FileReader("init.txt"));
        PreparedStatement preparedStatement = null;
//        preparedStatement=connection.prepareStatement("insert into park_info(community,park_num,park_type) values('A',1,0)");
//        preparedStatement.executeUpdate();
        String a;
        while ((a = bufferedReader.readLine()) != null) {
            preparedStatement = connection.prepareStatement(a);
            preparedStatement.executeUpdate();
        }
        connection.commit();
        bufferedReader.close();
        jdbcUtils.close(null, preparedStatement, connection);
    }

    private static void initParkBuy() throws SQLException, IOException {
        Calendar calendar = Calendar.getInstance();
        Connection connection = jdbcUtils.getConnection();
        Timestamp timestamp;
        PreparedStatement pst_buy = connection.prepareStatement("insert into park_buy values (?,?,?,?)");
        PreparedStatement pst_income = connection.prepareStatement("insert into property_income values (?,?,?)");
        PreparedStatement pst_temp = connection.prepareStatement("insert into park_temp values (?,?,?,?)");
        PreparedStatement pst_bill = connection.prepareStatement("update household_bill set is_park_charge_pay=?,park_charge=? where bill_time=? and household_id in(select household_id from park_buy)");
        int count = 1;
        double[] doubles = {200000, 220000, 240000};
        String[] car = {"闽J23333", "沪A66666", "京C55555"};
        int[] id = {1, 201, 401};
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 3; j++) {
                int day = 1;
                for (int k = 0; k < 3; k++) {
                    calendar.set(2018, i, day, 12, 0, 0);
                    pst_buy.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
                    pst_buy.setDouble(2, doubles[j]);
                    pst_buy.setInt(3, id[j] + k + i * 3);
                    pst_buy.setInt(4, count);
                    bufferedWriter.write(pst_buy.toString().replace(prefixString, "") + "\r\n");
                    pst_temp.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
                    pst_temp.setString(2, car[j]);
                    pst_temp.setDouble(3, 5);
                    pst_temp.setInt(4, id[j] + k + i * 3 + 40);
                    bufferedWriter.write(pst_temp.toString().replace(prefixString, "") + "\r\n");
                    count++;
                    day++;
                }
            }
            calendar.set(2018, i, 1, 0, 0, 0);
            pst_bill.setString(1, "1");
            pst_bill.setDouble(2, 50);
            pst_bill.setTimestamp(3, new Timestamp(calendar.getTime().getTime()));
            bufferedWriter.write(pst_bill.toString().replace(prefixString, "") + "\r\n");
            pst_income.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
            pst_income.setString(2, "停车费用");
            pst_income.setDouble(3, 1980045 + (i + 1) * 9 * 50);
            bufferedWriter.write(pst_income.toString().replace(prefixString, "") + "\r\n");
            pst_income.setString(2, "物业费");
            pst_income.setDouble(3, 45900);
            bufferedWriter.write(pst_income.toString().replace(prefixString, "") + "\r\n");
            pst_income.setString(2, "广告费用");
            pst_income.setDouble(3, 3000);
            bufferedWriter.write(pst_income.toString().replace(prefixString, "") + "\r\n");
        }
//        PreparedStatement pre=connection.prepareStatement("with bill ( household_id, money ) as (select household_id,(room_area * price_per_m2) as money " +
//                "from room_info where household_id IS NOT NULL ) update household_bill set property_charge = bill.money," +
//                "is_pro_charge_pay= ? where bill.household_id =household_id");
//        pre.setString(1,"1");
//        bufferedWriter.write(pre.toString().replace(prefixString, "") + "\r\n");
        bufferedWriter.write("update household_bill set is_pro_charge_pay='1',is_park_charge_pay='1'\r\n");
        bufferedWriter.write("update household_bill set property_charge=120 where household_id <= 36\r\n");
        bufferedWriter.write("update household_bill set property_charge=100 where household_id > 36 and household_id <= 72\r\n");
        bufferedWriter.write("update household_bill set property_charge=90 where household_id > 72 and household_id <= 108\r\n");
        bufferedWriter.write("update household_bill set property_charge=180 where household_id > 108 and household_id <= 144\r\n");
        bufferedWriter.write("update household_bill set property_charge=150 where household_id > 144 and household_id <= 180\r\n");
        bufferedWriter.write("update household_bill set property_charge=135 where household_id > 180 and household_id <= 216\r\n");
        bufferedWriter.write("update household_bill set property_charge=240 where household_id > 216 and household_id <= 252\r\n");
        bufferedWriter.write("update household_bill set property_charge=200 where household_id > 252 and household_id <= 288\r\n");
        bufferedWriter.write("update household_bill set property_charge=180 where household_id > 288\r\n");
        bufferedWriter.write("update park_info set park_type = '已购车位' where park_id in (select park_id from park_buy)\r\n");
    }

    private static void initBill() throws SQLException, IOException {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst = connection.prepareStatement("insert into household_bill values (?,?,?,?,?,?)");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            calendar.set(2018, i, 1, 0, 0, 0);
            for (int j = 1; j <= 300; j++) {
                pst.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
                pst.setInt(2, j);
                pst.setDouble(3, 0);
                pst.setDouble(4, 0);
                pst.setString(5, "0");
                pst.setString(6, "0");
                bufferedWriter.write(pst.toString().replace(prefixString, "") + "\r\n");
            }
        }
    }

    private static void initOut() throws SQLException, IOException {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst_out = connection.prepareStatement("insert into property_outcome values (?,?,?)");
        PreparedStatement pst_ser = connection.prepareStatement("insert into service_record(service_time,device_type) values (?,?)");
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {

            for (int j = 1; j <= 5; j++) {
                calendar.set(2018, i, j, 12, 0, 0);
                pst_ser.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
                pst_ser.setInt(2, j);
                bufferedWriter.write(pst_ser.toString().replace(prefixString, "") + "\r\n");
            }
            calendar.set(2018, i, 7, 12, 0, 0);
            pst_ser.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
            pst_ser.setInt(2, 2);
            bufferedWriter.write(pst_ser.toString().replace(prefixString, "") + "\r\n");
            calendar.set(2018, i, 1, 0, 0, 0);
            pst_out.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
            pst_out.setString(2, "维修费用");
            pst_out.setDouble(3, 1100);
            bufferedWriter.write(pst_out.toString().replace(prefixString, "") + "\r\n");
        }
    }

    private static void test() throws SQLException {
        Connection connection = jdbcUtils.getConnection();
        PreparedStatement pst = connection.prepareStatement("insert into park_rent(rent_start_time,rent_end_time,household_id) values(?,?,?)");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2018, 5, 5, 0, 0, 0);
        pst.setTimestamp(2, new Timestamp(calendar.getTime().getTime()));
        pst.setInt(3, 5);
        System.out.println(pst.toString());
        pst = connection.prepareStatement("select household_id from park_rent where rent_end_time < ?");
        pst.setTimestamp(1, new Timestamp(calendar.getTime().getTime()));
        System.out.println(pst.toString());
        pst = connection.prepareStatement("select household_id from park_rent where rent_end_time < ?");
        pst.setTimestamp(1, new Timestamp(new Date().getTime()));
        System.out.println(pst.toString());
        jdbcUtils.close(null, pst, connection);
    }
}
