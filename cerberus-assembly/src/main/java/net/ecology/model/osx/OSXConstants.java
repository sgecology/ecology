/**
 * 
 */
package net.ecology.model.osx;

/**
 * @author bqduc
 *
 */
public interface OSXConstants {
  static final String RESOURCE_REPO = "repoResource";
	static final String RESOURCE_NAME = "inputResourceName";
	static final String WORK_DATA_SHEET = "workDataSheet";
	static final String INPUT_STREAM = "sourceInputStream";
	static final String PROCESSING_DATABOOK_IDS = "dataBookIds";
	static final String PROCESSING_WORKSHEET_IDS = "dataSheetIds";
	static final String PROCESSING_DATASHEET = "processingWorksheet";
	static final String PROCESSING_WORKBOOK = "processingWorkbook";
	static final String MAPPING_DATABOOKS_DATASHEETS = "dataSheetsMap";
	static final String DATABOOKS_DATASHEETS_MAP = "dataBooksSheetsMap";
	static final String DATA_INDEXES = "dataIndexes";
	static final String STARTED_ROW_INDEX = "startedRowIndex";
  static final String ENCRYPTED_KEY = "encryptionKey";
	static final String ENCRYPTED_KEYS = "encryptionKeys";
	static final String LIMITED_COLUMNS = "limitedColumns";
	static final String LIMITED_ROWS = "limitedRows";
	static final String XLSX_MARSHALLING_DATA_METHOD = "excelMarshalType";

	static final String MASTER_FILE = "masterFile";
	static final String MASTER_FILE_ENCRYPTION_KEY = "masterFileEncryptionKey";

	static final String MASTER_BUFFER_DATA_BYTES = "masterBuffer";
	static final String MASTER_ARCHIVED_FILE_NAME = "masterFileName";

	static final String MARSHALLING_OBJECTS = "marshallingObjects";
	static final String MARSHALLED_CONTAINER = "marshalledContainer";
	static final String FROM_ATTACHMENT = "fromAttachment";

	static final String CONFIGURATION_ENTRY = "configurationEntry";

	static final String PROCESSING_WORKBOOK_IDS = "compressedEntries";
	static final String DATA_FILE = "compressedFile";
	static final String IS_COMPRESSED_FILE = "isCompressedFile";

	static final String PROCESSING_WORKBOOK_ID = "xlsxEntry";
	static final String PROCESSING_WORKBOOK_STREAM = "xlsxEntryStream";
}
