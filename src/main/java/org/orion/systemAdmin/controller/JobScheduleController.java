package org.orion.systemAdmin.controller;

import org.orion.common.basic.AppContext;
import org.orion.common.basic.Response;
import org.orion.common.basic.SearchParam;
import org.orion.common.basic.SearchResult;
import org.orion.common.dao.crud.CrudManager;
import org.orion.common.mastercode.ErrorCode;
import org.orion.common.message.DataManager;
import org.orion.common.miscutil.Encrtption;
import org.orion.common.miscutil.HttpUtil;
import org.orion.common.schedule.JobScheduleManager;
import org.orion.common.schedule.entity.BatchJobEntity;
import org.orion.common.schedule.entity.BatchJobFiredHistory;
import org.orion.common.schedule.entity.JobSchedule;
import org.orion.common.schedule.entity.QrtzJobDetails;
import org.orion.common.validation.Validation;
import org.orion.systemAdmin.entity.AppConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class JobScheduleController {
    @Resource
    private JobScheduleManager jobScheduleManager;
    @Resource
    private CrudManager crudManager;
    private final Logger logger = LoggerFactory.getLogger(JobScheduleController.class);

    @RequestMapping("/web/System/JobSchedule")
    public String initJobSchedulePage() {
        return "systemadmin/job_schedule";
    }

    @RequestMapping("/web/System/JobSchedule/validate")
    @ResponseBody
    public Response validateJobSchedule(JobSchedule jobSchedule, String action) {
        List<ErrorCode> errors = Validation.doValidate(jobSchedule);
        if (AppConsts.ACTION_ADD.equals(action)) {
            BatchJobEntity batchJobEntity = jobScheduleManager.getBatchJobBySchedule(jobSchedule);
            if (batchJobEntity != null) {
                errors.add(DataManager.getErrorCode("005"));
            } else {
                if (AppConsts.YES.equals(jobSchedule.getAutomatic())) {
                    BatchJobEntity qrtzJob = jobScheduleManager.getQrtzJobByClass(jobSchedule.getClassName());
                    if (qrtzJob != null) {
                        errors.add(DataManager.getErrorCode("00"));
                    }
                }
            }
        } else if(AppConsts.ACTION_MODIFY.equals(action) || AppConsts.ACTION_DELETE.equals(action)) {
            BatchJobEntity self = jobScheduleManager.getBatchJobBySchedule(jobSchedule);
            if (self == null || !self.getJobName().equals(jobSchedule.getJobName()) ||
                    !self.getJobGroup().equals(jobSchedule.getJobGroup())) {
                errors.add(DataManager.getErrorCode("000"));
            } else {
                if (AppConsts.YES.equals(jobSchedule.getAutomatic())) {
                    BatchJobEntity qrtzJob = jobScheduleManager.getQrtzJobByClass(jobSchedule.getClassName());
                    if (qrtzJob != null && (!qrtzJob.getJobName().equals(jobSchedule.getJobName()) ||
                            !qrtzJob.getJobGroup().equals(jobSchedule.getJobGroup()))) {
                        errors.add(DataManager.getErrorCode("00"));
                    }
                }
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

    @RequestMapping("/web/System/JobSchedule/search")
    @ResponseBody
    public SearchResult search(String page, String pageSize, BatchJobEntity jobEntity) {
        SearchParam<BatchJobEntity> searchParam = new SearchParam<>(page, pageSize, jobEntity);
        SearchResult searchResult = null;
        searchParam.orderByAsc(BatchJobEntity.COL_JOB_NAME);
        try {
            List<BatchJobEntity> batchJobEntityList = jobScheduleManager.search(searchParam);
            searchResult = SearchResult.success(batchJobEntityList, jobScheduleManager.getTotalPages(searchParam));
        } catch (Exception e) {
            searchResult = SearchResult.error();
            logger.error("", e);
        }
        return searchResult;
    }

    @RequestMapping("/web/System/JobSchedule/create")
    @ResponseBody
    @Transactional
    public Response createNewJob(JobSchedule jobSchedule, String token, HttpServletRequest request) throws Exception {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode(AppConsts.INVALID_REQUEST);
            return response;
        }
        try {
            AppContext appContext = AppContext.getAppContext(request);
            BatchJobEntity batchJobEntity = jobScheduleManager.initOrionBatchJob(jobSchedule);
            if (AppConsts.YES.equals(batchJobEntity.getIsQuartz())) {
                jobScheduleManager.registerQuartzJob(jobSchedule, appContext);
            } else {
                jobScheduleManager.registerManualJob(jobSchedule, appContext);
            }
            response.setCode(AppConsts.RESPONSE_SUCCESS);
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
            throw e;
        }
        return response;
    }

    @RequestMapping("/web/System/JobSchedule/display/{param}")
    @ResponseBody
    public Response displayJob(@PathVariable("param") String param) {
        Response response = new Response();
        try {
            String[] requestParam = HttpUtil.decryptParams(param);
            String jobName = requestParam[0];
            String jobGroup = requestParam[1];
            BatchJobEntity batchJobEntity = jobScheduleManager.getBatchJobByNameAndGroup(jobName, jobGroup);
            if (batchJobEntity != null) {
                BatchJobFiredHistory latestJobFired = jobScheduleManager.getLatestFired(batchJobEntity);
                JobSchedule jobSchedule = jobScheduleManager.getJobScheduleFromBatchJob(batchJobEntity);
                jobSchedule.setFiredHistory(latestJobFired);
                response.setObject(jobSchedule);
                response.setCode(AppConsts.RESPONSE_SUCCESS);
            }
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
        }
        return response;
    }

    @RequestMapping("/web/System/JobSchedule/modify/{param}")
    @ResponseBody
    public Response initModify(@PathVariable("param") String param, HttpServletRequest request) {
        HttpUtil.setSessionAttr(request, "modify_param", param);
        return displayJob(param);
    }

    @RequestMapping("/web/System/JobSchedule/delete/{param}")
    @ResponseBody
    public Response initDelete(@PathVariable("param") String param, HttpServletRequest request) {
        HttpUtil.setSessionAttr(request, "delete_param", param);
        return displayJob(param);
    }

    @RequestMapping("/web/System/JobSchedule/doModify")
    @ResponseBody
    @Transactional
    public Response modifyJob(JobSchedule jobSchedule, String token, HttpServletRequest request) throws Exception {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode(AppConsts.INVALID_REQUEST);
            return response;
        }
        try {
            String param = (String) HttpUtil.getSessionAttr(request, "modify_param");
            String[] requestParam = HttpUtil.decryptParams(param);
            String jobName = requestParam[0];
            String jobGroup = requestParam[1];
            HttpUtil.clearSession(request, "modify_param");
            AppContext appContext = AppContext.getAppContext(request);
            BatchJobEntity batchJobEntity = jobScheduleManager.getBatchJobByNameAndGroup(jobName, jobGroup);
            BatchJobEntity newJob = jobScheduleManager.initOrionBatchJob(jobSchedule, batchJobEntity);
            if (AppConsts.YES.equals(batchJobEntity.getIsQuartz()) && AppConsts.NO.equals(jobSchedule.getAutomatic())) {
                QrtzJobDetails qrtzJobDetails = jobScheduleManager.getQrtzJobDetails(jobName, jobGroup);
                jobScheduleManager.unsheduelQuartzJob(jobScheduleManager.getQrtzTrigger(jobName, jobGroup));

            } else if (AppConsts.NO.equals(batchJobEntity.getIsQuartz()) &&
                    AppConsts.YES.equals(jobSchedule.getAutomatic())) {
                jobScheduleManager.registerQuartzJob(jobSchedule, appContext);

            } else if (AppConsts.YES.equals(batchJobEntity.getIsQuartz()) &&
                    AppConsts.YES.equals(jobSchedule.getAutomatic())) {
                jobScheduleManager.modifyQuartzJob(jobSchedule);
            }
            jobScheduleManager.updateMaunalJob(newJob, appContext);
            response.setCode(AppConsts.RESPONSE_SUCCESS);
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
            throw e;
        }
        return response;
    }

    @RequestMapping("/web/System/JobSchedule/doDelete")
    @ResponseBody
    public Response deleteJob(String token, HttpServletRequest request) {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode(AppConsts.INVALID_REQUEST);
            return response;
        }
        try {
            String param = (String) HttpUtil.getSessionAttr(request, "delete_param");
            String[] requestParam = HttpUtil.decryptParams(param);
            String jobName = requestParam[0];
            String jobGroup = requestParam[1];
            HttpUtil.clearSession(request, "delete_param");
            BatchJobEntity deleteJob = jobScheduleManager.getBatchJobByNameAndGroup(jobName, jobGroup);
            AppContext context = AppContext.getAppContext(request);
            if (deleteJob != null) {
                if (AppConsts.YES.equals(deleteJob.getIsQuartz())) {
                    jobScheduleManager.deleteQuartzJob(jobName, jobGroup, context);
                } else {
                    jobScheduleManager.deleteManualJob(jobName, jobGroup, context);
                }
                response.setCode(AppConsts.RESPONSE_SUCCESS);
            } else {
                response.setCode(AppConsts.RESPONSE_ERROR);
            }
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
        }
        return response;
    }

    @RequestMapping("/web/System/JobSchedule/launch/{param}")
    @ResponseBody
    @Transactional
    public Response launchJob(@PathVariable("param") String param, String token, HttpServletRequest request) {
        Response response = new Response();
        if (!Encrtption.validateToken(token)) {
            response.setCode(AppConsts.INVALID_REQUEST);
            return response;
        }
        try {
            String[] requestParam = HttpUtil.decryptParams(param);
            String jobName = requestParam[0];
            String jobGroup = requestParam[1];
            AppContext context = AppContext.getAppContext(request);
            BatchJobEntity job = jobScheduleManager.getBatchJobByNameAndGroup(jobName, jobGroup);
            if (job != null) {
                BatchJobFiredHistory jobFiredHistory = jobScheduleManager.getRunningJobHistory(jobName, jobGroup);
                if (jobFiredHistory != null) {
                    response.setCode("201");
                } else {
                    jobScheduleManager.executeJob(job, context);
                    response.setCode(AppConsts.RESPONSE_SUCCESS);
                }
            }
        } catch (Exception e) {
            response.setCode(AppConsts.RESPONSE_ERROR);
            logger.error("", e);
        }
        return response;
    }

}
