package org.nirvikalpa.multitenancy.applicationlistener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLifecycleTracker implements ApplicationListener<ContextRefreshedEvent> {
    private static volatile boolean applicationStarted = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        applicationStarted = true;
    }

    public static boolean isApplicationStarted() {
        return applicationStarted;
    }

    public static void markAsStarted(){
        applicationStarted = true;
    }
}

