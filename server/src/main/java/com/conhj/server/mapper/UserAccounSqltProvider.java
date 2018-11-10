package com.conhj.server.mapper;

import org.apache.ibatis.jdbc.SQL;

import java.util.Map;


public class UserAccounSqltProvider {
    public String login(Map<String,String> par ){
        return new SQL(){
            {
                SELECT("*");
                FROM("account a");
                WHERE("a.username=#{username} " +
                        "and a.password=#{password}");
            }

        }.toString();
    }
}
