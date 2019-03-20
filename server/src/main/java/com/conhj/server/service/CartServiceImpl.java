package com.conhj.server.service;

import com.conhj.server.mapper.CartMapper;
import com.conhj.server.mapper.OrdersMapper;
import com.conhj.server.model.Cart;
import com.conhj.server.model.CartKey;
import com.conhj.server.model.Item;
import com.conhj.server.model.Orders;
import com.conhj.server.redis.dao.RedisMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CartServiceImpl {


    @Autowired
    private CartMapper cdao;

    @Autowired
    private OrdersMapper odao;
    @Autowired
    private RedisMapper  rdao;
    @Autowired
    private AmqpTemplate rabbitTemplate;




    public String addCart(Cart cart){
        boolean iu=true; //insert-true update-false
         List list=new ArrayList();
        list.add(cart.getUserid()+"");
        List result= RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"getMaxOrderidByUserid.lua");

        String sid=result.get(0).toString().substring(1);
        String flag=result.get(0).toString().substring(0,1);

        int id=Integer.parseInt(sid);
        if(flag.equals("0")){//可以继续购物
            //需要把相同物品的数量相加
            cart.setOrderid(id);
            list.clear();
            list.add("carts:"+cart.getUserid()+":"+cart.getOrderid()+":"+cart.getItemid());
            List ll= RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"getQuantity.lua");
            if(ll.get(0)!=null){//老商品
                cart.setQuantity(Integer.parseInt(ll.get(0).toString())+cart.getQuantity());
                iu=false;
            }
        }else {
            ++id;
            cart.setOrderid(id);
            Orders neworders=new Orders();

            //更新orders mysql
            neworders.setOrderid(id);
            neworders.setUserid(cart.getUserid());
            odao.insert(neworders);
        }
        //更新mysql cart
        if(!iu){
            cdao.updateByPrimaryKey(cart);
        }else{
            cdao.insert(cart);
        }
        //上面是存储cartMysql 下面是redis cart
//        String str= result.get(0).toString();老师为什么这么写？
//        String f=str.substring(1);//取出的是是否已经提交的标志位 0：继续购物 1：已经交钱了
//        list.add(f+"");
//        rdao.executeRedisByLua(list,"addCart.lua");
        list.clear();
        list.add(cart.getUserid()+"");
        list.add(cart.getOrderid()+"");
        list.add(cart.getItemid()+"");
        list.add(cart.getQuantity()+"");
        RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"addCart.lua");
        //String str=result.get(0).toString();

        //存储Redis orders
        if(flag.equals("1")){//新订单
            list.clear();
            list.add(cart.getUserid()+"");
            list.add(cart.getOrderid()+"");

            list.add(flag+id);//maxid
            list.add("0"+id);
            //更新maxid
            RedisMapper.executeRedisByLua(rdao.redisTemplate1, list,"addOrders.lua");
        }
        return id+"";
    }
    public String getMaxidByUserid(String userid){
        String value=rdao.getString("maxid:"+userid);
        return value.substring(1);
    }
    public void delCart(Cart cart){
        //删除Redis cart
        rdao.del("carts:"+cart.getUserid()+":"+cart.getOrderid()+":"+cart.getItemid());
        //删除mysql cart
        CartKey key=new CartKey();
        key.setUserid(cart.getUserid());
        key.setItemid(cart.getItemid());
        key.setOrderid(cart.getOrderid());
        cdao.deleteByPrimaryKey(key);


    }
    public void update(Cart cart){
        //redis 修改
        rdao.setString("carts:"+cart.getUserid()+":"+cart.getOrderid()+":"+cart.getItemid(),cart.getQuantity().toString());

        //mysql 修改
        cdao.updateByPrimaryKey(cart);
    }
    public List<Cart> showCart(String userid,String orderid){
        Set<String> set=rdao.keysQuery("carts:"+userid+":"+orderid+"*");
        List<Cart>clist=new ArrayList<Cart>();

        set.forEach(c->{
            //carts:1:19:26
            String quantity =rdao.getString(c);
            String itemid=c.split(":")[3];

            Cart cart=new Cart();
            cart.setUserid(Integer.parseInt(userid));
            cart.setOrderid(Integer.parseInt(orderid));
            cart.setItemid(Integer.parseInt(itemid));
            cart.setQuantity(Integer.parseInt(quantity));

            Set s1=rdao.keysQuery("item:"+"*"+":"+itemid);
            List list=new ArrayList(s1);
            //通过itemid 查 item
            //因为前面已经得到了cart除了出item以外的所有属性的值
            LinkedHashSet set1=(LinkedHashSet)rdao.getSet(list.get(0).toString());
            Iterator it=set1.iterator();
            while(it.hasNext()){
                Object obj=it.next();
                cart.setItem((Item)obj);
                clist.add(cart);
            }

        });
        return clist;
    }
    public void checkout(Orders order){
        //修改mysql

        String date=new Date().toString();
        System.out.println(date);
        order.setOrderdate(date);
        odao.updateByPrimaryKey(order);
        this.rabbitTemplate.convertAndSend("topic.order",order.getUserid()+":"+order.getOrderid()+":"+order.getTotalprice());
        //update redis
        rdao.setString("maxid:"+order.getUserid(),"1"+order.getOrderid());


    }







}
