package org.boostcourse.dao;

import org.boostcourse.dao.sqls.MemberRoleDaoSqls;
import org.boostcourse.dto.MemberRole;
import org.springframework.jdbc.SQLWarningException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//권한 정보를 읽어들이는 MemberRoleDao 클래스
@Repository
public class MemberRoleDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;

    // BeanPropertyRowMapper는 Role클래스의 프로퍼티를 보고 자동으로 칼럼과 맵핑해주는 RowMapper 객체를 생성한다.
    // roleId 프로퍼티는 role_id 칼럼과 맵핑된다.
    // 만약 프로퍼티와 칼럼의 규칙이 맞아 떨어지지 안흔다면 직접 RowMapper객체를 생성해야한다.
    // 생성하는 방법은 아래의 rowMapper2를 참고한다.
    private RowMapper<MemberRole> rowMapper = BeanPropertyRowMapper.newInstance(MemberRole.class);

    public MemberRoleDao (DataSource dataSource){
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("member_role").usingGeneratedKeyColumns("id");
    }

    public Long addMemberRole(MemberRole memberRole){
        SqlParameterSource params = new BeanPropertySqlParameterSource(memberRole);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<MemberRole> getRolesByEmail(String email){
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        return jdbc.query(MemberRoleDaoSqls.SELECT_ALL_BY_EMAIL, params, rowMapper);
    }
}
