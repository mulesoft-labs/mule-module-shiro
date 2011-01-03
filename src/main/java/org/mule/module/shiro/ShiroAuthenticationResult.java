package org.mule.module.shiro;

import org.mule.api.security.Authentication;

import java.util.Map;

import org.apache.shiro.subject.Subject;

public class ShiroAuthenticationResult implements Authentication
{

    private final Subject subject;
    private boolean authenticated;
    private Map properties;

    public ShiroAuthenticationResult(Subject subject)
    {
        this(subject, null);
    }
    
    public ShiroAuthenticationResult(Subject subject, Map properties)
    {
        this.subject = subject;
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
        return null;
    }

    public Object getPrincipal()
    {
        return subject.getPrincipals();
    }

    public Map getProperties()
    {
        return properties;
    }

    public void setProperties(Map properties)
    {
        this.properties = properties;
    }

    public Subject getSubject()
    {
        return subject;
    }

}
