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
import org.mule.api.security.SecurityException;
import org.mule.security.AbstractSecurityProvider;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.support.DelegatingSubject;

public class ShiroAuthenticationProvider extends AbstractSecurityProvider
{
    private SecurityManager securityManager;
    
    public ShiroAuthenticationProvider()
    {
        super("shiro");
    }

    public Authentication authenticate(Authentication authentication) throws SecurityException
    {
        AuthenticationToken token;
        if (authentication instanceof ShiroAuthentication)
        {
            token = ((ShiroAuthentication) authentication).getToken();
        }
        else 
        {
            token = createShiroToken(authentication);
        }
        
        DelegatingSubject subject = new DelegatingSubject(securityManager);
        subject.login(token);
        
        return new ShiroAuthenticationResult(subject, authentication.getProperties());
    }

    protected AuthenticationToken createShiroToken(Authentication authentication)
    {
        AuthenticationToken token;
        token = new UsernamePasswordToken((String)authentication.getPrincipal(),
            authentication.getCredentials().toString().toCharArray());
        return token;
    }

    public void setDelegate(SecurityManager securityManager)
    {
        this.securityManager = securityManager;
    }

    public SecurityManager getDelegate()
    {
        return securityManager;
    }

}
