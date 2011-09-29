/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.modules.shiro;

import org.mule.tck.FunctionalTestCase;
import org.mule.transport.http.HttpConstants;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

public abstract class AbstractShiroTestCase extends FunctionalTestCase {

    protected void doRequest(String realm,
                           String host,
                           String user,
                           String pass,
                           String url,
                           boolean handshake,
                           boolean preemtive,
                           int result) throws Exception
    {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        client.getState().setCredentials(new AuthScope(host, -1, realm),
            new UsernamePasswordCredentials(user, pass));
        GetMethod get = new GetMethod(url);
        get.setDoAuthentication(handshake);

        try
        {
            int status = client.executeMethod(get);
            if (status == HttpConstants.SC_UNAUTHORIZED && handshake == true)
            {
                // doAuthentication = true means that if the request returns 401, 
                // the HttpClient will resend the request with credentials
                status = client.executeMethod(get);
                if (status == HttpConstants.SC_UNAUTHORIZED && handshake == true)
                {
                    // doAuthentication = true means that if the request returns 401, 
                    // the HttpClient will resend the request with credentials
                    status = client.executeMethod(get);
                }
            }
            assertEquals(result, status);
        }
        finally
        {
            get.releaseConnection();
        }
    }

}
