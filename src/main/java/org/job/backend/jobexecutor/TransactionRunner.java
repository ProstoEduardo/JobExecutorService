package org.job.backend.jobexecutor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionRunner {

    @Transactional
    public void doInTransaction(Runnable action) {
        action.run();
    }
}
