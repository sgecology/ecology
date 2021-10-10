/**
 * 
 */
package net.ecology.comm.comp;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.css.service.config.ConfigurationService;
import net.ecology.entity.config.Configuration;
import net.ecology.exceptions.CryptographyException;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;
import net.ecology.security.GlobalCryptogramRepository;
import net.ecology.security.base.Cryptographer;

/**
 * @author ducbq
 *
 * Global communication repository manager
 */
@Component
public class EmailConfigurationHelper extends BasisComp {
	private static final long serialVersionUID = -2433918570834008630L;

	@Inject
	private ConfigurationService configurationService;

	public void configureEmailService() {
		try {
			if (false == configurationService.isExistsByName(GlobalConstants.CONFIG_ENTRY_NAME_EMAIL)) {
				buildEmailConfigurations();
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void buildEmailConfigurations() throws CryptographyException {
		GlobalCryptogramRepository globalCryptogramRepository = GlobalCryptogramRepository.getInstance();
		Cryptographer defaultCryptographer = globalCryptogramRepository.getDefaultCryptographer();
		Configuration emailConfig = Configuration.builder()
				.group(GlobalConstants.CONFIG_GROUP_APP)
				.name(GlobalConstants.CONFIG_ENTRY_NAME_EMAIL)
				.value("Application email configuration")
				.build();

		configurationService.saveAndFlush(emailConfig)
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_HOST).value("smtp.gmail.com").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_PORT).value("587").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_USER_NAME).value(defaultCryptographer.encode("duc.bui.appdev@gmail.com", GlobalCryptogramRepository.SECRET_KEY)).build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_PASSWORD).value(defaultCryptographer.encode("zaq1@#EDC", GlobalCryptogramRepository.SECRET_KEY)).build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_START_TLS_ENABLE).value("true").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_AUTH).value("true").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_TRANSPORT_PROTOCOL).value("smtp").build()) 
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_TRANSPORT_PROTOCOLX).value("smtp").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_DEBUG).value("false").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_SSL_TRUST).value("smtp.gmail.com").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_START_TLS_REQUIRED).value("true").build())
		.saveAndFlush(Configuration.builder().parent(emailConfig).name(GlobalConstants.CONFIG_EMAIL_ENCODING).value(GlobeConstants.ENCODING_NAME_UTF8).build());
	}
}