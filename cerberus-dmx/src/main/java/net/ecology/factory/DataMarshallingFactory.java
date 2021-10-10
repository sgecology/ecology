/**
 * 
 */
package net.ecology.factory;

import java.lang.reflect.InvocationTargetException;

import lombok.Builder;
import net.ecology.common.BeanUtility;
import net.ecology.domain.MarshallingProvider;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.instruments.CSVAdapter;
import net.ecology.instruments.SpreadsheetDataAdapter;
import net.ecology.instruments.base.IAdapter;
import net.ecology.instruments.base.Marshaller;

/**
 * @author ducbq
 *
 */
@Builder
public class DataMarshallingFactory {
	private static IAdapter csvDataAdapter = null;
	private static IAdapter excelDataAdapter = null;

	public static IAdapter getAdapter(MarshallingProvider provider){
		if (MarshallingProvider.PROVIDER_CSV.equals(provider)){
			if (null == csvDataAdapter){
				csvDataAdapter = CSVAdapter.builder().build();
			}
			return csvDataAdapter;
		}

		if (MarshallingProvider.PROVIDER_EXCEL.equals(provider)){
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
