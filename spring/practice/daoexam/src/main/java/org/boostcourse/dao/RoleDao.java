package org.boostcourse.dao;

import org.boostcourse.dto.Role;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.boostcourse.dao.RoleDaoSqls.*;

@Repository // Dao 객체에는 저장소의 역할을 한다는 의미로 repository 어노테이션을 사용
public class RoleDao {
    private NamedParameterJdbcTemplate jdbc;    // jdbcTemplate는 바인딩할 때 '?'를 사용하기 때문에 어떤 값이 매핑되는지 알아보기 힘든 문제가 있는데, NamedParameterJdbcTemplate는 이름을 이용해서 바인딩하거나, 결과값을 가져올 때 사용할 수 있다.
    private RowMapper<Role> rowMapper = BeanPropertyRowMapper.newInstance(Role.class);  // DB의 colunm명은 '_'로 단어를 구분하지만, 자바에서는 카멜 표기법을 사용하는데, BeanPropertyRowMapper에 DBMS와 JAVA의 naming 규칙을 맞춰주는 기능이 있음.
    private SimpleJdbcInsert insertAction;

    // spring version 4.3부터는 component scan으로 객체를 찾았을 때, 기본 생성자가 없다면 자동으로 객체를 주입해주기 때문에, DBConfig class에서 Bean으로 등록했던 dataSource가 파라미터로 전달됨.
    public RoleDao(DataSource dataSource){
        this.jdbc = new NamedParameterJdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource).withTableName("role");
    }

    public List<Role> selectAll(){
        return jdbc.query(SELECT_ALL, Collections.<String, Object>emptyMap(), rowMapper); // rowMapper는 query 결과를 dto에 저장하기 위한 목적으로 사용됨. (BeanPropertyRowMapper 객체를 이용해서 colunm의 값을 dto에 담아줌)
                                                                                          // query 메서드는 결과가 여러 건이었을 때 내부적으로 반복하면서 dto를 생성하고, 생성한 dto를 list에 담아서 반환하는 역할을 수행함.
    }

    public int insert(Role role){
        SqlParameterSource params = new BeanPropertySqlParameterSource(role);   // insert문은 자동으로 primary key를 자동으로 생성하고, 생성된 primary key를 다시 읽어와야 하는 경우도 있는데, SimpleJdbcInsert 객체가 그러한 일들을 수행해줌
                                                                                // 이번 예제에서는 primary key 값을 직접 넣어줌
                                                                                // new BeanPropertySqlParameterSource(role)는 role 객체에 있는 값을 Map으로 바꿔주는데, roldId를 rold_id로 바꿔서 맵 객체를 생성해줌.
        return insertAction.execute(params);
    }

    public int update(Role role){
        SqlParameterSource params = new BeanPropertySqlParameterSource(role);
        return jdbc.update(UPDATE, params);
    }

    public int deleteById(Integer id){
        Map<String, ?> params = Collections.singletonMap("roleId", id); // SqlParameterSource, BeanPropertySqlParameterSource를 사용해서 값을 전달해도 되지만, deleteById는 값을 하나만 사용하기 때문에 Collections.singletonMap 사용. (singletonMap은 키/값 쌍이 하나만 있을 때 유용하다.)
        return jdbc.update(DELETE_BY_ROLE_ID, params);
    }

    public Role selectById(Integer id){
        try{
            Map<String, ?> params = Collections.singletonMap("roleId", id);
            return jdbc.queryForObject(SELECT_BY_ROLE_ID, params, rowMapper);
        }catch(EmptyResultDataAccessException e){   // 조건에 맞는 row가 없을 경우 EmptyResultDataAccessException 발생
            return null;
        }
    }
}



