package org.nirvikalpa.multitenancy.context;


import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TenantContextConcurrencyTest {

    @Test
    void tenantContextHolder_shouldBeThreadSafeAcrossManyThreads() throws InterruptedException {
        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(threads);
        AtomicBoolean allIsolated = new AtomicBoolean(true);

        TenantContextHolder.setContext(new DefaultTenantContext());

        for (int i = 0; i < threads; i++) {
            final String tenantId = "tenant-" + i;
            executor.submit(() -> {
                try {
                    TenantContextHolder.setTenant(tenantId);
                    if (!tenantId.equals(TenantContextHolder.getTenant())) {
                        allIsolated.set(false);
                    }
                } finally {
                    TenantContextHolder.clear();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertTrue(allIsolated.get(), "TenantContextHolder failed under concurrency");
    }
}
