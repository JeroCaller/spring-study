package com.jerocaller.model;

import org.apache.ibatis.jdbc.SQL;

public class QueryBuilder {
	private final String USER_TABLE = " users ";
	
	public String getQuerySelectOneUser() {
		return new SQL()
			.SELECT("user_no, id, password")
			.FROM(USER_TABLE)
			.WHERE(" id = #{ id} AND password = #{password}")
			.toString();
	}
}
