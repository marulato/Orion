package org.orion.common.scheduel.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;
import org.orion.common.basic.SearchParam;
import org.orion.common.dao.SQLManager;
import org.orion.common.scheduel.entity.BatchJobEntity;
import org.orion.common.scheduel.entity.BatchJobFiredHistory;
import org.orion.common.scheduel.entity.JobScheduel;

import java.util.List;

@Mapper
public interface JobScheduelDao {

    @SelectProvider(type = SQLManager.class, method = "createSearch")
    List<BatchJobEntity> search(SearchParam<BatchJobEntity> searchParam);

    @SelectProvider(type = SQLManager.class, method = "createSearchCount")
    int getCountsBySearchParam(SearchParam<BatchJobEntity> searchParam);

    BatchJobEntity queryBatchJob(String jobName, String jobGroup);

    BatchJobEntity queryBatchJobByTrigger(JobScheduel jobScheduel);

    List<BatchJobEntity> queryAllBatchJobs();

    List<BatchJobFiredHistory> getJobFireHistory(BatchJobEntity jobEntity);
}
