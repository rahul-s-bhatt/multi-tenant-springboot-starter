package org.nirvikalpa.demo;

import org.nirvikalpa.multitenancy.context.TenantContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TenantCheckController {

    @GetMapping("/tenant")
    public String currentTenant() {
        return "Current tenant: " + TenantContextHolder.getTenantId();
    }
}