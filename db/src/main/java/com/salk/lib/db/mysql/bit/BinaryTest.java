package com.salk.lib.db.mysql.bit;

import com.salk.lib.db.gbase.ByteBitMap;
import com.salk.lib.db.gbase.InsertData;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.EnumSet;

/**
 * @author salkli
 * @since 2023/4/3
 **/
public class BinaryTest {

    @Test
    public void insert() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = InsertData.JDBCTools.getConnection();
            String sql = "INSERT INTO t_bit(f_binary,f_binary2,f_binary3) VALUES(?,?,?)";
            ps = con.prepareStatement(sql);
            // 枚举成员用于表示某个用户的爱好
            // EnumSet<Hobby> hobbies = EnumSet.of(Hobby.FOOTBALL, Hobby.DANCING);
            // 将爱好映射到位图
            long[] bitmap = BitUtil.strToBits(bitStrArray(0, 20));
            ps.setBytes(1, BitUtil.toByteArray(bitmap));
            long[] bitmap2 = BitUtil.strToBits(bitStrArray(21, 50));
            ps.setBytes(2, BitUtil.toByteArray(bitmap2));
            long[] bitmap3 = BitUtil.strToBits(bitStrArray(0, 10));
            ps.setBytes(3, BitUtil.toByteArray(bitmap3));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            InsertData.JDBCTools.release(con, ps);
        }
    }

    @Test
    public void insertEnum() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = InsertData.JDBCTools.getConnection();
            String sql = "INSERT INTO t_bit(f_binary,f_binary2) VALUES(?,?)";
            ps = con.prepareStatement(sql);

            // 枚举成员用于表示某个用户的爱好
            //EnumSet<Hobby> hobbies = EnumSet.of(Hobby.FOOTBALL, Hobby.DANCING);
            // 将爱好映射到位图
            long[] bitmap = BitUtil.mapEnumToBits(Hobby.values());
            ps.setBytes(1, BitUtil.toByteArray(bitmap));
            EnumSet<Hobby> hobbies = EnumSet.of(Hobby.FOOTBALL, Hobby.DANCING);
            long[] bitmap2 = BitUtil.mapEnumToBits(hobbies.toArray(new Hobby[0]));
            ps.setBytes(2, BitUtil.toByteArray(bitmap2));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            InsertData.JDBCTools.release(con, ps);
        }
    }

    @Test
    public void selectHobby() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = InsertData.JDBCTools.getConnection();
            long[] bitmap = BitUtil.mapEnumToBits(Hobby.values());

            long[] query_bitmap = new long[bitmap.length];
            int football_index = Hobby.FOOTBALL.ordinal();
            query_bitmap[football_index / 64] |= 1L << (football_index % 64);

            PreparedStatement query_stmt = con.prepareStatement(
                "SELECT f_binary,f_binary2,f_binary3 FROM  t_bit WHERE f_binary & CAST(? AS UNSIGNED integer) != 0");
            query_stmt.setLong(1, BitUtil.byteArrayToLong(BitUtil.toByteArray(query_bitmap)));
            ResultSet rs = query_stmt.executeQuery();
            while (rs.next()) {
                long[] hobby_bitmap = BitUtil.toLongArray(rs.getBytes("f_binary"));
                System.out.println(" hobbies=" + BitUtil.toEnumSet(hobby_bitmap, Hobby.class));

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            InsertData.JDBCTools.release(con, ps);
        }
    }


    @Test
    public void select() {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = InsertData.JDBCTools.getConnection();

            long[] bitmap3 = BitUtil.strToBits(bitStrArray(0, 99));
            // 查询包含足球爱好的用户
            long[] query_bitmap = new long[10];
            int football_index = 5;
            query_bitmap[football_index / 64] |= 1L << (football_index % 64);

            PreparedStatement query_stmt = con.prepareStatement("SELECT f_binary,f_binary2,f_binary3 FROM  t_bit" +
                    " WHERE f_binary3 & CAST(? AS UNSIGNED INTEGER) != 0");
            query_stmt.setLong(1, BitUtil.byteArrayToLong(BitUtil.toByteArray(query_bitmap)));
            ResultSet rs = query_stmt.executeQuery();
            while (rs.next()) {
                long[] hobby_bitmap = BitUtil.toLongArray(rs.getBytes("f_binary"));
                //System.out.println("user_id=" + user_id + "; hobbies=" + toEnumSet(hobby_bitmap, Hobby.class));
                System.out.println(hobby_bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            InsertData.JDBCTools.release(con, ps);
        }
    }




    private String[] bitStrArray(int begin, int last) {
        String[] result = new String[last - begin];
        for (int i = 0; i < last-begin; i++) {
            result[i] = String.valueOf(begin+i);
        }
        System.out.println(result);
        return result;
    }

    // 枚举类型用于表示爱好选项
    enum Hobby {
        FOOTBALL, BASKETBALL, SWIMMING, DANCING
    }

}
