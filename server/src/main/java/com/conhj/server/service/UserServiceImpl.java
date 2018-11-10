package com.conhj.server.service;

import com.conhj.server.config.MyConst;
import com.conhj.server.mapper.AccountMapper;
import com.conhj.server.mapper.ProfileMapper;
import com.conhj.server.mapper.UserAccountDy;
import com.conhj.server.model.Account;
import com.conhj.server.redis.dao.RedisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Account login(Map<String,String> map){
        //Account a=adao.login(map);
        Object o=rdao.getHashTableByKey("account",map.get("username"));
        if(o==null){
            return null;
        }
        Account a=(Account)o;
        if(map.get("password").toString().equals(a.getPassword())){
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
        rdao.executeRedisByLua(list,"addCookie.lua");
        rdao.setExpire(MyConst.USERCOOKIE+":"+MyConst.SESSIONID1);
    }

    public void delCookie(String sessionid){
        List list=new ArrayList();
        list.add(sessionid);
        rdao.executeRedisByLua(list,"delCookie.lua");
    }
    public String verifyCookie(String sessionid){
        List list=new ArrayList();
        list.add(sessionid);
        List result=rdao.executeRedisByLua(list,"verCookie.lua");
        return result.get(0).toString();
    }

}