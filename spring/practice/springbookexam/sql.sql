create table tbl_board(
	bno INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	title TEXT NOT NULL,
	content TEXT NOT NULL,
	writer VARCHAR(50) NOT NULL,
	regdate DATETIME default now(),
	updatedate DATETIME default now()
);

insert into tbl_board values(1, '테스트 제목', '테스트 내용', 'user00', NOW(), NOW());