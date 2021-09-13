package net.ecology.controller.admin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.extern.slf4j.Slf4j;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.dmx.manager.GlobalDmxManager;
import net.ecology.exceptions.CerberusException;
import net.ecology.global.GlobeConstants;
import net.ecology.model.Context;
import net.ecology.model.XWorkbook;
import net.ecology.model.XWorksheet;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.OsxBucketContainer;
import net.ecology.osx.helper.OfficeSuiteServiceProvider;
import net.ecology.osx.helper.OfficeSuiteServicesHelper;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author ducbq
 */
@Slf4j
@Named
@ViewScoped
public class AdminDataAccordion implements Serializable {
  private static final long serialVersionUID = 1474083820747048334L;

  @Inject
  private ResourceLoader resourceLoader;

  @Inject
  private GlobalDmxManager globalDmxManager;

  private List<String> processingSheetIds = CollectionsUtility.newList();

  @PostConstruct
  public void initDataModel() {
    processingSheetIds.add("inventory-items");
    processingSheetIds.add("business-units");
  }

  public void onLoadMasterDataFromCompressed() {
    log.info("On loading master data.");
    Context context = Context.builder().build()
        .put(OSXConstants.RESOURCE_NAME, GlobeConstants.APP_DEFAULT_CATALOUE_DATA)
        .put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING)
        ;
    try {
      OsxBucketContainer bucketContainer = this.globalDmxManager.marshallArchivedOfficeData(context);
      System.out.println(bucketContainer.hashCode());
    } catch (CerberusException e1) {
      e1.printStackTrace();
    }
    /*
    final String masterDataDirectory = "classpath:/META-INF/";
    final String masterDataFile = "data-catalog-high";
    try {
      //loadDataFromCompressedZip(masterDataDirectory + "data-catalog.zip");
      loadDataFromCompressedZip(
          masterDataDirectory + masterDataFile + ".zip", 
          null, 
          new String[] {masterDataFile+".xlsx"},  
          CollectionsUtility.createMap(),
          CollectionsUtility.newHashedMap(masterDataFile, CollectionsUtility.createDataList("contacts", "contacts-ext", "saleman", "inventory-items", "business-units")));
    } catch (CerberusException e) {
      log.error(e.getMessage(), e);
    }
    */
  }

  public void onLoadMasterData() {
    log.info("On loading master data.");
    try {
    	loadContactsFromSpreadsheet();
      //loadDataFromExcel();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  protected void loadDataFromExcel() throws CerberusException{
    Context context = Context.builder().build();
    InputStream inputStream = null;
    try {
      Resource resource = this.resourceLoader.getResource("classpath:/master/data-catalog.xlsx");
      if (null==resource){
        log.error("Unable to get resource from path: {}", "/master/data-catalog.xlsx");
        return;
      }

      inputStream = resource.getInputStream();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }

    if (null==inputStream){
      log.error("The input stream was empty, please check the data in the configured path!");
      return;
    }

    context.put(OSXConstants.INPUT_STREAM, inputStream);
    context.put(OSXConstants.PROCESSING_WORKSHEET_IDS, processingSheetIds);
    context.put(OSXConstants.STARTED_ROW_INDEX, new Integer[] {1, 1, 1});
    XWorkbook workbook = null;//OfficeSuiteServiceProvider.builder().build().readExcelFile(context);
    //processLoadedData(workbook);
  }
  /*
    String dataFile = "data-catalog-high.xlsx";
    try {
      long started = System.currentTimeMillis();
      InputStream inputStream = new FileInputStream("D:/git/heron/heron/src/main/resources/master/data-catalog-high.zip");
      OsxBucketContainer bucketContainer = OfficeSuiteServicesHelper.builder().build().loadZipDataFromInputStream(dataFile, inputStream);
      started = System.currentTimeMillis()-started;
      System.out.println("Taken: "+started);
      displayWorkbook((XWorkbook)bucketContainer.get(dataFile));
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace();
    }
  */

  protected void loadSpreadsheetData() {
		String compressedFileName = "D:/git/cerberus-v1/cerberus/src/main/resources/repo/data_repo.zip";
		Context context = null;
		try {
			File compressedFile = new File(compressedFileName);
      String[] compressedEntries = new String[]{"contact-data.xlsx", "catalog-data.xlsx"};
      Map<String, String[]> sheetIdList = CollectionsUtility.newHashedMap("catalog-data.xlsx", new String[]{"Measure Units", "inventory-items", "Catalogues"});
      
      context = initCompressedContextData(compressedFile, compressedEntries, null, sheetIdList);
			long started = System.currentTimeMillis();
			context = OfficeSuiteServicesHelper.builder().build().loadSpreadsheetFromZip(context);
			started = System.currentTimeMillis() - started;
			System.out.println("Taken: " + started);
			displayContactData(context);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  public void loadContactsFromSpreadsheet() {
		Context context = null;
		Resource resource = null;
		String resourceRepo = "classpath:/repo/data_repo.zip";
		try {
      resource = this.resourceLoader.getResource(resourceRepo);
      if (null==resource || !resource.isFile()){
      	log.info("Invalid resource: " + resourceRepo);
      	return;
      }

      String[] compressedEntries = new String[]{"contact-data.xlsx", "catalog-data.xlsx"};
      Map<String, String[]> sheetIdList = CollectionsUtility.newHashedMap("catalog-data.xlsx", new String[]{"Measure Units", "inventory-items", "Catalogues"});

      context = initCompressedContextData(resource.getFile(), compressedEntries, null, sheetIdList);
			long started = System.currentTimeMillis();
			context = OfficeSuiteServicesHelper.builder().build().loadSpreadsheetFromZip(context);
			started = System.currentTimeMillis() - started;
			System.out.println("Taken: " + started);
			displayContactData(context);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

  private void displayContactData(Context context){
		XWorkbook workbook = null;
		for (Object key :context.keys()){
	    System.out.println("+++++++++++++++++" + key + "+++++++++++++++++");
	    workbook = (XWorkbook)context.get((String)key);
	    for (XWorksheet worksheet :workbook.values()){
		    for (Object sheetKey :worksheet.keys()){
		    	System.out.println(worksheet.get(sheetKey));
	    	}
	    }
	  }
    System.out.println("+++++++++++++++++");
  }

	protected Context initCompressedContextData(
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
  
  protected void loadDataFromCompressedZip(
      String compressedZipFile, 
      String compressedZipFileSecretKey, 
      String[] processingDataFileEntries,  
      Map<String, String> secretKeys,
      Map<String, List<String>> sheetIdList) throws CerberusException{
    Context context = Context.builder().build();
    InputStream compressedZipInputStream = null;
    long duration = System.currentTimeMillis();
    try {
      Resource resource = this.resourceLoader.getResource(compressedZipFile);
      if (null==resource){
        log.error("Unable to get resource from path: {}", compressedZipFile);
        return;
      }

      compressedZipInputStream = resource.getInputStream();
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }

    if (null==compressedZipInputStream){
      log.error("The input stream was empty, please check the data in the configured path!");
      return;
    }

    //String originalFileName = compressedZipFile.substring(compressedZipFile.lastIndexOf("/")+1, compressedZipFile.lastIndexOf("."));
    context = Context.builder().build()
        .put(OSXConstants.DATA_FILE, CommonUtility.createFileFromInputStream(compressedZipFile, compressedZipInputStream))
        .put(OSXConstants.ENCRYPTED_KEYS, secretKeys)
        .put(OSXConstants.PROCESSING_WORKBOOK_IDS, CollectionsUtility.arraysAsList(processingDataFileEntries))
        .put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING)
        .put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdList);

    OsxBucketContainer bucketContainer = OfficeSuiteServiceProvider
    .builder()
    .build()
    .loadSpreadsheetInZip(context);
    duration = System.currentTimeMillis()-duration;
    log.info("Loading taken: " + duration + " miliseconds!");
    processLoadedData(bucketContainer);
  }

  protected void processLoadedData(OsxBucketContainer bucketContainer){
    if (null==bucketContainer)
      return;

    for (XWorkbook workbook :bucketContainer.getValues()){
      log.info("+++++++++++++++++");
      for (XWorksheet worksheet :workbook.values()){
        log.info("Sheet [" + worksheet.getId() + "]: " + worksheet.values().size());
        /*
        for (Integer key :worksheet.getKeys()){
          System.out.println(worksheet.getDataRow(key));
        }
        */
      }
    }
    log.info("+++++++++++++++++");
  }
}
