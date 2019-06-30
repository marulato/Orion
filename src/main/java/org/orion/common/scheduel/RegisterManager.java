package org.orion.common.scheduel;

import org.orion.common.miscutil.SpringUtil;
import org.orion.common.miscutil.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterManager {

    private static Map<String, Boolean> registMap;

    private static void init() {
        registMap = new HashMap<>();
        JobScheduelManager jobScheduelManager = SpringUtil.getBean(JobScheduelManager.class);
        List<BatchJobEntity> orionList = jobScheduelManager.getAllBatchJobs();
        if (orionList != null && !orionList.isEmpty()) {
            for (BatchJobEntity job : orionList) {
                registMap.put(job.getName(), job.getIsRegistered().equals("Y"));
            }
        }
    }

    public static boolean isRegistered(String jobName) {
        if (!StringUtil.isEmpty(jobName)) {
            if (registMap == null)
                init();
            if (registMap.containsKey(jobName))
                return registMap.get(jobName);
        }
        return false;
    }
}
