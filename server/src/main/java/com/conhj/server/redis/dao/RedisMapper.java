package com.conhj.server.redis.dao;
import com.conhj.server.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@AutoConfigureAfter(RedisConfig.class)
@Import(RedisConfig.class)
public class RedisMapper {

    @Resource(name="redisTemplate2")
    public RedisTemplate<String, Object> redisTemplate;

    @Resource(name="redisTemplate1")
    public RedisTemplate<String, Object> redisTemplate1;

    public RedisMapper( RedisTemplate<String, Object> redisTemplate2, RedisTemplate<String, Object> redisTemplate1){
        this.redisTemplate=redisTemplate2;
        this.redisTemplate1=redisTemplate1;
    }



//    // 在方法上加上注解@PostConstruct，这样方法就会在Bean初始化之后被Spring容器执行（注：Bean初始化包括，实例化Bean，并装配Bean的属性（依赖注入））。
//    @PostConstruct
//    public void init() {
//        redisMapper = this;
//    }


    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplate1() {
        return redisTemplate1;
    }

    public void setRedisTemplate1(RedisTemplate<String, Object> redisTemplate1) {
        this.redisTemplate1 = redisTemplate1;
    }

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
    public static List executeRedisByLua(RedisTemplate<String,Object> redisTemplate1, List args, String funName){
      //  if(redisTemplate1==null)return null;
        DefaultRedisScript<List> script=new DefaultRedisScript();
        script.setScriptSource(new ResourceScriptSource(new ClassPathResource(funName)));
        script.setResultType(List.class);
        List list= redisTemplate1.execute(script,args);
        return list;
    }







}
