/**
 * 
 */
package net.ecology.comm.comp;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.comm.domain.MailMessage;
import net.ecology.exceptions.CerberusException;

/**
 * @author ducbq
 *
 */
@Component(value="emailCommunicator")
public class EmailCommunicator implements Communicator {

	@Inject 
	private CommunicatorManager communicatorServiceHelper;

	@Override
	public void sendEmail(MailMessage mailMessage) throws CerberusException {
		System.out.println("net.paramount.comm.component.EmailServiceImpl.send(MailMessage)");
	}

	@Override
	public boolean sendEmail(MailMessage mailMessage, String templateId, String mailTemplateLoadingDir) throws CerberusException {
		return communicatorServiceHelper.sendEmail(mailMessage, templateId, mailTemplateLoadingDir);
	}
}
