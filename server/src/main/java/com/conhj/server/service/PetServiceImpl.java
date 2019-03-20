package com.conhj.server.service;

import com.conhj.server.mapper.CategoryMapper;
import com.conhj.server.model.Item;
import com.conhj.server.model.Product;
import com.conhj.server.redis.dao.RedisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PetServiceImpl {
    @Autowired
    private CategoryMapper cdao;
    @Autowired
    private RedisMapper rdao;

    public List queryCategory(){
        return rdao.getHashTable("category");
    }
    public List<Item>queryItem(String productid,String itemid){
        Set set=rdao.keysQuery("item:"+productid+":"+itemid) ;
        Set s1=rdao.unionSetObject(set);
        List<Item>list=new ArrayList(s1);
        return list;
    }
    public List<Item> querItems(String productid){
        Set set=rdao.keysQuery("item:"+productid+"*");
        Set s1=rdao.unionSetObject(set);
        List<Item> list=new ArrayList(s1);
        return list;
    }
    public List<Product>queryProduct(String categoryid){
        Set set=rdao.keysQuery("product:"+categoryid+"*");
        Set s1=rdao.unionSetObject(set);
        List<Product>list=new ArrayList(s1);
        return list;
    }


}
