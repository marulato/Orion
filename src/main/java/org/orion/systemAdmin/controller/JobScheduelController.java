package org.orion.systemAdmin.controller;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.Response;
import org.orion.common.basic.SearchParam;
import org.orion.common.basic.SearchResult;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.message.DataManager;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.miscutil.Validation;
import org.orion.common.scheduel.BatchJobEntity;
import org.orion.common.scheduel.JobScheduel;
import org.orion.common.scheduel.JobScheduelManager;
import org.orion.systemAdmin.entity.AppConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class JobScheduelController {
    @Resource
    private JobScheduelManager jobScheduelManager;
    private final Logger logger = LoggerFactory.getLogger(JobScheduelController.class);

    @RequestMapping("/web/System/JobScheduel")
    public String initJobScheduelPage() {
        return "systemadmin/job_scheduel";
    }

    @RequestMapping("/web/System/JobScheduel/validate")
    @ResponseBody
    public Response validateJobScheduel(JobScheduel jobScheduel, String action) {
        List<ErrorCode> errors = Validation.doValidate(jobScheduel);
        BatchJobEntity batchJobEntity = jobScheduelManager.getBatchJobByName(jobScheduel.getJobName());
        if (AppConsts.ACTION_ADD.equals(action)) {
            if (batchJobEntity != null) {
                errors.add(DataManager.getErrorCode(""));
            } else {
                BatchJobEntity jobWithSameTrigger = jobScheduelManager.getBatchJobByTrigger(jobScheduel);
                if (jobWithSameTrigger != null) {
                    errors.add(DataManager.getErrorCode(""));
                }
            }
        } else if(AppConsts.ACTION_MODIFY.equals(action)) {
            if (batchJobEntity == null) {
                errors.add(DataManager.getErrorCode(""));
            }
        }
        Response response = new Response();
        if (!errors.isEmpty()) {
            response.setCode(AppConsts.VALIDATION_ERROR);
            response.setErrors(errors);
        } else {
            response.setCode(AppConsts.VALIDATION_PASSED);
        }
        return response;
    }

    @RequestMapping("/web/System/JobScheduel/search")
    @ResponseBody
    public SearchResult search(String page, String pageSize, BatchJobEntity jobEntity) {
        SearchParam<BatchJobEntity> searchParam = new SearchParam<>(page, pageSize, jobEntity);
        SearchResult searchResult = null;
        searchParam.orderByAsc(BatchJobEntity.COL_JOB_NAME);
        try {
            List<BatchJobEntity> batchJobEntityList = jobScheduelManager.search(searchParam);
            SearchResult searchResult1 = SearchResult.success(batchJobEntityList, jobScheduelManager.getTotalPages(searchParam));
        } catch (Exception e) {
            searchResult = SearchResult.error();
            logger.error("", e);
        }
        return searchResult;
    }

    @ResponseBody
    public Response createNewJob(JobScheduel jobScheduel, String token, HttpServletRequest request) {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode("000");
            return response;
        }
        try {
            AppContext appContext = AppContext.getAppContext(request);
            BatchJobEntity batchJobEntity = jobScheduelManager.initOrionBatchJob(jobScheduel);
            if (AppConsts.YES.equals(batchJobEntity.getIsQuartz())) {
                jobScheduelManager.registerQuartzJob(jobScheduel, appContext);
            } else {
                jobScheduelManager.registerManualJob(jobScheduel, appContext);
            }
        } catch (Exception e) {
            logger.error("", e);
        }
        return response;
    }
}
