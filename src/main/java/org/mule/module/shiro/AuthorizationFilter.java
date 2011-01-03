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

import java.util.Arrays;
import java.util.Collection;

import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;

/**
 * Ensures that the Authenticated user is authorized against the specified
 * set of permissions.
 */
public class AuthorizationFilter extends AbstractSecurityFilter
{
    private Collection<Permission> permissions;

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
            shiroAuth.getSubject().checkPermissions(getPermissions(event));
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
        return permissions;
    }

    public Collection<Permission> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Collection<Permission> permissions)
    {
        this.permissions = permissions;
    }

    public void setPermission(String permission)
    {
        this.permissions = Arrays.asList((Permission)new WildcardPermission(permission));
    }
    
}
