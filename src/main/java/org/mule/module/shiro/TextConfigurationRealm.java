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
