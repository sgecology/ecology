/**
 * 
 */
package net.ecology.adapter;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.base.AbstractDataAdapter;
import net.ecology.base.DataAdapter;
import net.ecology.common.CommonConstants;
import net.ecology.exceptions.CerberusException;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.utility.CSVUtilityHelper;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class CSVDataAdapter extends AbstractDataAdapter implements DataAdapter {
	/**
	 * The process of converting the data or the objects into a byte-stream
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	protected Context marshall(Context context) throws CerberusException{
		List<String[]> fetchedData = null;
		Context dataContext = null;
		try {
			dataContext = Context.builder().build();
			IOContainer ioContainer = (IOContainer)context.get(CommonConstants.IO_DATA_CONTAINER);
			for (Object key :ioContainer.keys()) {
				fetchedData = CSVUtilityHelper.builder().build().fetchData(
						IOContainer.builder().build().put(ioContainer.get(key)),
						(char)context.get(CommonConstants.SEPARATOR),
						(int)context.get(CommonConstants.SKIP_LINES));
				dataContext.put((String)key, fetchedData);
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return dataContext;
	}

	/**
	 * The process of converting the byte-stream back to their original data or object
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	protected Context unmarshall(Context context) throws CerberusException{
		try {
			context.get("");
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return context;
	}
}
