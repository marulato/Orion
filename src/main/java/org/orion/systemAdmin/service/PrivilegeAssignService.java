package org.orion.systemAdmin.service;

import org.orion.common.mastercode.ErrorCode;
import org.orion.common.template.ModuelUrlConfiger;
import org.orion.common.template.RoleAssignConfiger;
import org.orion.common.validation.Validation;
import org.orion.systemAdmin.dao.ModuelUrlDao;
import org.orion.systemAdmin.dao.RoleModuelDao;
import org.orion.systemAdmin.entity.ModuelUrlAssign;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class PrivilegeAssignService {
    @Resource
    private RoleModuelDao roleModuelDao;
    @Resource
    private ModuelUrlDao moduelUrlDao;

    public List<RoleModuelAssign> initRoleModuelAssign(String configPath) {
        List<RoleModuelAssign> roleModuelAssignList = new ArrayList<>();
        RoleAssignConfiger roleAssignConfiger = new RoleAssignConfiger();
        roleAssignConfiger.loadConfig(configPath);
        Map<String, List<RoleModuelAssign>> roleAssignMap = roleAssignConfiger.getRoleAssignMap();
        if (roleAssignMap != null && !roleAssignMap.isEmpty()) {
            Set<String> keys = roleAssignMap.keySet();
            for (String key : keys) {
                List<RoleModuelAssign> moduels = roleAssignMap.get(key);
                for (RoleModuelAssign roleModuel : moduels) {
                    List<ErrorCode> errorCodes = Validation.doValidate(roleModuel);
                    if (errorCodes != null && !errorCodes.isEmpty()) {
                        roleModuelAssignList.clear();
                        return roleModuelAssignList;
                    } else {
                        roleModuelAssignList.add(roleModuel);
                    }
                }
            }
        }
        return roleModuelAssignList;
    }

    public List<ModuelUrlAssign> initModuelUrlAssign(String configPath) {
        List<ModuelUrlAssign> moduelUrlAssignList = new ArrayList<>();
        ModuelUrlConfiger moduelUrlConfiger = new ModuelUrlConfiger();
        moduelUrlConfiger.loadConfig(configPath);
        Map<String, List<ModuelUrlAssign>> moduelUrlMap = moduelUrlConfiger.getModuelUrlMap();
        if (moduelUrlMap != null && !moduelUrlMap.isEmpty()) {
            Set<String> keys = moduelUrlMap.keySet();
            for (String key : keys) {
                List<ModuelUrlAssign> moduelUrlAssigns = moduelUrlMap.get(key);
                for (ModuelUrlAssign moduelUrlAssign : moduelUrlAssigns) {
                    List<ErrorCode> errorCodes = Validation.doValidate(moduelUrlAssign);
                    if (errorCodes != null && !errorCodes.isEmpty()) {
                        moduelUrlAssignList.clear();
                        return moduelUrlAssignList;
                    } else {
                        moduelUrlAssignList.add(moduelUrlAssign);
                    }
                }

            }
        }
        return moduelUrlAssignList;
    }

    @Transactional
    public void saveRoleModuelAssignment(List<RoleModuelAssign> roleModuelAssignList) {
        if (roleModuelAssignList != null && !roleModuelAssignList.isEmpty()) {
            for (RoleModuelAssign rma : roleModuelAssignList) {
                List<ErrorCode> errorCodes = Validation.doValidate(rma);
                if (errorCodes != null && !errorCodes.isEmpty()) {
                    return;
                }
            }
            roleModuelDao.deleteAll();
            roleModuelDao.batchCreate(roleModuelAssignList);
        }
    }

    public void saveModuelUrlAssignment(List<ModuelUrlAssign> moduelUrlAssignList) {
        if (moduelUrlAssignList != null && !moduelUrlAssignList.isEmpty()) {
            for (ModuelUrlAssign mua : moduelUrlAssignList) {
                List<ErrorCode> errorCodes = Validation.doValidate(mua);
                if (errorCodes != null && !errorCodes.isEmpty()) {
                    return;
                }
            }
            moduelUrlDao.deleteAll();
            moduelUrlDao.batchCreate(moduelUrlAssignList);
        }
    }
}
