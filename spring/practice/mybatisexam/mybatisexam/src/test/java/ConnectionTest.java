import config.ApplicationConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class ConnectionTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void TestConnection(){
        try(Connection conn = dataSource.getConnection()){
            Assert.assertNotNull(conn);
        }catch (Exception e){
            Assert.fail(e.getMessage());
        }
    }
}
