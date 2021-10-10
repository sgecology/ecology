/**
 * 
 */
package net.ecology.instruments.base;

import java.util.List;

import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;

/**
 * @author ducbq
 *
 */
public interface IAdapter {
	List<?> marshall(Object contextObject, String separator, int skipLines) throws CerberusException;
	Context unmarshall(Context context);
}
