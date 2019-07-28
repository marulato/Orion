package org.orion.systemAdmin.service;

import org.orion.common.miscutil.StringUtil;
import org.orion.systemAdmin.dao.SysConfigDao;
import org.orion.systemAdmin.entity.SystemConfig;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;

    public List<SystemConfig> searchAllSystemConfigs(int page, int pageSize) {
        return sysConfigDao.search((page - 1) * pageSize, pageSize);
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
        return sysConfigDao.getCounts();
    }

}
