/**
 * 
 */
package net.ecology.instruments;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.opencsv.exceptions.CsvException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.exceptions.CerberusException;
import net.ecology.helper.CSVUtilityHelper;
import net.ecology.instruments.base.DataAdapter;
import net.ecology.instruments.base.IAdapter;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class CSVAdapter extends DataAdapter implements IAdapter {
	@Override
	protected List<String[]> marshalling(InputStream inputStream, String separator, int skipLines) throws CerberusException {
		List<String[]> fetchedData = null;
		try {
			fetchedData = CSVUtilityHelper.builder().build().fetchData(inputStream,
					separator.charAt(0),
					skipLines);
		} catch (IOException | CsvException e) {
			 throw new CerberusException(e);
		}
		return fetchedData;
	}
}