package org.nirvikalpa.multitenancy.context;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TenantContextHolderTest {

    @Test
    void shouldStoreAndRetrieveTenantIdPerThread() {
        TenantContextHolder.setTenantId("acme");
        assertEquals("acme", TenantContextHolder.getTenantId());
        TenantContextHolder.clear();
        assertNull(TenantContextHolder.getTenantId());
    }

    @Test
    void shouldNotLeakTenantIdAcrossThreads() throws InterruptedException {
        TenantContextHolder.setTenantId("main");

        final String[] threadTenant = new String[1];
        Thread t = new Thread(() -> threadTenant[0] = TenantContextHolder.getTenantId());
        t.start(); t.join();

        assertNull(threadTenant[0]);
        TenantContextHolder.clear();
    }
}
