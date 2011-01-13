/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.shiro;

import org.mule.api.MuleEvent;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.security.Authentication;
import org.mule.api.security.CryptoFailureException;
import org.mule.api.security.EncryptionStrategyNotFoundException;
import org.mule.api.security.NotPermittedException;
import org.mule.api.security.SecurityException;
import org.mule.api.security.SecurityProviderNotFoundException;
import org.mule.api.security.UnknownAuthenticationTypeException;
import org.mule.security.AbstractSecurityFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.subject.support.DelegatingSubject;

/**
 * Ensures that the Authenticated user is authorized against the specified
 * set of permissions.
 */
public class AuthorizationFilter extends AbstractSecurityFilter
{
    private Collection<String> permissions;
    private Collection<String> roles;

    @Override
    public void doFilter(MuleEvent event)
        throws SecurityException, UnknownAuthenticationTypeException, CryptoFailureException,
        SecurityProviderNotFoundException, EncryptionStrategyNotFoundException, InitialisationException
    {
        Authentication auth = event.getSession().getSecurityContext().getAuthentication();
        
        if (!(auth instanceof ShiroAuthenticationResult))
        {
            throw new UnknownAuthenticationTypeException(auth);
        }
        
        ShiroAuthenticationResult shiroAuth = (ShiroAuthenticationResult) auth;
        
        try 
        {
            Collection<Permission> permissions = getPermissions(event);
            if (permissions != null)
            {
                shiroAuth.getSubject().checkPermissions(permissions);
            }
            
            Collection<String> roles = getRoles(event);
            if (roles != null)
            {
                // work around SHIRO-234 and SHIRO-235
                DelegatingSubject subj = ((DelegatingSubject)shiroAuth.getSubject());
                subj.getSecurityManager().checkRoles(subj.getPrincipals(), roles.toArray(new String[0]));
            }
        }
        catch (UnauthorizedException e)
        {
            throw new NotPermittedException(event, event.getSession().getSecurityContext(), this);
        }
    }

    /**
     * The required set of permissions for the event. By default this is a
     * static Collection, but you can extend this class to make it a dynamic 
     * Collection.
     * @param event
     * @return
     */
    protected Collection<Permission> getPermissions(MuleEvent event)
    {
        List<Permission> p2 = new ArrayList<Permission>();
        if (permissions != null)
        {
            for (String name : permissions)
            {
                p2.add(new WildcardPermission(name));
            }
        }
        return p2;
    }

    /**
     * The required set of permissions for the event. By default this is a
     * static Collection, but you can extend this class to make it a dynamic 
     * Collection.
     * @param event
     * @return
     */
    protected Collection<String> getRoles(MuleEvent event)
    {
        return roles;
    }
    public Collection<String> getPermissions()
    {
        return permissions;
    }

    public void setRoles(Collection<String> roles)
    {
        this.roles = roles;
    }

    public void setPermissions(Collection<String> permissions)
    {
        this.permissions = permissions;
    }
    
}
