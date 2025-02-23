package org.job.backend.jobexecutor.service;

import lombok.RequiredArgsConstructor;
import org.job.backend.jobexecutor.TransactionRunner;
import org.job.backend.jobexecutor.dao.JobInfo;
import org.job.backend.jobexecutor.dao.Job;
import org.job.backend.jobexecutor.dao.JobRepository;
import org.job.backend.jobexecutor.dao.JobStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class JobExecutorServiceImpl implements JobExecutorService {

    private final JobRepository jobRepository;
    private final TransactionRunner runner;

    @Override
    public void executeJob() {
        var job = jobRepository.findFirstByStatus(JobStatus.NEW);
        job.ifPresent(j ->
                runner.doInTransaction(() -> process(j))
        );
    }

    @Override
    public void repeatFailedJob(Long jobId) {
        var job = jobRepository.findById(jobId);
        job.filter(j -> j.getStatus() == JobStatus.FAILED)
                .ifPresent(j ->
                        runner.doInTransaction(() -> process(j)));
    }

    @Override
    public Long createNewJob(JobInfo jobInfo) {
        var newJob = Job.builder()
                .status(JobStatus.NEW)
                .jobInfo(jobInfo)
                .build();
        newJob = jobRepository.save(newJob);
        return newJob.getId();
    }

    @Override
    public Job getJob(Long jobId) {
        return jobRepository.findById(jobId).orElse(null);
    }

    private void process(Job job) {
        try {
            job.setStatus(JobStatus.IN_PROGRESS);
            jobRepository.save(job);
            var result = run(job.getJobInfo());
            job.setResult(result.toString());
            job.setStatus(JobStatus.COMPLETED);
            jobRepository.save(job);
        } catch (Exception e) {
            job.setStatus(JobStatus.FAILED);
            jobRepository.save(job);
        }

    }

    private List<Integer> run(JobInfo jobInfo) {
        AtomicInteger counter = new AtomicInteger(0);
        return Stream
                .generate(() -> {
                    counter.incrementAndGet();
                    int random = (int) (Math.random() * jobInfo.getMax() + jobInfo.getMin());
                    return random;
                })
                .takeWhile(n -> counter.get() < jobInfo.getCount()).toList();
    }
}
