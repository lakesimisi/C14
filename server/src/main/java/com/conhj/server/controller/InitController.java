package com.conhj.server.controller;

import com.conhj.server.service.InitServiceImpl;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.awt.SunHints;

@RestController
@CrossOrigin
@RequestMapping(value={"/init"},name="初始化操作")
@Api(value="init",description="初始化操作数据接口")
public class InitController {
    @Autowired
    private InitServiceImpl service;

    @ApiOperation(value="init",notes="初始化信息")
    @ApiResponses(value ={
            @ApiResponse(code=404,message = "初始化错误"),
            @ApiResponse(code=200,message = "初始化成功")
    })
    @GetMapping(value={"/init"})
    public ResponseEntity<Void>init(){
        try{
            service.init();
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        }
    }


}
