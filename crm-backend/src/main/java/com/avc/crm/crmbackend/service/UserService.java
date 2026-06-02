package com.avc.crm.crmbackend.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author amayuru_i
 * @project crm-backend
 */
public interface UserService {
    public UserDetails loadUserByUsername(String email);
}
