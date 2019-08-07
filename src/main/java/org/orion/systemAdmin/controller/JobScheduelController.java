package org.orion.systemAdmin.controller;

import org.orion.common.audit.AuditTrail;
import org.orion.common.basic.AppContext;
import org.orion.common.basic.Response;
import org.orion.common.basic.SearchParam;
import org.orion.common.basic.SearchResult;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.message.DataManager;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.scheduel.JobScheduelManager;
import org.orion.common.scheduel.entity.BatchJobEntity;
import org.orion.common.scheduel.entity.BatchJobFiredHistory;
import org.orion.common.scheduel.entity.JobScheduel;
import org.orion.common.validation.Validation;
import org.orion.systemAdmin.entity.AppConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.List;

@Controller
public class JobScheduelController {
    @Resource
    private JobScheduelManager jobScheduelManager;
    @Resource
    private CrudManager crudManager;
    private final Logger logger = LoggerFactory.getLogger(JobScheduelController.class);

    @RequestMapping("/web/System/JobScheduel")
    public String initJobScheduelPage() {
        return "systemadmin/job_scheduel";
    }

    @RequestMapping("/web/System/JobScheduel/validate")
    @ResponseBody
    public Response validateJobScheduel(JobScheduel jobScheduel, String action) {
        List<ErrorCode> errors = Validation.doValidate(jobScheduel);
        BatchJobEntity batchJobEntity = jobScheduelManager.getBatchJobByNameAndGroup(jobScheduel.getJobName(),
                jobScheduel.getJobGroup());
        if (AppConsts.ACTION_ADD.equals(action)) {
            if (batchJobEntity != null) {
                errors.add(DataManager.getErrorCode("005"));
            } else {
                BatchJobEntity jobWithSameTrigger = jobScheduelManager.getBatchJobByTrigger(jobScheduel);
                if (jobWithSameTrigger != null) {
                    errors.add(DataManager.getErrorCode("008"));
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
            searchResult = SearchResult.success(batchJobEntityList, jobScheduelManager.getTotalPages(searchParam));
        } catch (Exception e) {
            searchResult = SearchResult.error();
            logger.error("", e);
        }
        return searchResult;
    }

    @RequestMapping("/web/System/JobScheduel/create")
    @ResponseBody
    public Response createNewJob(JobScheduel jobScheduel, String token, HttpServletRequest request) {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode(AppConsts.INVALID_REQUEST);
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
            AuditTrail<BatchJobEntity> afterInsert = new AuditTrail<>(batchJobEntity, AppConsts.AUDIT_AFTER_INSERT);
            crudManager.createAudit(afterInsert);
            response.setCode(AppConsts.RESPONSE_SUCCESS);
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
        }
        return response;
    }

    @RequestMapping("/web/System/JobScheduel/display/{param}")
    @ResponseBody
    public Response displayJob(@PathVariable("param") String param) {
        Response response = new Response();
        try {
            String[] requestParam = Encrtption.decryptAES(URLDecoder.decode(param), AppConsts.REQUEST_KEY, true).split(",");
            String jobName = requestParam[0];
            String jobGroup = requestParam[1];
            BatchJobEntity batchJobEntity = jobScheduelManager.getBatchJobByNameAndGroup(jobName, jobGroup);
            if (batchJobEntity != null) {
                BatchJobFiredHistory latestJobFired = jobScheduelManager.getLatestFired(batchJobEntity);
                JobScheduel jobScheduel = jobScheduelManager.getJobScheduelFromBatchJob(batchJobEntity);
                jobScheduel.setFiredHistory(latestJobFired);
                response.setObject(jobScheduel);
                response.setCode(AppConsts.RESPONSE_SUCCESS);
            }
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
        }
        return response;
    }
}
