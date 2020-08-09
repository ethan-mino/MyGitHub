package org.boostcourse.service.impl;

import org.boostcourse.dao.GuestbookDao;
import org.boostcourse.dao.LogDao;
import org.boostcourse.dto.Guestbook;
import org.boostcourse.dto.Log;
import org.boostcourse.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class GuestbookServiceImpl implements GuestbookService {
    @Autowired
    GuestbookDao guestbookDao;

    @Autowired
    LogDao logDao;

    @Override
    @Transactional  // 내부적으로 read-only의 형태로 connection을 사용.
    public List<Guestbook> getGuestbooks(Integer start) {
        List<Guestbook> list = guestbookDao.selectAll(start, LIMIT);
        return list;
    }

    @Override
    @Transactional(readOnly = false) // read-only로 수행되면, transaction이 적용안됨.
    public int deleteGuestbook(Long id, String ip) {
        int deleteCount = guestbookDao.deleteById(id);

        Log log = new Log();
        log.setRegdate(new Date());
        log.setId(id);
        log.setIp(ip);
        log.setMethod("delete");

        logDao.insert(log);

        return deleteCount;
    }

    @Override
    @Transactional(readOnly = false) // read-only로 수행되면, transaction이 적용안됨.
    public Guestbook addGuestbook(Guestbook guestbook, String ip) {
        guestbook.setRegdate(new Date());
        Long id = guestbookDao.insert(guestbook);

        Log log = new Log();
        log.setId(id);
        log.setIp(ip);
        log.setMethod("insert");
        log.setRegdate(new Date());

        logDao.insert(log);
        return guestbook;
    }

    @Override
    public int getCount() {
        return guestbookDao.selectCount();
    }
}
