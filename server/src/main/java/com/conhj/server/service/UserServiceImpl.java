package com.conhj.server.service;

import com.conhj.server.config.MyConst;
import com.conhj.server.mapper.AccountMapper;
import com.conhj.server.mapper.ProfileMapper;
import com.conhj.server.mapper.UserAccountDy;
import com.conhj.server.model.Account;
import com.conhj.server.redis.dao.RedisMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl {
    @Autowired
    private UserAccountDy adao;

    @Autowired
    private AccountMapper acdao;

    @Autowired
    private AccountMapper adao1;

    @Autowired
    private RedisMapper rdao;

    @Autowired
    private ProfileMapper pdao;
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendTest() {
        String context = "hello " + new Date();
        System.out.println("Sender : " + context);
        this.rabbitTemplate.convertAndSend("hello", context);
    }

    public Account login(Map<String,String> map){
        //Account a=adao.login(map);
        Object o=rdao.getHashTableByKey("account",map.get("username"));
        if(o==null){
            return null;
        }
        Account a=(Account)o;
        if(map.get("password").toString().equals(a.getPassword())){
            this.rabbitTemplate.convertAndSend("topic.login",a.getUserid() );
            return a;
        }else {
            return null;
        }

    }
    public void register(Account account){
        acdao.insert(account);
        account.getProfile().setUserid(account.getUserid());
        pdao.insert(account.getProfile());


        rdao.setHashTable("account",account.getUsername(),account);
        rdao.setHashTable("profile",account.getProfile().getUserid().toString(),account.getProfile());

    }
    public void addCookie(Account a,String sessionid){
        List list=new ArrayList();
        list.add(sessionid);
        list.add(a.getUserid()+":"+a.getUsername());
        RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"addCookie.lua");
        rdao.setExpire(MyConst.USERCOOKIE+":"+MyConst.SESSIONID1);
    }

    public void delCookie(String sessionid){
        List list=new ArrayList();
        list.add(sessionid);
        RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"delCookie.lua");
    }
    public String verifyCookie(String sessionid){
        List list=new ArrayList();
        list.add(sessionid);
        List result= RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"verCookie.lua");
        return result.get(0).toString();
    }

}