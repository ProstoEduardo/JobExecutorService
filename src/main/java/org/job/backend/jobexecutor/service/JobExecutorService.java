package org.job.backend.jobexecutor.service;


import org.job.backend.jobexecutor.dao.JobInfo;
import org.job.backend.jobexecutor.dao.Job;

public interface JobExecutorService {

     Long createNewJob(JobInfo jobInfo);

     Job getJob(Long jobId);

     void executeJob();

     void repeatFailedJob(Long jobId);
}
