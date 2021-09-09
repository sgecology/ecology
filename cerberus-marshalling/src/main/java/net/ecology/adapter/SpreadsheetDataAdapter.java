/**
 * 
 */
package net.ecology.adapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.base.AbstractDataAdapter;
import net.ecology.base.DataAdapter;
import net.ecology.model.Context;
import net.ecology.osx.OSXDataRepository;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class SpreadsheetDataAdapter extends AbstractDataAdapter implements DataAdapter {
	
	/**
	 * The process of converting the data or the objects into a byte-stream
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	protected Context marshall(Context context) {
		return OSXDataRepository
				.builder()
				.build()
				.loadSpreadsheetData(context);
	}

	/**
	 * The process of converting the byte-stream back to their original data or object
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	protected Context unmarshall(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
