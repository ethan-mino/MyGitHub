package mapper;


import domain.BoardVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardMapper {
    @Select("SELECT * FROM tbl_board where bno > 0")
    public List<BoardVO> getList();
}
