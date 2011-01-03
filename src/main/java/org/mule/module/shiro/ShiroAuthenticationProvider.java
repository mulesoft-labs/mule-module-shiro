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
