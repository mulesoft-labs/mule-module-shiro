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

import org.mule.api.security.Authentication;

import java.util.Map;

import org.apache.shiro.authc.AuthenticationToken;

public class ShiroAuthentication implements Authentication
{

    private final AuthenticationToken token;
    private boolean authenticated;
    private Map properties;

    public ShiroAuthentication(AuthenticationToken token)
    {
        this(token, null);
    }
    
    public ShiroAuthentication(AuthenticationToken token, Map properties)
    {
        this.token = token;
        this.properties = properties;
    }

    public void setAuthenticated(boolean b)
    {
        this.authenticated = b;
    }

    public boolean isAuthenticated()
    {
        return authenticated;
    }

    public Object getCredentials()
    {
        return token.getCredentials();
    }

    public Object getPrincipal()
    {
        return token.getPrincipal();
    }

    public Map getProperties()
    {
        return properties;
    }

    public void setProperties(Map properties)
    {
        this.properties = properties;
    }

    public AuthenticationToken getToken()
    {
        return token;
    }

}
