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
