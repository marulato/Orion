package org.orion.common.scheduel;

import org.orion.common.basic.BaseEntity;

public class BatchJobEntity extends BaseEntity {

    private String name;
    private String description;
    private String className;
    private String isQuartz;
    private String isRegistered;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getIsQuartz() {
        return isQuartz;
    }

    public void setIsQuartz(String isQuartz) {
        this.isQuartz = isQuartz;
    }

    public String getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(String isRegistered) {
        this.isRegistered = isRegistered;
    }
}
