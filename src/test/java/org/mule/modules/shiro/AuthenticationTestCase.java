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

import org.mule.transport.http.HttpConstants;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class AuthenticationTestCase extends AbstractShiroTestCase
{

    protected String getConfigResources()
    {
        return "authentication-config.xml";
    }

    public void testAuthenticationFailureNoContext() throws Exception
    {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        GetMethod get = new GetMethod(getUrl());

        get.setDoAuthentication(false);

        try
        {
            int status = client.executeMethod(get);
            assertEquals(HttpConstants.SC_UNAUTHORIZED, status);
            assertTrue(get.getResponseBodyAsString().contains("no security context on the session. Authentication denied on endpoint"));
        }
        finally
        {
            get.releaseConnection();
        }
    }

    public void testAuthenticationFailureBadCredentials() throws Exception
    {
        doRequest(null, "localhost", "anonX", "anonX", getUrl(), true, false, 401);
    }

    protected String getUrl()
    {
        return "http://localhost:4567/index.html";
    }

    public void testAuthenticationAuthorised() throws Exception
    {
        doRequest(null, "localhost", "anon", "anon", getUrl(), false, true, 200);
    }

    public void testAuthenticationAuthorisedWithHandshake() throws Exception
    {
        doRequest(null, "localhost", "anon", "anon", getUrl(), true, false, 200);
    }

    public void xtestAuthenticationAuthorisedWithHandshakeAndBadRealm() throws Exception
    {
        doRequest("blah", "localhost", "anon", "anon", getUrl(), true, false, 401);
    }

    public void testAuthenticationAuthorisedWithHandshakeAndRealm() throws Exception
    {
        doRequest("mule-realm", "localhost", "ross", "ross", getUrl(), true, false, 200);
    }

}
