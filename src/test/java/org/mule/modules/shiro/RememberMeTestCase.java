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


public class RememberMeTestCase extends AbstractShiroTestCase
{

    protected String getConfigResources()
    {
        return "remember-me-config.xml";
    }

    public void testAuthroizedUser() throws Exception
    {
        doRequest("mule-realm", "localhost", "administrator", "password", "http://localhost:4567/permissions/auth", true, true, 200);
        doRequest("mule-realm", "localhost", "administrator", "password", "http://localhost:4567/permissions/auth", true, true, 200);
    }
    
}
