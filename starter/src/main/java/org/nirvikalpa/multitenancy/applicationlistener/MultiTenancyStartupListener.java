package org.nirvikalpa.multitenancy.applicationlistener;

import org.nirvikalpa.multitenancy.pojo.MultiTenancyDiagnosticsReporter;
import org.nirvikalpa.multitenancy.properties.MultiTenancyProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

@Component
public class MultiTenancyStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter;
    public MultiTenancyStartupListener(MultiTenancyDiagnosticsReporter multiTenancyDiagnosticsReporter) {
        this.multiTenancyDiagnosticsReporter = multiTenancyDiagnosticsReporter;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        multiTenancyDiagnosticsReporter.collect();
        multiTenancyDiagnosticsReporter.printToConsole();
    }
}
