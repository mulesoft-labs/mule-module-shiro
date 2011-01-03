/*
 * $Id: SpringSecurityMessages.java 20320 2010-11-24 15:03:31Z dfeist $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.module.shiro;

import org.mule.config.i18n.Message;
import org.mule.config.i18n.MessageFactory;

public class ShiroMessages extends MessageFactory
{
    private static final ShiroMessages factory = new ShiroMessages();
    
    private static final String BUNDLE_PATH = getBundlePath("shiro");

    public static Message basicFilterCannotHandleHeader(String header)
    {
        return factory.createMessage(BUNDLE_PATH, 1, header);
    }

    public static Message authRealmMustBeSetOnFilter()
    {
        return factory.createMessage(BUNDLE_PATH, 2);
    }

    public static Message noSecurityProviderFound()
    {
        return factory.createMessage(BUNDLE_PATH, 3);
    }
}


