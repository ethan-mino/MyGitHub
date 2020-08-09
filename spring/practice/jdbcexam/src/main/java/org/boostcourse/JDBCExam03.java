package org.boostcourse;

import org.boostcourse.dao.RoleDao;
import org.boostcourse.dto.Role;

import java.util.List;

public class JDBCExam03 {
    public static void main(String[] args) {
        RoleDao roleDao = new RoleDao();
        List<Role> roleList = roleDao.getRoles();

        for(Role role : roleList){
            System.out.println(role);
        }
    }
}
