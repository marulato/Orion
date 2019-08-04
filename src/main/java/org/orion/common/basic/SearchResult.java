package org.orion.common.basic;

import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.entity.AppConsts;

public class SearchResult {

    private String responseCode;
    private Object result;
    private int page;
    private int pageSize;
    private int totalPages;

    public SearchResult(){}

    public static SearchResult success(Object result, int totalPages) {
        SearchResult searchResult = new SearchResult();
        searchResult.setResponseCode(AppConsts.RESPONSE_SUCCESS);
        searchResult.setTotalPages(totalPages);
        searchResult.setResult(result);
        return searchResult;
    }

    public static SearchResult error() {
        SearchResult searchResult = new SearchResult();
        searchResult.setResponseCode(AppConsts.RESPONSE_ERROR);
        return searchResult;
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

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
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

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
