package org.boostcourse.dao;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.dto.Guestbook;
import org.boostcourse.dto.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class GuestbookDaoTest {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        GuestbookDao guestbookDao = ac.getBean(GuestbookDao.class);

/*        Guestbook guestbook = new Guestbook();
        guestbook.setName("길민호");
        guestbook.setContent("반갑습니다. 여러분");
        guestbook.setRegdate(new Date());
        Long id = guestbookDao.insert(guestbook);
        System.out.println("id : " + id);*/

/*        LogDao logDao = ac.getBean(LogDao.class);
        Log log = new Log();
        log.setIp("127.0.0.1");
        log.setMethod("Insert");
        log.setRegdate(new Date());

        logDao.insert(log); */
    }
}
