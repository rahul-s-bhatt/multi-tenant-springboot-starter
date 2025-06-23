package org.nirvikalpa.multitenancy.exception;

public class NoTenantFoundException extends RuntimeException {
    public NoTenantFoundException(String message){
        super(message);
    }
}
