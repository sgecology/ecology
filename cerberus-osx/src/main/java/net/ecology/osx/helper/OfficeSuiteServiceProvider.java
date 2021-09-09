/**
 * 
 */
package net.ecology.osx.helper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.poifs.filesystem.FileMagic;

import lombok.Builder;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.exceptions.CerberusException;
import net.ecology.model.Context;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.OSXWorkbook;
import net.ecology.model.osx.OSXWorksheet;
import net.ecology.model.osx.OsxBucketContainer;
import net.ecology.osx.model.DmxWorkbook;
import net.ecology.osx.model.OfficeDocumentType;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author bqduc
 *
 */
@Builder
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OfficeSuiteServiceProvider {
	protected OfficeDocumentType detectOfficeDocumentType(InputStream inputStream) throws CerberusException {
		OfficeDocumentType excelSheetType = OfficeDocumentType.INVALID;
		InputStream checkInputStream = null;
		checkInputStream = FileMagic.prepareToCheckMagic(inputStream);
		try {
			if (FileMagic.OOXML.equals(FileMagic.valueOf(checkInputStream))) {
				excelSheetType = OfficeDocumentType.XSSF_WORKBOOK;
			} else {
				excelSheetType = OfficeDocumentType.HSSF_WORKBOOK;
			}
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return excelSheetType;
	}

	protected OSXWorkbook readXlsxByEventHandler(final Map<?, ?> params) throws CerberusException {
		return XSSFEventDataHelper.instance(params).readXlsx();
	}

	protected OSXWorkbook readXlsxByStreaming(final Map<?, ?> params) throws CerberusException {
		return OfficeStreamingReaderHealper.builder().build().readXlsx(params);
	}

	public DmxWorkbook loadExcelData(Context context) throws CerberusException {
		DmxWorkbook dataWorkbook = DmxWorkbook.builder().build();
		OfficeMarshalType officeMarshalType = OfficeMarshalType.STREAMING;
		if (context.containsKey(OSXConstants.XLSX_MARSHALLING_DATA_METHOD)) {
			officeMarshalType = (OfficeMarshalType) context.get(OSXConstants.XLSX_MARSHALLING_DATA_METHOD);
		}

		if (OfficeMarshalType.EVENT_HANDLER.equals(officeMarshalType)) {
			//workbookContainer = this.readXlsxByEventHandler(parameters);
		} else if (OfficeMarshalType.STREAMING.equals(officeMarshalType)) {
			dataWorkbook = OfficeStreamingReaderHealper.builder().build().loadXlsxData(
					(String)context.get(OSXConstants.PROCESSING_WORKBOOK_ID), 
					(InputStream)context.get(OSXConstants.PROCESSING_WORKBOOK_STREAM), 
					(String)context.get(OSXConstants.ENCRYPTED_KEY), 
					(String[])context.get(OSXConstants.PROCESSING_WORKSHEET_IDS)); 
		} else {
			// Not implemented yet
		  throw new CerberusException("The application did not support office marshall type: " + officeMarshalType);
		}
		return dataWorkbook;
	}

	public OSXWorkbook readExcelFile(final Map<?, ?> parameters) throws CerberusException {
	  OSXWorkbook workbookContainer = null;
		OfficeMarshalType officeMarshalType = OfficeMarshalType.STREAMING;
		if (parameters.containsKey(OSXConstants.XLSX_MARSHALLING_DATA_METHOD)) {
			officeMarshalType = (OfficeMarshalType) parameters.get(OSXConstants.XLSX_MARSHALLING_DATA_METHOD);
		}

		if (OfficeMarshalType.EVENT_HANDLER.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByEventHandler(parameters);
		} else if (OfficeMarshalType.STREAMING.equals(officeMarshalType)) {
			workbookContainer = this.readXlsxByStreaming(parameters);
		} else {
			// Not implemented yet
		  throw new CerberusException("The application did not support office marshall type: " + officeMarshalType);
		}
		return workbookContainer;
	}

	public OSXWorkbook readExcelFile(final Context context) throws CerberusException {
    return readExcelFile(context.getContextData());
  }

	public OsxBucketContainer loadSpreadsheetInZip(final Context context) throws CerberusException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = CollectionsUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		OSXWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> worksheetIds = null;
		Map<String, String> passwordMap = null;
		try {
			zipFile = (File) context.get(OSXConstants.DATA_FILE);
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, (List<String>) context.get(OSXConstants.PROCESSING_WORKBOOK_IDS));
			if (zipInputStreams.isEmpty()) {
				return bucketContainer;
			}

			passwordMap = (Map) context.get(OSXConstants.ENCRYPTED_KEYS);
			sheetIdsMap = (Map) context.get(OSXConstants.PROCESSING_WORKSHEET_IDS);
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				worksheetIds = (List<String>) sheetIdsMap.get(zipEntry);
				processingParameters.putAll(context.getContextData());
				processingParameters.remove(OSXConstants.DATA_FILE);
				processingParameters.put(OSXConstants.INPUT_STREAM, zipInputStream);
				processingParameters.put(OSXConstants.PROCESSING_WORKSHEET_IDS, worksheetIds);
				processingParameters.put(OSXConstants.ENCRYPTED_KEYS, (String) passwordMap.get(zipEntry));
				workbookContainer = readExcelFile(processingParameters);
				if (null != workbookContainer) {
					bucketContainer.put(zipEntry, workbookContainer);
				}
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return bucketContainer;
	}

	public Context loadSpreadsheetData(final Context context) throws CerberusException {
		Context targetContext = null;
		Map<String, InputStream> inputStreams = null;
		Map<String, Object> processingParameters = CollectionsUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		InputStream inputStream = null;
		Context currentContext = Context.builder().build();
		DmxWorkbook dataWorkbook = null;
		try {
			if (Boolean.TRUE.equals(context.get(OSXConstants.IS_COMPRESSED_FILE))){
				inputStreams = CommonUtility.extractZipInputStreams(
						(File)context.get(OSXConstants.DATA_FILE), 
						(String[]) context.get(OSXConstants.PROCESSING_WORKBOOK_IDS));
				if (inputStreams.isEmpty()) {
					return targetContext;
				}
			} else {
				inputStreams = CollectionsUtility.createMap();
			}

			targetContext = Context.builder().build();
			for (String entry : inputStreams.keySet()) {
				inputStream = inputStreams.get(entry);
				officeDocumentType = detectOfficeDocumentType(inputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				processingParameters.putAll(context.getContextData());
				processingParameters.remove(OSXConstants.DATA_FILE);
				
				currentContext.put(OSXConstants.PROCESSING_WORKBOOK_STREAM, inputStream);
				currentContext.put(OSXConstants.PROCESSING_WORKBOOK_ID, entry);
				if (null != ((Map) context.get(OSXConstants.PROCESSING_WORKSHEET_IDS)).get(entry)){
					currentContext.put(OSXConstants.PROCESSING_WORKSHEET_IDS, ((Map) context.get(OSXConstants.PROCESSING_WORKSHEET_IDS)).get(entry));
				}

				if (null != context.get(OSXConstants.ENCRYPTED_KEYS) && null != ((Map)context.get(OSXConstants.ENCRYPTED_KEYS)).get(entry)){
					currentContext.put(OSXConstants.ENCRYPTED_KEYS, (String) ((Map) context.get(OSXConstants.ENCRYPTED_KEYS)).get(entry));
				}
				dataWorkbook = loadExcelData(currentContext);
				//workbookContainer = readExcelFile(processingParameters);
				if (null != dataWorkbook) {
					targetContext.put(entry, dataWorkbook);
				}
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return targetContext;
	}

	public OsxBucketContainer extractOfficeDataFromZip(final Context context) throws CerberusException {
		OsxBucketContainer bucketContainer = OsxBucketContainer.instance();
		File zipFile = null;
		Map<String, InputStream> zipInputStreams = null;
		Map<String, Object> processingParameters = CollectionsUtility.createMap();
		OfficeDocumentType officeDocumentType = OfficeDocumentType.INVALID;
		OSXWorkbook workbookContainer = null;
		InputStream zipInputStream = null;
		Map<String, List<String>> sheetIdsMap = null;
		List<String> workbookIds = null;
		Map<String, String> passwordMap = null;
		try {
			if (context.containsKey(OSXConstants.MASTER_BUFFER_DATA_BYTES) && Boolean.FALSE.equals(context.get(OSXConstants.FROM_ATTACHMENT))) {
				zipFile = CommonUtility.createDataFile((String)context.get(OSXConstants.MASTER_ARCHIVED_FILE_NAME), (byte[])context.get(OSXConstants.MASTER_BUFFER_DATA_BYTES));
			} /*else if (executionContextParams.containKey(OSXConstants.MASTER_BUFFER_DATA_BYTES) && executionContextParams.containKey(OSXConstants.MASTER_ARCHIVED_FILE_NAME)) {
				zipFile = CommonUtility.createDataFile((String)executionContextParams.get(OSXConstants.MASTER_ARCHIVED_FILE_NAME), (byte[])executionContextParams.get(OSXConstants.MASTER_BUFFER_DATA_BYTES));
			}*/

			if (null==zipFile) {
				return bucketContainer;
			}

			if (context.containsKey(OSXConstants.ENCRYPTED_KEYS)) {
				passwordMap = (Map) context.get(OSXConstants.ENCRYPTED_KEYS);
			}

			if (context.containsKey(OSXConstants.PROCESSING_WORKSHEET_IDS)) {
				sheetIdsMap = (Map) context.get(OSXConstants.PROCESSING_WORKSHEET_IDS);
			} else if (context.containsKey(OSXConstants.MAPPING_DATABOOKS_DATASHEETS)) {
				sheetIdsMap = (Map) context.get(OSXConstants.MAPPING_DATABOOKS_DATASHEETS);
			}

			if (context.containsKey(OSXConstants.PROCESSING_DATABOOK_IDS)) {
				workbookIds = (List<String>)context.get(OSXConstants.PROCESSING_DATABOOK_IDS);
			}
			zipInputStreams = CommonUtility.extractZipInputStreams(zipFile, workbookIds);
			
			for (String zipEntry : zipInputStreams.keySet()) {
				zipInputStream = zipInputStreams.get(zipEntry);
				officeDocumentType = detectOfficeDocumentType(zipInputStream);
				if (!OfficeDocumentType.isExcelDocument(officeDocumentType)) {
					continue;
				}

				if (null != sheetIdsMap) {
					processingParameters.put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdsMap.get(zipEntry));
				}
				processingParameters.putAll(context.getContextData());
				processingParameters.remove(OSXConstants.DATA_FILE);
				processingParameters.put(OSXConstants.INPUT_STREAM, zipInputStream);
				processingParameters.put(OSXConstants.ENCRYPTED_KEYS, (String) passwordMap.get(zipEntry));
				workbookContainer = readExcelFile(processingParameters);
				if (null != workbookContainer) {
					bucketContainer.put(zipEntry, workbookContainer);
				}
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return bucketContainer;
	}

	public static void main(String[] args) throws Exception {
		String zipFileName = "D:\\git\\paramount\\msp-osx\\src\\main\\resources\\data\\develop_data.zip";
		
		OsxBucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadDefaultZipConfiguredData(new File(zipFileName));
		OSXWorkbook workbookContainer = null;
		Set<Object> keys = bucketContainer.getKeys();
		for (Object key : keys) {
			workbookContainer = (OSXWorkbook) bucketContainer.get(key);
			System.out.println("############################### " + key + " ###############################");
			displayWorkbookContainer(workbookContainer);
		}

		if (null != keys)
			return;
	}

	protected static void displayWorkbookContainer(OSXWorkbook workbookContainer) {
		for (OSXWorksheet worksheetContainer : workbookContainer.datasheets()) {
			System.out.println("Sheet: " + worksheetContainer.getId());
			for (List<?> dataRow : worksheetContainer.getValues()) {
				System.out.println(dataRow);
			}
			System.out.println("============================DONE==============================");
		}
	}
}
