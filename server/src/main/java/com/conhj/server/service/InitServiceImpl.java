package com.conhj.server.service;

import com.conhj.server.mapper.*;
import com.conhj.server.model.*;
import com.conhj.server.redis.dao.RedisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InitServiceImpl {

    @Autowired
    private RedisMapper rdao;

    @Autowired
    private CategoryMapper cdao;

    @Autowired
    private AccountMapper adao;

    @Autowired
    private ProfileMapper pdao;

    @Autowired
    private ProductMapper prdao;

    @Autowired
    private ItemMapper idao;

    @Autowired
    private OrdersMapper odao;

    @Autowired
    private CartMapper crdao;



    public void flushdb(){
        List list=new ArrayList();
      //  if(rdao.redisTemplate1==null) return ;
        rdao.executeRedisByLua(rdao.redisTemplate1, list,"flushDB.lua");
    }
    public void init(){
        flushdb();
        this.initAccount();
        this.initProfile();
        this.initCategory();
        this.initProduct();
        this.initItem();
        this.initOrders();
        this.initCart();
    }

    public void initOrders(){
        OrdersExample e=new OrdersExample();
        e.createCriteria().andOrderidIsNotNull();
        List<Orders>list=odao.selectByExample(e);
        list.forEach(a->{
            SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
            if(a.getOrderdate()!=null) {
                rdao.setString("orders:" + a.getUserid()+":"+a.getOrderid(),format1.format(a.getOrderdate())+":"+a.getTotalprice());
            }
        });
        AccountExample example1=new AccountExample();
        example1.createCriteria().andUseridIsNotNull();
        List<Account>list1=adao.selectByExample(example1);
//         List<A> firstA= AList.stream() .filter(a -> "hanmeimei".equals(a.getUserName())) .collect(Collectors.toList());
//          上面是集合，下面是单个的，查找username= hanmeimei的对象
// Optional<A> firstA= AList.stream() .filter(a -> "hanmeimei".equals(a.getUserName())) .findFirst();
//        if (firstA.isPresent()) {
//            A a = firstA.get();   //这样子就取到了这个对象呢。
//        }
        list1.forEach(c->{
            Optional<Orders>optional=list.stream().filter(o->c.getUserid()==o.getUserid()).max((o1,o2)->o1.getOrderid()-o2.getOrderid());
            if(optional.isPresent()){
                Orders o=optional.get();
                rdao.setString("maxid:"+c.getUserid(),(o.getOrderdate()==null?"0":"1")+o.getOrderid());
            }
            else{//当前用户的order为空
                //插入redis记录
                rdao.setString("maxid:"+c.getUserid(),"01");
                //同时更新mysql,为当前记录插入order
                Orders order=new Orders();
                order.setOrderid(1);
                order.setUserid(c.getUserid());
                odao.insert(order);
            }

        });

    }
    public void initCart(){
        CartExample e=new CartExample();
        e.createCriteria().andItemidIsNotNull();
        List<Cart>list=crdao.selectByExample(e);
        list.forEach(a->rdao.setString("carts:"+a.getUserid()+":"+a.getOrderid()+":"+a.getItemid(),a.getQuantity().toString()));
    }
    public void initAccount(){
        AccountExample e=new AccountExample();
        e.createCriteria().andUseridIsNotNull();
        List<Account>list=adao.selectByExample(e);
        list.forEach(a->rdao.setHashTable("account",a.getUsername(),a));
    }
    public void initItem(){
        ItemExample e=new ItemExample();
        e.createCriteria().andItemidIsNotNull();
        List<Item>list=idao.selectByExample(e);
        list.forEach(a->rdao.setSet("item:"+a.getProductid()+":"+a.getItemid(),a));
    }
    public void initProduct(){
        ProductExample p=new ProductExample();
        p.createCriteria().andProductidIsNotNull();
        List<Product>list=prdao.selectByExample(p);
        list.forEach(pr->rdao.setSet("product:"+pr.getCatid()+":"+pr.getProductid(),pr));
    }
    public void initCategory(){
        CategoryExample e=new CategoryExample();
        e.createCriteria().andCatidIsNotNull();
        List<Category>list=cdao.selectByExample(e);
        list.forEach(a->rdao.setHashTable("category",a.getCatid(),a));
    }
    public void initProfile(){
        ProfileExample e=new ProfileExample();
        e.createCriteria().andUseridIsNotNull();
        List<Profile>list=pdao.selectByExample(e);
        list.forEach(a->rdao.setHashTable("profile",a.getUserid().toString(),a));
    }


    public RedisMapper getRdao() {
        return rdao;
    }

    public void setRdao(RedisMapper rdao) {
        this.rdao = rdao;
    }
}
