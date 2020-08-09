package org.boostcourse.service;

import org.boostcourse.dto.Guestbook;

import java.util.List;

public interface GuestbookService {
    public static final Integer LIMIT = 5;
    public List<Guestbook> getGuestbooks (Integer start);   // 방명록 정보를 페이지 별로 읽어오기
    public int deleteGuestbook(Long id, String ip); // 방명록 삭제
    public Guestbook addGuestbook(Guestbook guestbook, String ip);  // 방명록 추가
    public int getCount();  // 페이징 처리를 위해서 전체 건수 구하기
}
