package com.avc.crm.crmbackend.common.exception;

/**
 * @author amayuru_i
 * @project crm-backend
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resource;
    private final Long   id;

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " not found with id: " + id);
        this.resource = resource;
        this.id       = id;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message);
        this.resource = null;
        this.id       = null;
    }

    public String getResource() { return resource; }
    public Long   getId()       { return id; }
}
