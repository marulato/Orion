package org.orion.systemAdmin.service;

import org.orion.common.basic.JSONMessage;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.miscutil.Validation;
import org.orion.common.template.RoleAssignConfiger;
import org.orion.systemAdmin.dao.RoleModuelDao;
import org.orion.systemAdmin.entity.RoleModuelAssign;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleAssignService {
    @Resource
    private RoleModuelDao roleModuelDao;

    public List<RoleModuelAssign> initRoleModuelAssign(String configPath) {
        List<RoleModuelAssign> roleModuelAssignList = new ArrayList<>();
        RoleAssignConfiger roleAssignConfiger = new RoleAssignConfiger();
        roleAssignConfiger.loadConfig(configPath);
        Map<String, List<String>> roleAssignMap = roleAssignConfiger.getRoleAssignMap();
        if (roleAssignMap != null && !roleAssignMap.isEmpty()) {
            Set<String> keys = roleAssignMap.keySet();
            for (String key : keys) {
                List<String> moduels = roleAssignMap.get(key);
                for (String moduel : moduels) {
                    RoleModuelAssign roleModuelAssign = new RoleModuelAssign();
                    roleModuelAssign.setRoleId(key);
                    roleModuelAssign.setModuelId(moduel);
                    roleModuelAssignList.add(roleModuelAssign);
                }
            }
        }
        return roleModuelAssignList;
    }

    @Transactional
    public void confirm(List<RoleModuelAssign> roleModuelAssignList) {
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
}
