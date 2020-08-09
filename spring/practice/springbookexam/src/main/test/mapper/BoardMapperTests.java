package mapper;

import config.ApplicationConfig;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationConfig.class})
public class BoardMapperTests {

    private BoardMapper boardMapper;

    public BoardMapperTests(){};

    @Test
    public void testGetBoards(){
        //log.info(boardMapper.toString());
        //log.info(boardMapper.getList().toString());
    }
}
