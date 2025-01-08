package com.iot.common.mysql.model.pojo.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @创建人 zsy
 * @创建时间 2022-11-07
 * @描述 没有描述
 */
@Setter
public class PageQuery<T> extends Page<T> {


    private List<FilterItem> filters;

    @JsonIgnore
    private Map<String, Object> condition;

    @JsonIgnore
    private transient Map<String, Object> otherCondition;


    public PageQuery( PageQueryDTO pageQueryDTO) {
        super(pageQueryDTO.getPage(), pageQueryDTO.getLimit());

        this.filters = pageQueryDTO.getFilters();
        this.condition = pageQueryDTO.getCondition();
        if (this.condition==null) this.condition = new LinkedHashMap<>();

        if (pageQueryDTO.getOrders() != null) {
            for (OrderItem item : pageQueryDTO.getOrders()) {
                String field = item.getColumn();
                if (StringUtils.isNotBlank(field)) {
                    field = StringUtils.camelToUnderline(field);
                    boolean isAsc = item.isAsc();
                    field = this.replaceSqlInject(field);
                    this.addOrder(isAsc ? OrderItem.asc(field) : OrderItem.desc(field));
                }
            }
        }

    }

    private String replaceSqlInject(String str) {
        return str.replace("<", "＜").replace(">", "＞").replace(";", "；").replace("--", "——").replace("'", "\"\"");
    }

    public List<FilterItem> filters() {
        return filters == null ? new ArrayList<>() : filters;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public Map<String, Object> getOtherCondition() {
        if(otherCondition==null){
            otherCondition = new LinkedHashMap<>();
        }
        return otherCondition;
    }

    public void setOtherCondition(Map<String, Object> otherCondition) {
        this.otherCondition = otherCondition;
    }
}
