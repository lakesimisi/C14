package com.conhj.server.controller;

import com.conhj.server.config.MyConst;
import com.conhj.server.mapper.UserAccountDy;
import com.conhj.server.model.Account;
import com.conhj.server.service.UserServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping(name="账户操作",value={"/acc"})
@Api(value="acc",description="账户操作数据接口")
public class UserController {
    @Autowired
    private UserServiceImpl service;

    @ApiOperation(value = "loginAccount",notes = "账户登录")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "未找到资源"),
            @ApiResponse(code=200,message = "成功")
    })
    @PostMapping(value = {"/login"})
    public  ResponseEntity <Account> login(@RequestBody Account account){
        System.out.println(account.getUsername());
        Map<String,String> map=new HashMap<String,String>();
        map.put("username",account.getUsername());
        map.put("password",account.getPassword());
        Account a=service.login(map);
        if(a==null){
            return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
        }else{
            service.addCookie(a, MyConst.USERCOOKIE+":"+MyConst.SESSIONID1);

            return new ResponseEntity<Account>(a,HttpStatus.OK);
        }

    }
    @ApiOperation(value = "register",notes = "账户注册")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "注册失败"),
            @ApiResponse(code=200,message = "注册成功")
    })
    @PostMapping(value = {"/reg"})
    public  ResponseEntity <Void> register(@RequestBody Account account){
        service.register(account);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }




    @ApiOperation(value = "outAccount",notes = "账户退出")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "退出失败"),
            @ApiResponse(code=200,message = "退出成功")
    })
    @GetMapping(value = {"/out"})
    public  ResponseEntity <Void> out(){
        service.delCookie(MyConst.USERCOOKIE+":"+MyConst.SESSIONID1);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ApiOperation(value = "verifyLogin",notes = "账户是否登录校验")
    @ApiResponses(value = {
            @ApiResponse(code=404,message = "出现错误"),
            @ApiResponse(code=200,message = "已经登录")
    })
    @GetMapping(value = {"/verify"})
    public ResponseEntity<String> verify( ){
        String result=service.verifyCookie(MyConst.USERCOOKIE+":"+MyConst.SESSIONID1);

        System.out.println(result);
        if(result.indexOf(":")!=-1){
            if(result!=null){

                return new ResponseEntity<String>(result,HttpStatus.OK);

            }else{

                return new ResponseEntity<String>("gc",HttpStatus.NOT_FOUND);
            }

        }else{

            return new ResponseEntity<String>("gc",HttpStatus.NOT_FOUND);
        }

    }

}
