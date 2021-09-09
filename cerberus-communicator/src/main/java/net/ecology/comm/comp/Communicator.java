/**
 * 
 */
package net.ecology.comm.comp;

import net.ecology.comm.domain.CorpMimeMessage;
import net.ecology.exceptions.CerberusException;
import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
public interface Communicator {
	void sendEmail(CorpMimeMessage mailMessage) throws CerberusException;
	void send(Context context) throws CerberusException;
}
