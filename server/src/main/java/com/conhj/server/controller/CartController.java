package com.conhj.server.controller;

import com.conhj.server.model.Cart;
import com.conhj.server.model.Orders;
import com.conhj.server.service.CartServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value={"/cart"},name="购物车操作")
@Api(value="cart",description="购物车操作数据接口")
public class CartController {

    @Autowired
    private CartServiceImpl service;

    @ApiResponses(value = {
            @ApiResponse(code=404,message = "增加购物车错误"),
            @ApiResponse(code=200,message = "增加购物车成功")
    })
    @PostMapping(value = {"/add"})
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        service.addCart(cart);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code=404,message = "show错误"),
            @ApiResponse(code=200,message = "show成功")
    })
    @GetMapping(value = {"/show/{userid}"})
    public ResponseEntity<List> showCart(@PathVariable String userid){

        String orderid=service.getMaxidByUserid(userid);//通过redis 和userid 取maxid

        List <Cart>list=service.showCart(userid,orderid);


        return new ResponseEntity<List>(list, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code=404,message = "修改错误"),
            @ApiResponse(code=200,message = "修改成功")
    })
    @PostMapping(value = {"/update"})
    public ResponseEntity<Void> updateCart(@RequestBody Cart cart){
        service.update(cart);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


    @ApiResponses(value = {
            @ApiResponse(code=404,message = "删除错误"),
            @ApiResponse(code=200,message = "删除成功")
    })
    @PostMapping(value = {"/del"})
    public ResponseEntity<Void> delCart(@RequestBody Cart cart){

        service.delCart(cart);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "结算错误"),
            @ApiResponse(code=200,message = "结算成功")
    })
    @PostMapping(value = {"/checkout"})
    public ResponseEntity<Void> checkout(@RequestBody Orders order){
        service.checkout(order);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
