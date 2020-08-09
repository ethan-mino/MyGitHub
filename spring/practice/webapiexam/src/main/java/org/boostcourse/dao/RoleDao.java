package org.boostcourse.dao;

import org.boostcourse.dto.Role;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RoleDao {
    private static String url = "jdbc:mysql://localhost:3306/example";
    private static String user = "root";
    private static String password = "1234";

    public List<Role> getRoles(){
        List<Role> roleList = new ArrayList<>();

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch(Exception e){
            e.printStackTrace();
        }

        String sql = "SELECT role_id, description FROM role";
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement ps = conn.prepareStatement(sql)){
            try(ResultSet rs = ps.executeQuery();){
                while(rs.next()){
                    String description = rs.getString("description");
                    int roleId = rs.getInt("role_id");

                    Role role = new Role(roleId, description);
                    roleList.add(role);
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return roleList;
    }
    public int addRole(Role role){
        int insertCount = 0;

        Connection conn= null;
        PreparedStatement ps = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO role(role_id, description) VALUES(?, ?)";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, role.getRoleId());
            ps.setString(2, role.getDescription());

            insertCount = ps.executeUpdate();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(ps != null){
                try{
                    ps.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try{
                    conn.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        return insertCount;
    }
    public Role getRollById(int roleId){
        Role role = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            String sql = "SELECT role_id, description FROM role WHERE role_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roleId);

            rs = ps.executeQuery();
            if(rs.next()){
                String description = rs.getString("description");
                int id = rs.getInt("role_id");

                role = new Role(id, description);
            }

            return role;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if(rs != null){
                try{
                    rs.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try{
                    ps.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try{
                    conn.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }

        return role;
    }
}
