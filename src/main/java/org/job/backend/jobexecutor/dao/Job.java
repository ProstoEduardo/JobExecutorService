package org.job.backend.jobexecutor.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Job")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.ENUM)
    @Column(name = "status")
    private JobStatus status;

    @JdbcTypeCode(SqlTypes.JAVA_OBJECT)
    private JobInfo jobInfo;

    @Column(name = "result")
    private String result;
}
