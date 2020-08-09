package org.boostcourse.main;

import javafx.application.Application;
import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.dao.RoleDao;
import org.boostcourse.dto.Role;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sun.java2d.SurfaceDataProxy;

import javax.sql.DataSource;

public class JDBCTest {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);

        RoleDao roleDao = ac.getBean(RoleDao.class);
        Role role = new Role();
        role.setDescription("PROGRAMMER");
        role.setRoleId(500);

/*
        int count = roleDao.insert(role);
        System.out.println(count + " 건 입력하였습니다. ");
        int count = roleDao.update(role);
        System.out.println(count + " 건 수정하였습니다. ");

        Role selectResult = roleDao.selectById(102);
        System.out.println(selectResult);

        int deleteCount = roleDao.deleteById(103);
        System.out.println(deleteCount + " 건 삭제되었습니다.");
*/

        Role selectResult2 = roleDao.selectById(103);
        System.out.println(selectResult2);
    }
}
