package org.boostcourse.service.impl;

import org.boostcourse.config.ApplicationConfig;
import org.boostcourse.dto.Guestbook;
import org.boostcourse.service.GuestbookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Date;

public class GuestbookServiceTest {
    public static void main(String[] args) {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        GuestbookService guestbookService = ac.getBean(GuestbookService.class);

        Guestbook guestbook = new Guestbook();
        guestbook.setName("강경미");
        guestbook.setContent("반갑습니다. 열어분들~");
        guestbook.setRegdate(new Date());

        guestbookService.addGuestbook(guestbook, "127.0.0.1");
    }
}
