package org.nirvikalpa.multitenancy.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextLeakTest {

    @Test
    void runningRunnableOnSameThreadLeaksContext() {
        TenantContext context = new DefaultTenantContext();
        context.setCurrentTenant("outer");

        Runnable task = () -> {
            assertEquals("outer", context.getCurrentTenant(), "Should see tenantId from outer thread");
            context.setCurrentTenant("inner");
        };

        task.run(); // ❌ runs on the current thread

        assertEquals("inner", context.getCurrentTenant(), "Tenant changed because run() used current thread");
        context.clear();
    }
}
