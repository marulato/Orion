package org.orion.systemAdmin.service;

import org.orion.common.basic.SearchParam;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.dao.SysConfigDao;
import org.orion.systemAdmin.entity.SystemConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;
    @Resource
    private CrudManager crudManager;

    public List<SystemConfig> searchAllSystemConfigs(SearchParam searchParam) {
        if (searchParam != null) {
            return sysConfigDao.search(searchParam);
        }
        return new ArrayList<>();
    }

    public List<SystemConfig> getAllSystemConfigs() {
        return sysConfigDao.queryAll();
    }

    public SystemConfig getSystemConfig(String key) {
        if (!StringUtil.isEmpty(key)) {
            return sysConfigDao.query(key);
        }
        return null;
    }

    public int getSystemConfigTotalPages(int pageSize) {
        int rows = getSystemConfigTotalCounts();
        if (rows % pageSize == 0) {
            return rows / pageSize;
        } else {
            return rows / pageSize + 1;
        }
    }

    public int getSystemConfigTotalCounts() {
        return crudManager.countAll("SYS_CONFIG");
    }

}
