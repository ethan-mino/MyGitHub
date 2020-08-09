package org.boostcourse.dao.sqls;

// Member DAO에서 읽어들일 SQL정보를 가지고 있는 MemberDaoSqls클래스
public class MemberDaoSqls {
    public static final String SELECT_ALL_BY_EMAIL = "SELECT id, name, password, email, create_date, modify_date " +
                                                     "FROM member " +
                                                     "WHERE email = :email";
}
