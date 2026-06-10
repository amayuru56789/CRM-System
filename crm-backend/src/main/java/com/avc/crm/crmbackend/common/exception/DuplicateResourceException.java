package com.avc.crm.crmbackend.common.exception;

/**
 * @author amayuru_i
 * @project crm-backend
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
