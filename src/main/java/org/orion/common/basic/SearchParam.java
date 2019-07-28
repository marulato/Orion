package org.orion.common.basic;

public class SearchParam {

    private String table;
    private SearchOrder order;
    private int page;
    private int pageSize;
    private int start;

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
