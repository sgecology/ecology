/**
 * 
 */
package net.ecology.comm.comp;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.comm.domain.CorpMimeMessage;
import net.ecology.exceptions.CerberusException;
import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
@Component(value="emailCommunicator")
public class EmailCommunicator implements Communicator {

	@Inject 
	private CommunicatorServiceHelper communicatorServiceHelper;

	@Override
	public void sendEmail(CorpMimeMessage mailMessage) throws CerberusException {
		System.out.println("net.paramount.comm.component.EmailServiceImpl.send(MailMessage)");
	}

	@Override
	public void send(Context context) throws CerberusException {
		communicatorServiceHelper.sendEmail(context);
	}
}
