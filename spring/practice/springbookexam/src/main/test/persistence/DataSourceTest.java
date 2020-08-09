package persistence;

import config.ApplicationConfig;
import config.DBConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class DataSourceTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Test
    public void connenctionTest(){
        try(Connection conn = dataSource.getConnection();){
            log.info(conn.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testMyBatis(){
        try(SqlSession session = sqlSessionFactory.openSession();
            Connection conn = session.getConnection()){
            log.info(session.toString());
            log.info(conn.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
