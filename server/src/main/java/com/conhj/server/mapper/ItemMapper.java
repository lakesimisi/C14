package com.conhj.server.mapper;

import com.conhj.server.model.Item;
import com.conhj.server.model.ItemExample;
import java.util.List;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ItemMapper {
    @SelectProvider(type=ItemSqlProvider.class, method="countByExample")
    long countByExample(ItemExample example);

    @DeleteProvider(type=ItemSqlProvider.class, method="deleteByExample")
    int deleteByExample(ItemExample example);

    @Delete({
        "delete from item",
        "where itemid = #{itemid,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer itemid);

    @Insert({
        "insert into item (itemid, itemno, ",
        "listprice, productid, ",
        "unitcost, status, ",
        "attr1)",
        "values (#{itemid,jdbcType=INTEGER}, #{itemno,jdbcType=VARCHAR}, ",
        "#{listprice,jdbcType=DECIMAL}, #{productid,jdbcType=INTEGER}, ",
        "#{unitcost,jdbcType=DECIMAL}, #{status,jdbcType=VARCHAR}, ",
        "#{attr1,jdbcType=VARCHAR})"
    })
    int insert(Item record);

    @InsertProvider(type=ItemSqlProvider.class, method="insertSelective")
    int insertSelective(Item record);

    @SelectProvider(type=ItemSqlProvider.class, method="selectByExample")
    @Results({
        @Result(column="itemid", property="itemid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="itemno", property="itemno", jdbcType=JdbcType.VARCHAR),
        @Result(column="listprice", property="listprice", jdbcType=JdbcType.DECIMAL),
        @Result(column="productid", property="productid", jdbcType=JdbcType.INTEGER),
        @Result(column="unitcost", property="unitcost", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="attr1", property="attr1", jdbcType=JdbcType.VARCHAR),
            @Result(column="productid",property="product",one=@One(select="com.conhj.server.mapper.ProductMapper.selectByPrimaryKey"))
    })
    List<Item> selectByExample(ItemExample example);

    @Select({
        "select",
        "itemid, itemno, listprice, productid, unitcost, status, attr1",
        "from item",
        "where itemid = #{itemid,jdbcType=INTEGER}"
    })
    @Results({
        @Result(column="itemid", property="itemid", jdbcType=JdbcType.INTEGER, id=true),
        @Result(column="itemno", property="itemno", jdbcType=JdbcType.VARCHAR),
        @Result(column="listprice", property="listprice", jdbcType=JdbcType.DECIMAL),
        @Result(column="productid", property="productid", jdbcType=JdbcType.INTEGER),
        @Result(column="unitcost", property="unitcost", jdbcType=JdbcType.DECIMAL),
        @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
        @Result(column="attr1", property="attr1", jdbcType=JdbcType.VARCHAR),
            @Result(column="productid",property="product",one=@One(select="com.conhj.server.mapper.ProductMapper.selectByPrimaryKey"))
    })
    Item selectByPrimaryKey(Integer itemid);

    @UpdateProvider(type=ItemSqlProvider.class, method="updateByExampleSelective")
    int updateByExampleSelective(@Param("record") Item record, @Param("example") ItemExample example);

    @UpdateProvider(type=ItemSqlProvider.class, method="updateByExample")
    int updateByExample(@Param("record") Item record, @Param("example") ItemExample example);

    @UpdateProvider(type=ItemSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(Item record);

    @Update({
        "update item",
        "set itemno = #{itemno,jdbcType=VARCHAR},",
          "listprice = #{listprice,jdbcType=DECIMAL},",
          "productid = #{productid,jdbcType=INTEGER},",
          "unitcost = #{unitcost,jdbcType=DECIMAL},",
          "status = #{status,jdbcType=VARCHAR},",
          "attr1 = #{attr1,jdbcType=VARCHAR}",
        "where itemid = #{itemid,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(Item record);
}