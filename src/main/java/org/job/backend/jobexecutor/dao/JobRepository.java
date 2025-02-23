package org.job.backend.jobexecutor.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {

    Optional<Job> findFirstByStatus(JobStatus status);
}
