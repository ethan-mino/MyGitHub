package org.boostcourse;

import org.boostcourse.dao.RoleDao;
import org.boostcourse.dto.Role;

public class JDBCExam02 {
    public static void main(String[] args) {
        int roleId = 500;
        String description = "CTO";

        Role role = new Role(roleId, description);

        RoleDao roleDao = new RoleDao();
        int insertCount = roleDao.addRole(role);

        System.out.println(insertCount);
    }
}
