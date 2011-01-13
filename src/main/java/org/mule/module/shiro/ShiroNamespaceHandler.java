/*
 * $Id: SpringSecurityNamespaceHandler.java 20320 2010-11-24 15:03:31Z dfeist $
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.shiro;

import org.mule.api.config.MuleProperties;
import org.mule.config.spring.parsers.generic.ChildDefinitionParser;
import org.mule.config.spring.parsers.generic.NamedDefinitionParser;
import org.mule.config.spring.parsers.specific.SecurityFilterDefinitionParser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Registers a Bean Definition Parser for handling Shiro related elements.
 */
public class ShiroNamespaceHandler extends NamespaceHandlerSupport
{
    public void init()
    {
        registerBeanDefinitionParser("security-manager", new NamedDefinitionParser(MuleProperties.OBJECT_SECURITY_MANAGER));
        registerBeanDefinitionParser("delegate-security-provider", new ChildDefinitionParser("provider", ShiroAuthenticationProvider.class));
        registerBeanDefinitionParser("authorization-filter", new SecurityFilterDefinitionParser(AuthorizationFilter.class));
        registerBeanDefinitionParser("http-url-pattern-filter", new SecurityFilterDefinitionParser(HttpUrlPatternFilter.class));
    }
}