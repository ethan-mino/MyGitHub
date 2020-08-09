package org.boostcourse.dao;

import org.boostcourse.dto.Log;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LogDao {
    private NamedParameterJdbcTemplate jdbc;
    private SimpleJdbcInsert insertAction;

    public LogDao(DataSource dataSource){
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("Log")
                .usingGeneratedKeyColumns("id");    // id가 자동으로 입력됨.
    }

    public Long insert(Log log){
        SqlParameterSource params = new BeanPropertySqlParameterSource(log);
        return insertAction.executeAndReturnKey(params).longValue();    // executeAndReturnKey() 메서드는 insert문을 내부적으로 생성해서 실행하고, 자동으로 생성된 id값을 리턴.
    }
}
