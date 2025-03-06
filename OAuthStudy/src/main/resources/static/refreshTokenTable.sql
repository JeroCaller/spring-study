-- forTest.sql문을 적용하여 관련 테이블 생성하지 않았다면 반드시 해당 쿼리문을 먼저 실행하여 
-- 필요한 테이블들을 먼저 생성해줘야 한다. 

CREATE DATABASE IF NOT EXISTS test_oauth;
USE test_oauth;

DROP TABLE IF EXISTS tb_refresh_token;
CREATE TABLE IF NOT EXISTS tb_refresh_token(
	id INT PRIMARY KEY AUTO_INCREMENT,
	refresh_token VARCHAR(1000) UNIQUE,
	user_id INT NOT NULL
);

ALTER TABLE tb_refresh_token
	ADD CONSTRAINT fk_tb_user_tb_refresh_token_user_id
		FOREIGN KEY (user_id) REFERENCES tb_user(id);