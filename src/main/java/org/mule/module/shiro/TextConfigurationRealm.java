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
/**
 * A class which initializes the user/role definitions. See SHIRO-228.
 */
public class TextConfigurationRealm extends org.apache.shiro.realm.text.TextConfigurationRealm
{

    @Override
    protected void onInit()
    {
        super.onInit();
        processDefinitions();
    }

}
