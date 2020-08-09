package org.boostcourse;

import org.boostcourse.dao.RoleDao;
import org.boostcourse.dto.Role;

public class JDBCExam01 {
    public static void main(String[] args) {
        RoleDao dao = new RoleDao();
        Role role = dao.getRollById(100);

        System.out.println(role);
    }
}
