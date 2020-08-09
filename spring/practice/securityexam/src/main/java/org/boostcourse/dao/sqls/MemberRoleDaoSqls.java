package org.boostcourse.dao.sqls;

// email에 해당하는 권한 정보를 읽어들이기 위해서 member테이블과 member_role테이블을 조인(JOIN)하여 결과를 얻는 SQL을 가진 MemberRoleDaoSqls 클래스
public class MemberRoleDaoSqls {
    public static final String SELECT_ALL_BY_EMAIL = "SELECT mr.id, mr.member_id, mr.role_name " +
                                                     "FROM member_role mr JOIN member m ON mr.member_id = m.id  " +
                                                     "WHERE m.email = :email";
}
