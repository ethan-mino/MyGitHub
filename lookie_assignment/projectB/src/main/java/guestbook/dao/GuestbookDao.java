package guestbook.dao;

import guestbook.dto.Guestbook;
import guestbook.utill.DBUtil;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GuestbookDao {
    String dbUrl = "jdbc:mysql://localhost:3306/lookie?useUnicode=true&characterEncoding=utf8&useSSL=false";
    String dbId = "root";
    String dbPassword = "!d8h6ukja123";

    public List<Guestbook> getGuestbooks(){
        List<Guestbook> list = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;

        try(Connection conn = DBUtil.getConnection(dbUrl, dbId, dbPassword)){
            String sql = "SELECT * FROM guestbook";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            // 조회 결과를 각 list에 추가
            while(rs.next()){
                long id = rs.getInt("id");
                String name = rs.getString("name");
                String content = rs.getString("content");
                Date regdate = rs.getDate("regdate");

                list.add(new Guestbook(id, name, content, regdate));
            }
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("DB Error");
        }

        // 조회 결과 
        return list;
    }

    public void addGuestbook(Guestbook guestbook){
        try(Connection conn = DBUtil.getConnection(dbUrl, dbId, dbPassword)){
            String name = guestbook.getName();
            String content = guestbook.getContent();
            Date regdate = guestbook.getRegdate();

            // 데이터 추가
            String sql = "INSERT INTO guestbook(name, content, regdate) VALUES(?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, content);
            ps.setDate(3, new java.sql.Date(regdate.getTime()));
            ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("DB Error");
        }
    }
}
