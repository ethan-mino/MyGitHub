package org.boostcourse.dao;

public class GuestDaoSqls {
    public static final String SELECT_PAGING = "SELECT id, name, content, regdate FROM guestbook ORDER BY id DESC limit :start, :limit"; // "limit start, limit" -> start : 시작할 결과의 번호(SQL은 0부터 시작), limit : 반환할 결과의 수
    public static final String DELETE_BY_ID = "DELETE FROM guestbook WHERE id = :id";
    public static final String SELECT_COUNT = "SELECT count(*) FROM guestbook";
}
