package org.job.backend.jobexecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.job.backend.jobexecutor.service.JobExecutorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
class JobScheduler {

    private final JobExecutorService jobExecutorService;

    /**
     * Шедлоком можно ограничить действия, или использовать блокировки на уровне БД
     */
    @Scheduled(fixedDelay = 1)
//    @SchedulerLock(name = "JobExecutor", lockAtLeastFor = "PT1M", lockAtMostFor = "PT9M")
    void executeCompassToAbsReport() {
        log.info("Starting JobExecutor scheduler");
        jobExecutorService.executeJob();
        log.info("JobExecutor finished");
    }
}
