package com.conhj.server.mapper;

import com.conhj.server.model.Item;
import com.conhj.server.model.ItemExample.Criteria;
import com.conhj.server.model.ItemExample.Criterion;
import com.conhj.server.model.ItemExample;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;

public class ItemSqlProvider {

    public String countByExample(ItemExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("item");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(ItemExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("item");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(Item record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("item");
        
        if (record.getItemid() != null) {
            sql.VALUES("itemid", "#{itemid,jdbcType=INTEGER}");
        }
        
        if (record.getItemno() != null) {
            sql.VALUES("itemno", "#{itemno,jdbcType=VARCHAR}");
        }
        
        if (record.getListprice() != null) {
            sql.VALUES("listprice", "#{listprice,jdbcType=DECIMAL}");
        }
        
        if (record.getProductid() != null) {
            sql.VALUES("productid", "#{productid,jdbcType=INTEGER}");
        }
        
        if (record.getUnitcost() != null) {
            sql.VALUES("unitcost", "#{unitcost,jdbcType=DECIMAL}");
        }
        
        if (record.getStatus() != null) {
            sql.VALUES("status", "#{status,jdbcType=VARCHAR}");
        }
        
        if (record.getAttr1() != null) {
            sql.VALUES("attr1", "#{attr1,jdbcType=VARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExample(ItemExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("itemid");
        } else {
            sql.SELECT("itemid");
        }
        sql.SELECT("itemno");
        sql.SELECT("listprice");
        sql.SELECT("productid");
        sql.SELECT("unitcost");
        sql.SELECT("status");
        sql.SELECT("attr1");
        sql.FROM("item");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        Item record = (Item) parameter.get("record");
        ItemExample example = (ItemExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("item");
        
        if (record.getItemid() != null) {
            sql.SET("itemid = #{record.itemid,jdbcType=INTEGER}");
        }
        
        if (record.getItemno() != null) {
            sql.SET("itemno = #{record.itemno,jdbcType=VARCHAR}");
        }
        
        if (record.getListprice() != null) {
            sql.SET("listprice = #{record.listprice,jdbcType=DECIMAL}");
        }
        
        if (record.getProductid() != null) {
            sql.SET("productid = #{record.productid,jdbcType=INTEGER}");
        }
        
        if (record.getUnitcost() != null) {
            sql.SET("unitcost = #{record.unitcost,jdbcType=DECIMAL}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{record.status,jdbcType=VARCHAR}");
        }
        
        if (record.getAttr1() != null) {
            sql.SET("attr1 = #{record.attr1,jdbcType=VARCHAR}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("item");
        
        sql.SET("itemid = #{record.itemid,jdbcType=INTEGER}");
        sql.SET("itemno = #{record.itemno,jdbcType=VARCHAR}");
        sql.SET("listprice = #{record.listprice,jdbcType=DECIMAL}");
        sql.SET("productid = #{record.productid,jdbcType=INTEGER}");
        sql.SET("unitcost = #{record.unitcost,jdbcType=DECIMAL}");
        sql.SET("status = #{record.status,jdbcType=VARCHAR}");
        sql.SET("attr1 = #{record.attr1,jdbcType=VARCHAR}");
        
        ItemExample example = (ItemExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(Item record) {
        SQL sql = new SQL();
        sql.UPDATE("item");
        
        if (record.getItemno() != null) {
            sql.SET("itemno = #{itemno,jdbcType=VARCHAR}");
        }
        
        if (record.getListprice() != null) {
            sql.SET("listprice = #{listprice,jdbcType=DECIMAL}");
        }
        
        if (record.getProductid() != null) {
            sql.SET("productid = #{productid,jdbcType=INTEGER}");
        }
        
        if (record.getUnitcost() != null) {
            sql.SET("unitcost = #{unitcost,jdbcType=DECIMAL}");
        }
        
        if (record.getStatus() != null) {
            sql.SET("status = #{status,jdbcType=VARCHAR}");
        }
        
        if (record.getAttr1() != null) {
            sql.SET("attr1 = #{attr1,jdbcType=VARCHAR}");
        }
        
        sql.WHERE("itemid = #{itemid,jdbcType=INTEGER}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, ItemExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}