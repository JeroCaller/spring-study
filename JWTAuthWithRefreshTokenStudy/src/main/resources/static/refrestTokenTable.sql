-- forTest.sql문을 적용하여 관련 테이블 생성하지 않았다면 반드시 해당 쿼리문을 먼저 실행하여 
-- 필요한 테이블들을 먼저 생성해줘야 한다. 

CREATE DATABASE IF NOT EXISTS test_auth;
USE test_auth;

DROP TABLE IF EXISTS tb_refresh_token;
CREATE TABLE IF NOT EXISTS tb_refresh_token(
	id INT PRIMARY KEY AUTO_INCREMENT,
	refresh_token VARCHAR(1000) UNIQUE,
	before_token VARCHAR(1000) UNIQUE, -- RTR 방식 이용 시 교체된 가장 최근의 리프레시 토큰를 담기 위함.
	is_invalid VARCHAR(20) NOT NULL, -- 로그아웃 등의 이유로 현재 리프레시 토큰이 무효한지 여부.
	created_at DATETIME,
	updated_at DATETIME,
	user_id INT NOT NULL
);

ALTER TABLE tb_refresh_token
	ADD CONSTRAINT fk_tb_user_tb_refresh_token_user_id
		FOREIGN KEY (user_id) REFERENCES tb_user(id);