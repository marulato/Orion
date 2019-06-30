package org.orion.common.scheduel.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.common.scheduel.BatchJobEntity;
import org.orion.common.scheduel.ScheduelEntity;

import java.util.List;

@Mapper
public interface JobScheduelDao {

    List<ScheduelEntity> queryAllQuartzJobs(int page, int pageSize);

    void createBatchJob(BatchJobEntity batchJobEntity);

    BatchJobEntity queryBatchJob(String name);

    List<BatchJobEntity> queryAllBatchJobs();
}
