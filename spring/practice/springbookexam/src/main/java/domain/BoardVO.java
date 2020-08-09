package domain;

import lombok.Data;

import java.util.Date;

@Data
public class BoardVO {
    private int bno;
    private String title;
    private String content;
    private String writer;
    private Date regDate;
    private Date updateDate;
}
