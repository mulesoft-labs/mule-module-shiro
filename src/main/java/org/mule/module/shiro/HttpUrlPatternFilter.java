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
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.security.CryptoFailureException;
import org.mule.api.security.EncryptionStrategyNotFoundException;
import org.mule.api.security.SecurityException;
import org.mule.api.security.SecurityProviderNotFoundException;
import org.mule.api.security.UnauthorisedException;
import org.mule.api.security.UnknownAuthenticationTypeException;
import org.mule.api.transport.PropertyScope;
import org.mule.transport.http.HttpConnector;
import org.mule.transport.http.filters.HttpBasicAuthenticationFilter;

import java.util.Collection;

import org.springframework.util.AntPathMatcher;

public class HttpUrlPatternFilter extends HttpBasicAuthenticationFilter
{
    private String urlPattern;
    private Collection<String> permissions;
    private Collection<String> roles;
    private String unauthorizedUrl;
    private String loginUrl;
    private AuthorizationFilter authorizationFilter;
    private AntPathMatcher matcher = new AntPathMatcher();
    
    @Override
    protected void doInitialise() throws InitialisationException
    {
        super.doInitialise();
        
        authorizationFilter = new AuthorizationFilter();
        authorizationFilter.setPermissions(permissions);
        authorizationFilter.setRoles(roles);
        authorizationFilter.setSecurityManager(getSecurityManager());
        authorizationFilter.setMuleContext(muleContext);
        authorizationFilter.setSecurityProviders(getSecurityProviders());
        authorizationFilter.initialise();
    }

    protected boolean applies(MuleEvent event)
    {
        MuleMessage message = event.getMessage();
        if (matcher.match(urlPattern, (String) message.getProperty(HttpConnector.HTTP_REQUEST_PROPERTY, PropertyScope.INBOUND)))
        {
            return true;
        }
        return false;
    }

    @Override
    public void doFilter(MuleEvent event)
        throws SecurityException, UnknownAuthenticationTypeException, CryptoFailureException,
        SecurityProviderNotFoundException, EncryptionStrategyNotFoundException, InitialisationException
    {
        if (applies(event))
        {
            try 
            {
                super.doFilter(event);
            } 
            catch (UnauthorisedException e)
            {
                // TODO: redirect
                throw e;
            }
            
            authorizationFilter.doFilter(event);
        }
    }

    public String getUrlPattern()
    {
        return urlPattern;
    }

    public void setUrlPattern(String urlPattern)
    {
        this.urlPattern = urlPattern;
    }

    public Collection<String> getPermissions()
    {
        return permissions;
    }

    public void setPermissions(Collection<String> requiredPermissions)
    {
        this.permissions = requiredPermissions;
    }

    public Collection<String> getRoles()
    {
        return roles;
    }

    public void setRoles(Collection<String> requiredRoles)
    {
        this.roles = requiredRoles;
    }

    public String getUnauthorizedUrl()
    {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl)
    {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String getLoginUrl()
    {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl)
    {
        this.loginUrl = loginUrl;
    }

}
