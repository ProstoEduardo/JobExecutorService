package org.job.backend.jobexecutor.web;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.job.backend.jobexecutor.dao.Job;
import org.job.backend.jobexecutor.dao.JobInfo;
import org.job.backend.jobexecutor.service.JobExecutorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/jobs")
public class JobController {

    private final JobExecutorService jobExecutorService;

    @PostMapping
    public ResponseEntity<Long> startJob(@Valid @RequestBody JobInfo jobInfo) {
        return ResponseEntity.ok(jobExecutorService.createNewJob(jobInfo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable Long id) {
        var job = jobExecutorService.getJob(id);
        if (job == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(job);
    }

    @PostMapping("/repeat/{id}")
    public ResponseEntity<?> repeatJob(@PathVariable Long id) {
        jobExecutorService.repeatFailedJob(id);
        return ResponseEntity.ok().build();
    }
}
