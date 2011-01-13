/*
 * $Id: HttpFilterFunctionalTestCase.java 20320 2010-11-24 15:03:31Z dfeist $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.modules.shiro;

import org.mule.tck.FunctionalTestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

public class HttpUrlPatternFilterTestCase extends FunctionalTestCase
{

    protected String getConfigResources()
    {
        return "http-url-pattern-filter-config.xml";
    }

    public void testUnauthroizedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "user", "password", "http://localhost:4567/permissions/auth", true, true, 405);
        doRequest("mule-realm", "localhost", "user", "password", "http://localhost:4567/roles/auth", true, true, 405);
    }

    public void testAuthroizedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "administrator", "password", "http://localhost:4567/permissions/auth", true, true, 200);
        doRequest("mule-realm", "localhost", "administrator", "password", "http://localhost:4567/roles/noauth", true, true, 200);
    }
    
    private void doRequest(String realm,
                           String host,
                           String user,
                           String pass,
                           String url,
                           boolean handshake,
                           boolean preemtive,
                           int result) throws Exception
    {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(preemtive);
        client.getState().setCredentials(new AuthScope(host, -1, realm),
            new UsernamePasswordCredentials(user, pass));
        GetMethod get = new GetMethod(url);
        get.setDoAuthentication(handshake);

        try
        {
            int status = client.executeMethod(get);
            assertEquals(result, status);
        }
        finally
        {
            get.releaseConnection();
        }
    }

}
