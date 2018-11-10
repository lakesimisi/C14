package com.conhj.server.redis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisMapper {
    @Autowired
    public RedisTemplate<Serializable, Serializable> redisTemplate;
    @Autowired
    public RedisTemplate<Serializable,Serializable>redisTemplate1;

    public void setExpire(String key){
        redisTemplate.expire(key,30, TimeUnit.MINUTES);
    }

    public void setHashTable(String tablename,String key,Object value){
        HashOperations hs=redisTemplate.opsForHash();
        hs.put(tablename,key,value);
    }

    public Set<Object>unionSetObject(Set otherKeys){//有疑问
        SetOperations s=redisTemplate.opsForSet();
        return s.union("",otherKeys);
    }

    public void setSet(String key,Object value){
        SetOperations s=redisTemplate.opsForSet();
        s.add(key,value);
    }
    public Object getSet(String key){
        SetOperations s=redisTemplate.opsForSet();
         return s.members(key);
    }

    public void setString(String key,String value){
        ValueOperations vs=redisTemplate1.opsForValue();
        vs.set(key,value);

    }
    public String getString(String key){
        ValueOperations s=redisTemplate1.opsForValue();
        return s.get(key).toString();
    }

    public Set keysQuery(String key){
        Set set=redisTemplate.keys(key);
        return set;
    }
    public Object getHashTableByKey(String tableName,String key){
        HashOperations hash=redisTemplate.opsForHash();
        return hash.get(tableName,key);
    }
    public List getHashTable(String tableName){
        HashOperations hash=redisTemplate.opsForHash();
        List list=hash.values(tableName);
        return list;
    }
    public void del(String key){
        redisTemplate.delete(key);
    }
    public List executeRedisByLua(List args,String funName){
        DefaultRedisScript<List> script=new DefaultRedisScript();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(funName)));
        script.setResultType(List.class);
        List list=redisTemplate1.execute(script,args);
        return list;
    }







}
