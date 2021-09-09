/**
 * 
 */
package net.ecology.base;

import net.ecology.exceptions.CerberusException;
import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
public abstract class AbstractDataAdapter implements DataAdapter {
	protected abstract Context marshall(Context context) throws CerberusException;
	protected abstract Context unmarshall(Context context) throws CerberusException;

	/**
	 * The process of converting the data or the objects into a byte-stream
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	public Context marshal(Context context) {
		return marshall(context);
	}

	/**
	 * The process of converting the byte-stream back to their original data or object
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	public Context unmarshal(Context context) {
		return unmarshall(context);
	}
}
