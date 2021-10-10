/**
 * 
 */
package net.ecology.instruments;

import java.io.InputStream;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.instruments.base.DataAdapter;
import net.ecology.instruments.base.IAdapter;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class SpreadsheetDataAdapter extends DataAdapter implements IAdapter {
	
	/**
	 * The process of converting the data or the objects into a byte-stream
	 * 
	 * @param context
	 * 
	 * @return Context
	 */
	@Override
	protected List<String[]> marshalling(InputStream inputStream, String separator, int skipLines) {
		/*OSXDataRepository
		.builder()
		.build()
		.loadSpreadsheetData(dataContext);*/
		return null;
	}
}
