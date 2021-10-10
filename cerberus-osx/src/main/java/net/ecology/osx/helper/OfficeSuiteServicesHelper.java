/**
 * 
 */
package net.ecology.osx.helper;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;
import net.ecology.model.XWorkbook;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.XContainer;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author ducbui
 *
 */
@Builder
public class OfficeSuiteServicesHelper implements Serializable {
	/**
   * 
   */
  private static final long serialVersionUID = 4535045388946671757L;

  protected Context initConfigData(final File zipFile) {
		Context executionContext = Context.builder().build();

		Map<String, String> secretKeyMap = CollectionsUtility.newHashedMap("Vietbank_14.000.xlsx", "thanhcong");
		Map<String, List<String>> sheetIdMap = CollectionsUtility.newMap();
		sheetIdMap.put("Vietbank_14.000.xlsx", CollectionsUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));

		executionContext.put(OSXConstants.DATA_FILE, zipFile);
		executionContext.put(OSXConstants.ENCRYPTED_KEYS, secretKeyMap);
		executionContext.put(OSXConstants.PROCESSING_WORKBOOK_IDS, CollectionsUtility.arraysAsList(new String[] {"Vietbank_14.000.xlsx", "data-catalog.xlsx"}));
		executionContext.put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING);
		executionContext.put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdMap);
		return executionContext;
	}

	public XContainer loadDefaultZipConfiguredData(final File sourceZipFile) throws CerberusException {
		XContainer bucketContainer = null;
		Context executionContext = null;
		try {
			executionContext = this.initConfigData(sourceZipFile);
			bucketContainer = OfficeSuiteServiceProvider
					.builder()
					.build()
					.loadSpreadsheetInZip(executionContext);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return bucketContainer;
	}

	public XContainer loadDefaultZipConfiguredData(final Context executionContext) throws CerberusException {
		return OfficeSuiteServiceProvider
					.builder()
					.build()
					.loadSpreadsheetInZip(executionContext);
	}

	public XContainer loadZipDataFromInputStream(final String originFileName, final InputStream inputStream) throws CerberusException {
		XContainer bucketContainer = null;
		Context executionContext = null;
		File targetDataFile = null;
		try {
			targetDataFile = CommonUtility.createFileFromInputStream(originFileName, inputStream);
			executionContext = this.initConfigData(targetDataFile);
			bucketContainer = OfficeSuiteServiceProvider
					.builder()
					.build()
					.loadSpreadsheetInZip(executionContext);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return bucketContainer;
	}

  public XContainer loadOfficeDataFromCompressedInputStream(Context context) throws CerberusException {
    return OfficeSuiteServiceProvider
          .builder()
          .build()
          .loadSpreadsheetInZip(context);
  }

  public XWorkbook unmarshallContacts(Context executionContext) {
		XWorkbook fetchedDataWorkbook = null;
		return fetchedDataWorkbook;
	}

	public Context loadSpreadsheetFromZip(Context context) throws CerberusException {
		try {
			context = OfficeSuiteServiceProvider
					.builder()
					.build()
					.loadSpreadsheetData(context);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return context;
	}
}
