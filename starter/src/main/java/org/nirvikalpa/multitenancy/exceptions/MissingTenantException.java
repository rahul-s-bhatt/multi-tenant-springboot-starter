package org.nirvikalpa.multitenancy.exceptions;

public class MissingTenantException extends IllegalStateException {
    public MissingTenantException() {
        super("No tenant ID found in context. Make sure a tenant identifier resolver (e.g. a filter) is configured properly.");
    }

    public MissingTenantException(String message) {
        super(message);
    }

    public MissingTenantException(String message, Throwable cause) {
        super(message, cause);
    }
}
