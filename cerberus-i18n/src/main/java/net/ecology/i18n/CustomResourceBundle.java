 package net.ecology.i18n;

import org.springframework.stereotype.Component;

import net.ecology.global.GlobeConstants;

/**
 * Adds support for UTF-8 based bundles for admin i18n messages
 * 
 * Taken from: https://stackoverflow.com/a/3646601/1260910
 *
 * @author ducbq
 */
 @Component
public class CustomResourceBundle extends MultiplePropertiesResourceBundle {

    protected static final String BUNDLE_NAME = "admin";
    protected static final String BUNDLE_EXTENSION = "properties";
    protected static final String CHARSET = GlobeConstants.ENCODING_NAME_UTF8;
    
    static String i18nMvp = "";
    static {
      i18nMvp = "i18n.mvp";
    }

    public CustomResourceBundle() {
    	super(new String[] {"i18n", "i18n.admin", "i18n.general", i18nMvp}, "*.properties");
    }
}
