package org.orion.common.scheduel.dao;

import org.apache.ibatis.annotations.Mapper;
import org.orion.common.scheduel.OrionBatchJobEntity;
import org.orion.common.scheduel.ScheduelEntity;

import java.util.List;

@Mapper
public interface JobScheduelDao {

    List<ScheduelEntity> queryAllJobs(int page, int pageSize);

    void createOrionBatchJob(OrionBatchJobEntity orionBatchJobEntity);

    OrionBatchJobEntity queryOrionJob(String name);

    List<OrionBatchJobEntity> queryAllOrionJobs();
}
