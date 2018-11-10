package com.conhj.server.controller;

import com.conhj.server.model.Category;
import com.conhj.server.model.Item;
import com.conhj.server.model.Product;
import com.conhj.server.service.PetServiceImpl;
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
@RequestMapping(value = {"/pet"},name="宠物操作")
@Api(value = "pet",description = "宠物操作数据接口")
public class PetController {
    @Autowired
    private PetServiceImpl service;
    @ApiResponses(value={
            @ApiResponse(code=404,message="查询成功"),
            @ApiResponse(code=200,message="查询失败")
    })
    @GetMapping(value={"/qCategory"})
    public ResponseEntity<List> qCategory(){
        List<Category>list=service.queryCategory();
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }

    @ApiResponses(value={
            @ApiResponse(code=404,message="查询产品成功"),
            @ApiResponse(code=200,message="查询产品失败")
    })
    @GetMapping(value={"/qProduct/{cid}"})
    public ResponseEntity<List> qProduct(@PathVariable String cid){
        List<Product>list=service.queryProduct(cid);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }
    @GetMapping(value = {"/qItems/{pid}"})
    public ResponseEntity<List> qItems(@PathVariable String pid){//查询product
        List<Item> list=service.querItems(pid);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }
    @GetMapping(value = {"/qItem/{iid}/{pid}"} )
    public ResponseEntity<List> qItem(@PathVariable String iid,@PathVariable String pid){//查询product
        List<Item> list=service.queryItem(pid,iid);
        return new ResponseEntity<List>(list, HttpStatus.OK);
    }


}
