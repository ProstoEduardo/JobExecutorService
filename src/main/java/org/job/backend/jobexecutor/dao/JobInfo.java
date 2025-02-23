package org.job.backend.jobexecutor.dao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JobInfo {

    @NotNull
    private Integer max;

    @NotNull
    private Integer min;

    @NotNull
    private Integer count;
}
