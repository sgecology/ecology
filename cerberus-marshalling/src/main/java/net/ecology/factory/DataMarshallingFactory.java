/**
 * 
 */
package net.ecology.factory;

import java.lang.reflect.InvocationTargetException;

import lombok.Builder;
import net.ecology.adapter.CSVDataAdapter;
import net.ecology.adapter.SpreadsheetDataAdapter;
import net.ecology.base.DataAdapter;
import net.ecology.base.Marshaller;
import net.ecology.common.BeanUtility;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.model.MarshallingProvider;

/**
 * @author ducbq
 *
 */
@Builder
public class DataMarshallingFactory {
	private static DataAdapter csvDataAdapter = null;
	private static DataAdapter excelDataAdapter = null;

	public static DataAdapter getAdapter(MarshallingProvider provider){
		if (MarshallingProvider.DATA_PROVIDER_CSV.equals(provider)){
			if (null == csvDataAdapter){
				csvDataAdapter = CSVDataAdapter.builder().build();
			}
			return csvDataAdapter;
		}

		if (MarshallingProvider.DATA_PROVIDER_EXCEL.equals(provider)){
			if (null == excelDataAdapter){
				excelDataAdapter = SpreadsheetDataAdapter.builder().build();
			}
			return excelDataAdapter;
		}

		throw new ContextExecutionException("The provider: [" + provider + "] not support!");
	}

	public static Marshaller<?, ?> requestMarshaller(Class<?> clasz) throws CerberusException {
		Marshaller<?, ?> marshaller = null;
		try {
			marshaller = (Marshaller<?, ?>)BeanUtility.getInstance().newInstance(clasz);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new CerberusException(e);
		}
		return marshaller;
	}
}
