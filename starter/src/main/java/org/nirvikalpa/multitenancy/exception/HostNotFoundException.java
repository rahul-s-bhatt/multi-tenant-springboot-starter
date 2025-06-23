package org.nirvikalpa.multitenancy.exception;

public class HostNotFoundException extends RuntimeException {
    public HostNotFoundException(String message){
        super(message);
    }
}
