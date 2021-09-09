/**
 * 
 */
package net.sunrise.osx.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.model.Context;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.OSXWorkbook;
import net.ecology.model.osx.OSXWorksheet;
import net.ecology.model.osx.OsxBucketContainer;
import net.ecology.osx.helper.OfficeSuiteServiceProvider;
import net.ecology.osx.helper.OfficeSuiteServicesHelper;
import net.ecology.osx.model.DmxWorkbook;
import net.ecology.osx.model.DmxWorksheet;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author bqduc
 *
 */
public class OSXTestMain {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadSpreadsheetData();
		//loadZipData();
	  //doTestReadXlsx();
	}

	protected static void doTestReadXlsx() {
		Map<Object, Object> params = CollectionsUtility.createMap();
		String[] sheetIds = new String[]{/*"languages", "items", "localized-items"*/"inventory-items", "business-units"}; 
		OsxBucketContainer dataBucket = null;
		String dataSheetSource = "D:\\workspace\\aquariums.git\\aquarium\\aquarium-admin\\src\\main\\resources\\config\\data\\data-catalog.xlsx";
		List<String> sheetIdList = CollectionsUtility.createList("inventory-items", "business-units");
		dataSheetSource = "D:/git/heron/heron/src/main/resources/master-data/data-catalog.xlsx";
		try {
			params.put(OSXConstants.INPUT_STREAM, new FileInputStream(dataSheetSource));
			params.put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdList);
			params.put(OSXConstants.STARTED_ROW_INDEX, new Integer[] {1, 1, 1});
			OSXWorkbook workbookContainer = OfficeSuiteServiceProvider.builder()
			.build()
			.readExcelFile(params);
			
			OSXWorksheet osxWorksheet = workbookContainer.getDatasheet(sheetIds[0]);
			displayDatasheet(osxWorksheet);
			/*List<?> details = null;
			List<?> forthcomingBooks = (List<?>)workbookContainer.get("Forthcoming");
			List<?> onlineBooks = (List<?>)dataBucket.getBucketData().get("online-books");
			for (Object currentItem :forthcomingBooks) {
				System.out.println(currentItem);
			}

			for (Object currentItem :onlineBooks) {
				details = (List<?>)currentItem;
				System.out.println(details.size());
			}*/
			//System.out.println(dataBucket.getBucketData().get("Forthcoming"));
			//System.out.println(dataBucket.getBucketData().get("online-books"));
		} catch (Exception e) {
			e.printStackTrace();;
		}

	}

  protected static Context initContextData(
      final Map<String, String> secretKeys, 
      final Map<String, List<String>> sheetIdList, 
      final String[] zipEntries,
      final String originalFileName,
      final InputStream compressedZipInputStream) {

    return Context.builder().build()
        .put(OSXConstants.DATA_FILE, CommonUtility.createFileFromInputStream(originalFileName, compressedZipInputStream))
        .put(OSXConstants.ENCRYPTED_KEYS, secretKeys)
        .put(OSXConstants.PROCESSING_WORKBOOK_IDS, CollectionsUtility.arraysAsList(zipEntries))
        .put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING)
        .put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdList);
  }

  protected static Context initCompressedContextData(
  		File compressedFile,
      final String[] compressedEntries,
      final Map<String, String> secretKeys, 
      final Map<String, String[]> sheetIdList) {

    return Context.builder().build()
    		.put(OSXConstants.IS_COMPRESSED_FILE, Boolean.TRUE)
        .put(OSXConstants.DATA_FILE, compressedFile)
        .put(OSXConstants.ENCRYPTED_KEYS, secretKeys)
        .put(OSXConstants.PROCESSING_WORKBOOK_IDS, compressedEntries)
        .put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING)
        .put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdList);
  }

  protected static void loadSpreadsheetData() {
		String compressedFileName = "D:/git/cerberus-v1/cerberus/src/main/resources/repo/data_repo.zip";
		Context context = null;
		try {
			File compressedFile = new File(compressedFileName);
      String[] compressedEntries = new String[]{"contact-data.xlsx", "catalog-data.xlsx"};
      Map<String, String[]> sheetIdList = CollectionsUtility.createHashMapData("catalog-data.xlsx", new String[]{"Measure Units", "inventory-items", "Catalogues"});
      
      context = initCompressedContextData(compressedFile, compressedEntries, null, sheetIdList);
			long started = System.currentTimeMillis();
			context = OfficeSuiteServicesHelper.builder().build().loadSpreadsheetFromZip(context);
			started = System.currentTimeMillis() - started;
			System.out.println("Taken: " + started);
			displayContextData(context);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected static void loadZipData(){
	  String dataFile = "data-catalog-high.xlsx";
	  try {
	    long started = System.currentTimeMillis();
	    InputStream inputStream = new FileInputStream("D:/git/heron/heron/src/main/resources/master/data-catalog-high.zip");
	    OsxBucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadZipDataFromInputStream(dataFile, inputStream);
	    started = System.currentTimeMillis()-started;
	    System.out.println("Taken: "+started);
	    displayWorkbook((OSXWorkbook)bucketContainer.get(dataFile));
	    System.out.println();
	  } catch (Exception e) {
	    e.printStackTrace();
	  }
	}

	private static void displayWorkbook(OSXWorkbook workbook){
	  for (OSXWorksheet worksheet :workbook.datasheets()){
	    System.out.println("+++++++++++++++++" + worksheet.getId() + "+++++++++++++++++");
	    for (Integer key :worksheet.getKeys()){
	      System.out.println(worksheet.getDataRow(key));
	    }
	  }
    System.out.println("+++++++++++++++++");
  }

	private static void displayDatasheet(OSXWorksheet osxWorksheet){
	  System.out.println("+++++++++++++++++");
	  for (Integer key :osxWorksheet.getKeys()){
	    System.out.println(osxWorksheet.getDataRow(key));
	  }
    System.out.println("+++++++++++++++++");
	}

	private static void displayContextData(Context context){
		DmxWorkbook workbook = null;
		for (Object key :context.keys()){
	    System.out.println("+++++++++++++++++" + key + "+++++++++++++++++");
	    workbook = (DmxWorkbook)context.get((String)key);
	    for (DmxWorksheet worksheet :workbook.values()){
		    for (Object sheetKey :worksheet.keys()){
		    	System.out.println(worksheet.get(sheetKey));
	    	}
	    }
	  }
    System.out.println("+++++++++++++++++");
  }
}
