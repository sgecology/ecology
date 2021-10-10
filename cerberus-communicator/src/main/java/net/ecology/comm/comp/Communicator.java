/**
 * 
 */
package net.ecology.comm.comp;

import net.ecology.comm.domain.MailMessage;
import net.ecology.exceptions.CerberusException;

/**
 * @author ducbq
 *
 */
public interface Communicator {
	void sendEmail(MailMessage mailMessage) throws CerberusException;
	boolean sendEmail(MailMessage mailMessage, String templateId, String mailTemplateLoadingDir) throws CerberusException;
}
