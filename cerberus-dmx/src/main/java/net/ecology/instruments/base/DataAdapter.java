/**
 * 
 */
package net.ecology.instruments.base;

import java.io.InputStream;
import java.util.List;

import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;

/**
 * @author ducbq
 *
 */
public abstract class DataAdapter implements IAdapter {
	/**
	 * The process of converting the byte-stream back to their original data or object
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	public Context unmarshall(Context context) {
		return unmarshalling(context);
	}

	protected List<String[]> marshalling(Context dataContext, String separator, int skipLines) {
		return null;
	}

	protected List<String[]> marshalling(InputStream inputStream, String separator, int skipLines) {
		return null;
	}

	/**
	 * The process of converting the byte-stream back to their original data or object
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	protected Context unmarshalling(Context context) {
		return context;
	}

	@Override
	public List<?> marshall(Object object, String separator, int skipLines) {
		if (null == object || !(object instanceof InputStream))
			throw new CerberusException("Context object have to be a valid InputStream. ");

		return marshalling((InputStream)object, separator, skipLines);
	}
}