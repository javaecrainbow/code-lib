package com.salk.lib.db.gbase;

/**
 * @author salkli
 * @since 2023/3/30
 **/
import com.salk.lib.db.mysql.bit.BitUtil;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;

public class InsertData {
    public static void main(String[] args) {
        InsertData insertData=new InsertData();
        //insertData.getBlob();
        insertData.insertBlob();
    }
    public void getBlob(){//读取Blob数据
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = JDBCTools.getConnection();
            String sql = "SELECT varb1,varb2,f_binary_l FROM t_fields where varb1 is not null";
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
                byte[] picture = rs.getBytes(1);//得到Blob对象
                System.out.println(picture);

                byte[] picture1 = rs.getBytes(2);//得到Blob对象

                byte[] yihuo=new byte[picture.length];
                for (int i = 0; i < Math.min(picture.length, picture1.length); i++) {
                    yihuo[i]=(byte)(picture[i] ^ picture1[i]);

                }
                boolean result=true;
                for (int i = 0; i < Math.min(picture.length, yihuo.length); i++) {
                    if((yihuo[i]|picture[i])!=picture[i]){
                        result=false;
                    }

                }
                System.out.println(result);
                //开始读入文件
                System.out.println(picture1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertBlob(){//插入Blob
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = JDBCTools.getConnection();
            String sql = "INSERT INTO t_bit(f_binary_str,f_blob,f_binary_l,f_binary_l2) VALUES(?,?,?,?)";
            ps = con.prepareStatement(sql);
            ByteBitMap bitMap = new ByteBitMap(44);
            bitMap.add(2);
            bitMap.add(3);
            bitMap.add(43);
            //bitMap.add(64);
            //bitMap.add(456);
            //bitMap.add(32332);
            byte[] bits = bitMap.bits;
            ps.setBytes(1,bits);

            ByteBitMap bitMap2 = new ByteBitMap(44);
            bitMap2.add(2);
            bitMap2.add(3);
            bitMap2.add(5);
            bitMap2.add(43);
            //bitMap2.add(64);
            //bitMap2.add(456);
            //bitMap2.add(32332);
            byte[] bits2 = bitMap2.bits;

            ps.setBytes(2, bits2);
            long l = BitUtil.bytesToLong(bits);
            System.out.println(l);
            ps.setLong(3, l);
            long l2 = BitUtil.bytesToLong(bits2);
            ps.setLong(4, l2);
            System.out.println(l2);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            JDBCTools.release(con, ps);
        }
    }
    public static class JDBCTools {//JDBC工具类  用来建立连接和释放连接
        public static Connection getConnection() throws Exception{//连接数据库
            String driverClass = null;
            String url = null;
            String user = null;
            String password = null;

            Properties properties = new Properties();

            InputStream in = JDBCTools.class.getClassLoader().getResourceAsStream("jdbc.properties");
            properties.load(in);
            driverClass = properties.getProperty("driver");
            url = properties.getProperty("jdbcurl");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            Class.forName(driverClass);
            return DriverManager.getConnection(url, user, password);
        }
        public static void release(Connection con , Statement state){//关闭数据库连接
            if(state != null){
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        public static void release(ResultSet rs , Connection con , Statement state){//关闭数据库连接
            if(rs != null)
            {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(state != null){
                try {
                    state.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(con != null){
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
