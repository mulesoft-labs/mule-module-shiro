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

import org.mule.api.MuleMessage;
import org.mule.api.security.Authentication;
import org.mule.api.security.SecurityException;
import org.mule.security.AbstractSecurityProvider;
import org.mule.transport.http.HttpConnector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.subject.WebSubject;

public class ShiroAuthenticationProvider extends AbstractSecurityProvider
{
    private SecurityManager securityManager;
    private boolean rememberMe;
    
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
        
        Subject subject;
        
        MuleMessage message = authentication.getEvent().getMessage();
        HttpServletRequest req = (HttpServletRequest) message.getInvocationProperty(HttpConnector.HTTP_SERVLET_REQUEST_PROPERTY);
        if (req != null && securityManager instanceof WebSecurityManager)
        {
            HttpServletResponse res = (HttpServletResponse) message.getInvocationProperty(HttpConnector.HTTP_SERVLET_RESPONSE_PROPERTY);
            subject = new WebSubject.Builder(securityManager, req, res).buildWebSubject();
        }
        else
        {
            subject = new Subject.Builder(securityManager).buildSubject();
        }
        
        subject.login(token);
        
        return new ShiroAuthenticationResult(subject, authentication.getProperties(), authentication.getEvent());
    }

    protected AuthenticationToken createShiroToken(Authentication authentication)
    {
        UsernamePasswordToken token = new UsernamePasswordToken((String)authentication.getPrincipal(),
            authentication.getCredentials().toString().toCharArray());
        token.setRememberMe(rememberMe);
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

    public boolean isRememberMe()
    {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe)
    {
        this.rememberMe = rememberMe;
    }

}
