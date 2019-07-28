package org.orion.common.basic;

import org.orion.common.miscutil.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class SearchOrder {

    private Map<String, String> order;

    public SearchOrder() {
        order = new HashMap<>();
    }

    public void addOrder(String column, boolean asc) {
        if (asc) {
            order.put(StringUtil.convertToTableColumn(column), "ASC");
        } else {
            order.put(StringUtil.convertToTableColumn(column), "DESC");
        }
    }

    public void disorder() {
        order.clear();
    }

    public String orderBy() {
        if (!order.isEmpty()) {
            StringBuilder orderby = new StringBuilder();
            order.forEach((col, ord) -> {
                orderby.append(" ORDER BY ");
                orderby.append(col).append(" ").append(ord);
                orderby.append(", ");
            });
            orderby.deleteCharAt(orderby.lastIndexOf(","));
            return orderby.toString();
        }
        return null;
    }
}
