package org.orion.common.schedule.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.SQLManager;
import org.orion.common.schedule.entity.*;

import java.util.List;

@Mapper
public interface JobScheduleDao {

    @SelectProvider(type = SQLManager.class, method = "createSearch")
    List<BatchJobEntity> search(SearchParam<BatchJobEntity> searchParam);

    @SelectProvider(type = SQLManager.class, method = "createSearchCount")
    int getCountsBySearchParam(SearchParam<BatchJobEntity> searchParam);

    BatchJobEntity queryBatchJobByNameAndGroup(String jobName, String jobGroup);

    BatchJobEntity queryBatchJobBySchedule(JobSchedule jobSchedule);

    BatchJobEntity queryQrtzJobByClass(String className);

    BatchJobEntity queryBatchJobByTrigger(JobSchedule jobSchedule);

    List<BatchJobEntity> queryAllBatchJobs();

    List<BatchJobFiredHistory> getJobFireHistory(BatchJobEntity jobEntity);

    QrtzJobDetails queryQrtzJobDetailsNameAndGroup(String jobName, String jobGroup);

    QrtzCronTriggers queryCronTrigger(String triggerName, String triggerGroup);

    QrtzTriggers queryTrigger(String jobName, String jobGroup);

    void insertJobHistory(BatchJobFiredHistory history);

    BatchJobFiredHistory queryRunningJobHistory(String jobName, String jobGroup);

    List<BatchJobParam> queryJobParam(String jobName, String jobGroup);

    void deleteAllJobParams(String jobName, String jobGroup);

    void insertJobParams(@Param("params") List<BatchJobParam> params);
}
