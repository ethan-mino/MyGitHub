package org.boostcourse.dao;

import org.boostcourse.dao.sqls.MemberDaoSqls;
import org.boostcourse.dto.Member;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MemberDao {
    private NamedParameterJdbcTemplate jdbc;
    private RowMapper<Member> rowMapper = BeanPropertyRowMapper.newInstance(Member.class);
    private SimpleJdbcInsert insertAction;

    public MemberDao(DataSource dataSource){
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("member").usingGeneratedKeyColumns("id");
    }

    public Long addMember(Member member){
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Member getMemberByEmail(String email){
        Map<String, Object> params = new HashMap<>();
        params.put("email", email);

        return jdbc.queryForObject(MemberDaoSqls.SELECT_ALL_BY_EMAIL, params, rowMapper);
    }

}
