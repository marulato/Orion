package org.orion.common.basic;

import org.orion.common.miscutil.ReflectionUtil;
import org.orion.common.miscutil.StringUtil;

public  class SearchParam <T> {

    private String table;
    private SearchOrder order;
    private T object;
    private int page;
    private int pageSize;
    private int start;

    public SearchParam(){
        order = new SearchOrder();
    }

    public SearchParam(String page, String pageSize, T object) {
        setPage(page);
        setPageSize(pageSize);
        order = new SearchOrder();
        try {
            table = ReflectionUtil.getString(object, "TABLE_NAME");
        } catch (Exception e) {
           table = null;
        }
        this.object = object;
    }

    public void orderByAsc(String column) {
        order.addOrder(column, true);
    }

    public void orderByDesc(String column) {
        order.addOrder(column, false);
    }

    public void setPage(String page) {
        if (StringUtil.isNumeric(page))
            this.page = Integer.parseInt(page);
        else
            this.page = 1;
    }

    public void setPageSize(String pageSize) {
        if (StringUtil.isNumeric(pageSize))
            this.pageSize = Integer.parseInt(pageSize);
        else
            this.pageSize = 10;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public int getStart() {
        return (page - 1) * pageSize;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public SearchOrder getOrder() {
        return order;
    }

    public void setOrder(SearchOrder order) {
        this.order = order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}
