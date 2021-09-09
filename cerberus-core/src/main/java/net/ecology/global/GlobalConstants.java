/**
 * 
 */
package net.ecology.global;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

/**
 * @author bqduc
 *
 */
public interface GlobalConstants {
	/////////////////////////////////////////////////////////////////////////////////
	static final String language_vi = "vi";
	static final String country_VN = "VN";

	static final Locale USA = Locale.US;
	static final Locale VIETNAM = new Locale(language_vi, country_VN);
	/////////////////////////////////////////////////////////////////////////////////
	
	static final String QNS_PACKAGE = "net.ecology";
	static final String QNS_PACKAGE_ADMINFACES = "com.github.adminfaces";

	static final long NONE_OBJECT_ID = -1;
	static final String NONE_OBJECT_CODE = "NONE";
	static final String NONE_OBJECT_NAME = "NONEE";
	static final String NONE_OBJECT_TRANSLATED_NAME = "NONEED";
	
	static final String AUTHENTICATED_PROFILE = "authenticatedProfile";

	static final short	SIZE_STRING_TINY = 25;
  static final short  SIZE_STRING_SMALL = 50;

	static final byte		SIZE_SERIAL = 15;
	static final byte		SIZE_CODE_MIN = 3;
	static final byte		SIZE_CODE_MEDIUM = 15;
	static final byte		SIZE_CODE = 20;//Including the backup part
	static final byte		SIZE_CURRENCY_CODE = 5;
	static final byte		SIZE_POSTAL_CODE = 7;
	static final short	SIZE_NAME = 250;
	static final short	SIZE_NAME_MEDIUM = 150;
	static final short	SIZE_NAME_SHORT = 100;
	static final byte		SIZE_LANGUAGE_CODE = 7;

	static final byte		SIZE_ISBN_10 = 10;
	static final byte		SIZE_ISBN_13 = 13;
	static final byte		SIZE_BARCODE = 25;

	static final short	SIZE_ADDRESS_MEDIUM = 150;
  static final short  SIZE_ADDRESS = 250;
  static final short  SIZE_CITY = 170;
	
  static final short	SIZE_COUNTRY = 80;

	static final String SERIAL_PATTERN = StringUtils.repeat("0", GlobalConstants.SIZE_CODE);

	static final String KEY_CONTEXT_CLASS = "contextClass";

	static final short defaultNumberOfThreads = 50;

	static final String CONFIG_GROUP_APP = "app.config";
	static final String CONFIG_ENTRY_NAME_EMAIL = "config.email";
	static final String CONFIG_EMAIL_HOST = "email.host";
	static final String CONFIG_EMAIL_PORT = "email.port";
	static final String CONFIG_EMAIL_USER_NAME = "email.username";
	static final String CONFIG_EMAIL_PASSWORD = "email.password";
	static final String CONFIG_EMAIL_START_TLS_ENABLE = "mail.smtp.starttls.enable";
	static final String CONFIG_EMAIL_AUTH = "mail.smtp.auth";
	static final String CONFIG_EMAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	static final String CONFIG_EMAIL_TRANSPORT_PROTOCOLX = "email.transportProtocol";
	static final String CONFIG_EMAIL_DEBUG = "email.debug";
	static final String CONFIG_EMAIL_SSL_TRUST = "email.smtp.ssl.trust";
	static final String CONFIG_EMAIL_START_TLS_REQUIRED = "email.smtpStartTlsRequired";
	static final String CONFIG_EMAIL_ENCODING = "email.encoding";

	static final String CONFIG_APP_ACCESS_URL = "appAccessUrl";
}
