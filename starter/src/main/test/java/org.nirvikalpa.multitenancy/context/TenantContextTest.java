package org.nirvikalpa.multitenancy.context;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @Test
    void shouldStoreAndRetrieveTenantIdPerThread() {
        TenantContext context = new DefaultTenantContext();
        context.setCurrentTenant("acme");

        assertEquals("acme", context.getCurrentTenant());

        context.clear();
        assertNull(context.getCurrentTenant());
    }

    @Test
    void shouldNotLeakTenantIdAcrossThreads() throws InterruptedException {
        TenantContext context = new DefaultTenantContext();
        context.setCurrentTenant("main");

        Runnable task = () -> {
            assertNull(context.getCurrentTenant());
            context.setCurrentTenant("child");
            assertEquals("child", context.getCurrentTenant());
            context.clear();
            assertNull(context.getCurrentTenant());
        };

        Thread thread = new Thread(task);
        thread.start();
        thread.join();

        // Ensure original thread still holds its context
        assertEquals("main", context.getCurrentTenant());
        context.clear();
    }
}
