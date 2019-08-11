package org.orion.common.schedule.entity;

import java.util.List;

public class JobParamModel {

    private List<BatchJobParam> params;

    public List<BatchJobParam> getParams() {
        return params;
    }

    public void setParams(List<BatchJobParam> params) {
        this.params = params;
    }
}
