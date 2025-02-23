package org.job.backend.jobexecutor;

import org.job.backend.jobexecutor.dao.*;
import org.job.backend.jobexecutor.service.JobExecutorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobExecutorServiceImplTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private TransactionRunner runner;

    @InjectMocks
    private JobExecutorServiceImpl jobExecutorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNewJob() {
        var jobInfo = new JobInfo();
        jobInfo.setMax(100);
        jobInfo.setMin(1);
        jobInfo.setCount(5);

        var job = new Job();
        job.setId(1L);

        when(jobRepository.save(any(Job.class))).thenReturn(job);

        Long jobId = jobExecutorService.createNewJob(jobInfo);

        assertNotNull(jobId);
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void testGetJob() {
        Job job = new Job();
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

        Job result = jobExecutorService.getJob(1L);

        assertNotNull(result);
        verify(jobRepository, times(1)).findById(1L);
    }

    @Test
    void testExecuteJob() {
        // Mock the behavior of jobRepository and runner
        Job job = new Job();
        when(jobRepository.findFirstByStatus(JobStatus.NEW)).thenReturn(Optional.of(job));
        doNothing().when(runner).doInTransaction(any());

        jobExecutorService.executeJob();

        verify(jobRepository, times(1)).findFirstByStatus(JobStatus.NEW);
        verify(runner, times(1)).doInTransaction(any());
    }

    @Test
    void testRepeatFailedJob() {
        Job job = new Job();
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        doNothing().when(runner).doInTransaction(any());

        assertDoesNotThrow(() -> jobExecutorService.repeatFailedJob(1L));

        verify(jobRepository, times(1)).findById(1L);
    }
}
