package com.conhj.server.mapper;

import com.conhj.server.model.Cart;
import com.conhj.server.model.CartExample;
import com.conhj.server.model.CartKey;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface CartMapper {
    @SelectProvider(type=CartSqlProvider.class, method="countByExample")
    long countByExample(CartExample example);

    @DeleteProvider(type=CartSqlProvider.class, method="deleteByExample")
    int deleteByExample(CartExample example);

    @Delete({
        "delete from cart",
        "where userid = #{userid,jdbcType=INTEGER}",
          "and itemid = #{itemid,jdbcType=INTEGER}",
          "and orderid = #{orderid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(CartKey key);

    @Insert({
        "insert into cart (userid, itemid, ",
        "orderid, quantity)",
        "values (#{userid,jdbcType=INTEGER}, #{itemid,jdbcType=INTEGER}, ",
        "#{orderid,jdbcType=INTEGER}, #{quantity,jdbcType=INTEGER})"
    })
    int insert(Cart record);

    @InsertProvider(type=CartSqlProvider.class, method="insertSelective")
    int insertSelective(Cart record);

    @SelectProvider(type=CartSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="itemid", property="itemid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="quantity", property="quantity", jdbcType=JdbcType.INTEGER),
            @Result(column="itemid", property="item", one=@One(select="com.conhj.server.mapper.ItemMapper.selectByPrimaryKey"))
    })
    List<Cart> selectByExample(CartExample example);

    @Select({
        "select",
        "userid, itemid, orderid, quantity",
        "from cart",
        "where userid = #{userid,jdbcType=INTEGER}",
          "and itemid = #{itemid,jdbcType=INTEGER}",
          "and orderid = #{orderid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="userid", property="userid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="itemid", property="itemid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="orderid", property="orderid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="quantity", property="quantity", jdbcType=JdbcType.INTEGER)
    })
    Cart selectByPrimaryKey(CartKey key);

    @UpdateProvider(type=CartSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Cart record, @Param("example") CartExample example);

    @UpdateProvider(type=CartSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Cart record, @Param("example") CartExample example);

    @UpdateProvider(type=CartSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Cart record);

    @Update({
        "update cart",
        "set quantity = #{quantity,jdbcType=INTEGER}",
        "where userid = #{userid,jdbcType=INTEGER}",
          "and itemid = #{itemid,jdbcType=INTEGER}",
          "and orderid = #{orderid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Cart record);
}