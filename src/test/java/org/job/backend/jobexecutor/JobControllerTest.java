package org.job.backend.jobexecutor;

import org.job.backend.jobexecutor.dao.Job;
import org.job.backend.jobexecutor.dao.JobInfo;
import org.job.backend.jobexecutor.service.JobExecutorService;
import org.job.backend.jobexecutor.web.JobController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class JobControllerTest {

    @Mock
    private JobExecutorService jobExecutorService;

    @InjectMocks
    private JobController jobController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStartJob() {
        JobInfo jobInfo = new JobInfo();
        jobInfo.setMax(100);
        jobInfo.setMin(1);
        jobInfo.setCount(5);

        when(jobExecutorService.createNewJob(any(JobInfo.class))).thenReturn(1L);

        ResponseEntity<Long> response = jobController.startJob(jobInfo);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok(1L), response);
        verify(jobExecutorService, times(1)).createNewJob(any(JobInfo.class));
    }

    @Test
    void testGetJob() {
        var job = new Job();
        when(jobExecutorService.getJob(1L)).thenReturn(job);

        ResponseEntity<Job> response = jobController.getJob(1L);

        assertNotNull(response);
        assertEquals(job, response.getBody());
        verify(jobExecutorService, times(1)).getJob(1L);
    }

    @Test
    void testRepeatJob() {
        doNothing().when(jobExecutorService).repeatFailedJob(1L);

        ResponseEntity<?> response = jobController.repeatJob(1L);

        assertNotNull(response);
        assertEquals(ResponseEntity.ok().build(), response);
        verify(jobExecutorService, times(1)).repeatFailedJob(1L);
    }
}
