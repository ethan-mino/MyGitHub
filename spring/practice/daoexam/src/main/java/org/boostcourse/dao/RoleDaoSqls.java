package org.boostcourse.dao;

public class RoleDaoSqls {
    public static final String SELECT_ALL = "SELECT role_id, description FROM role order by role_id";
    // insert문은 simpleJdbcInsert 객체를 사용하면 query문 필요없음.
    public static final String UPDATE = "UPDATE role set description = :description where role_id = :roleId"; // :description, :roleId 부분이 값으로 바인딩 될 부분이다.
    // '*'보다는 컬럼명을 나열하는 것이 의미전달이 명확하다.
    public static final String SELECT_BY_ROLE_ID = "SELECT role_id, description FROM role where role_id = :roleId";
    public static final String DELETE_BY_ROLE_ID = "DELETE FROM role where role_id = :roleId";
}
