package com.nix.cinema.common;

import com.nix.cinema.model.base.BaseModel;
import com.nix.cinema.service.BaseService;

import java.util.List;

/**
 * @author Kiss
 * @date 2018/05/02 1:02
 * 分页插件
 */
public class Pageable<M extends BaseModel<M>> {
    private Integer page;
    private Integer limit;
    private String order;
    private String sort;
    private String conditionsSql;
    private BaseService<M> baseService;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getConditionsSql() {
        return conditionsSql;
    }

    public void setConditionsSql(String conditionsSql) {
        this.conditionsSql = conditionsSql;
    }
    public Integer getCount() {
        return baseService.list(null,null,null,null,conditionsSql).size();
    }

    public List<M> getList(BaseService<M> service) {
        baseService = service;
        return service.list(page,limit,order,sort,conditionsSql);
    }
}
