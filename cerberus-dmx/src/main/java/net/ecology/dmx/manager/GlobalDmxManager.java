/**
 * 
 */
package net.ecology.dmx.manager;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.FileIOUtility;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.dmx.helper.ResourcesStorageServiceHelper;
import net.ecology.dmx.repository.BusinessUnitDataManager;
import net.ecology.dmx.repository.ContactRepositoryManager;
import net.ecology.dmx.repository.InventoryItemRepositoryManager;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.framework.entity.Entity;
import net.ecology.global.GlobeConstants;
import net.ecology.globe.GlobalMarshallingRepository;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.OSXWorkbook;
import net.ecology.model.osx.OsxBucketContainer;
import net.ecology.osx.helper.OfficeSuiteServiceProvider;
import net.ecology.osx.model.ConfigureUnmarshallObjects;
import net.ecology.osx.model.DmxWorkbook;
import net.ecology.osx.model.MarshallingObjects;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author ducbui
 *
 */
@Component
public class GlobalDmxManager extends ComponentRoot {
	private static final long serialVersionUID = -759495846609992244L;

	public static final int NUMBER_OF_CATALOGUE_SUBTYPES_GENERATE = 500;
	public static final int NUMBER_TO_GENERATE = 15000;
	public static final String DEFAULT_COUNTRY = "Việt Nam";

	@Inject
	private InventoryItemRepositoryManager itemDmxRepository;

	@Inject
	private ContactRepositoryManager contactDmxRepository;

	@Inject
	protected AttachmentService attachmentService;

	@Inject
	protected ResourcesStorageServiceHelper resourcesStorageServiceHelper;

	@Inject
	protected ConfigurationService configurationService;
	
	/*@Inject
	protected OfficeSuiteServiceProvider officeSuiteServiceProvider;*/

	@Inject
	protected BusinessUnitDataManager businessUnitDataManager;
	
  /*@Inject
  private ResourceLoader resourceLoader;*/

  @Inject
  private GlobalMarshallingRepository globalMarshallingRepository;

	@SuppressWarnings("unchecked")
	public Context marshallData(Context context) throws CerberusException {
		List<String> databookIdList = null;
		Map<String, List<String>> datasheetIdMap = null;
		String archivedResourceName = null;
		List<String> marshallingObjects = null;
		try {
			if (!context.containsKey(OSXConstants.MARSHALLING_OBJECTS))
				return context;

			databookIdList = (List<String>)context.get(OSXConstants.PROCESSING_DATABOOK_IDS);
			datasheetIdMap = (Map<String, List<String>>)context.get(OSXConstants.MAPPING_DATABOOKS_DATASHEETS);

			if (Boolean.TRUE.equals(context.get(OSXConstants.FROM_ATTACHMENT))) {
				archivedResourceName = (String)context.get(OSXConstants.RESOURCE_NAME);
				this.marshallDataFromArchivedInAttachment(archivedResourceName, databookIdList, datasheetIdMap);
			} else {
				marshallDataFromArchived(context);
			}

			marshallingObjects = (List<String>)context.get(OSXConstants.MARSHALLING_OBJECTS);
			if (null == context.get(OSXConstants.MARSHALLED_CONTAINER))
				return context;

			if (marshallingObjects.contains(ConfigureUnmarshallObjects.BUSINESS_UNITS.getDataSheetId())){
				//Should be a thread
				businessUnitDataManager.unmarshallBusinessObjects(context);
			}
			
			if (marshallingObjects.contains(MarshallingObjects.INVENTORY_ITEMS.getName()) || marshallingObjects.contains(MarshallingObjects.MEASURE_UNITS.getName())){
				//Should be a thread
				itemDmxRepository.unmarshallBusinessObjects(context);
			}

			if (marshallingObjects.contains(MarshallingObjects.CONTACTS.name())){
				//Should be a thread
				contactDmxRepository.unmarshallBusinessObjects(context);
			}
		} catch (Exception e) {
			 throw new CerberusException (e);
		}
		return context;
	}

	/**
	 * Archive resource data to database unit
	 */
	public void archive(final String archivedFileName, final IOContainer ioContainer, final String encryptionKey) throws CerberusException {
		Attachment attachment = null;
		Optional<Attachment> optAttachment = null;
    logger.info("Enter GlobalDmxManager.archive(String, InputStream, String)");
		try {
			optAttachment = this.attachmentService.getByName(archivedFileName);
			if (!optAttachment.isPresent()) {
				attachment = resourcesStorageServiceHelper.buildAttachment(archivedFileName, ioContainer, encryptionKey);
				this.attachmentService.save(attachment);
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
    logger.info("Enter GlobalDmxManager.archive(String, InputStream, String)");
	}

	/**
	 * Archive resource data to database unit
	 */
	public Collection<String> archive(final IOContainer ioContainer) throws CerberusException {
    logger.info("Enter GlobalDmxManager.archive(IOContainer)");
    Collection<Attachment> attachments = this.resourcesStorageServiceHelper.buildAttachments(ioContainer);
    Collection<String> archivedAttachmentNames = CollectionsUtility.newCollection();
    for (Attachment attachment :attachments) {
			archivedAttachmentNames.add(attachment.getName());
			if (this.attachmentService.exists(GlobeConstants.PROP_NAME, attachment.getName()))
				continue;

			this.attachmentService.saveAndFlush(attachment);
    }
    logger.info("Enter GlobalDmxManager.archive(IOContainer)");
    return archivedAttachmentNames;
	}

	/**
	 * De-archive resource data to database unit
	 */
	public IOContainer unarchive(final Collection<String> archivedNames) throws CerberusException {
		IOContainer ioContainer = IOContainer.builder().build();
		logger.info("Enter GlobalDmxManager.unarchive(Collection<String>)");
		Attachment attachment = null;
		IOContainer ioContainerProcessing = null;
		for (String archivedName :archivedNames) {
			attachment = this.attachmentService.getByName(archivedName).orElse(null);
			if (null==attachment)
				continue;

			ioContainerProcessing = FileIOUtility.buildDataStream(attachment.getName(), attachment.getData());
			ioContainer.put(archivedName, ioContainerProcessing.get(archivedName));
		}
    logger.info("Enter GlobalDmxManager.archive(String, InputStream, String)");
    return ioContainer;
	}

	/**
   * Archive resource data to database unit
   */
  public Context archive(final Context context) throws CerberusException {
    String encryptionKey = null;
    logger.info("Enter GlobalDmxManager.archive(Context)");
    if (!context.containsKey(OSXConstants.RESOURCE_REPO)){
      logger.info("There is no input resource ro be archived. ");
      return context;
    }
    IOContainer ioDataContainer = this.globalMarshallingRepository.resourceAsIOContainer((String)context.get(OSXConstants.RESOURCE_REPO));
    if (null==ioDataContainer){
    	logger.error("The input stream for path: " + (String)context.get(OSXConstants.RESOURCE_REPO) + " does not valid. ");
    	return context;
    }

    if (!context.containsKey(OSXConstants.RESOURCE_NAME)){
      logger.info("There is no archive name to be processed. ");
      return context;
    }
    String archivedRepoName = (String)context.get(OSXConstants.RESOURCE_NAME);
    logger.info("Archive with resource name: {}", archivedRepoName);

    if (context.containsKey(OSXConstants.ENCRYPTED_KEY)){
      encryptionKey = (String)context.get(OSXConstants.ENCRYPTED_KEY);
    }

    this.archive(archivedRepoName, ioDataContainer, encryptionKey);
    logger.info("Leave GlobalDmxManager.archive(Context)");
    return context;
  }

  public OsxBucketContainer marshallDataFromArchivedInAttachment(String archivedName, List<String> databookIds, Map<String, List<String>> datasheetIds) throws CerberusException {
		Optional<Attachment> optAttachment = this.attachmentService.getByName(archivedName);
		if (!optAttachment.isPresent())
			return null;

		Optional<Configuration> optConfig = null;
		OsxBucketContainer osxBucketContainer = null;
		InputStream inputStream = null;
		Context defaultExecutionContext = null;
		try {
			inputStream = CommonUtility.buildInputStream(archivedName, optAttachment.get().getData());
			if (null==inputStream)
				return null;

			optConfig = configurationService.getByName(archivedName);
			if (optConfig.isPresent()) {
				defaultExecutionContext = resourcesStorageServiceHelper.syncExecutionContext(optConfig.get(), optAttachment.get().getData());
			}

			defaultExecutionContext.put(OSXConstants.PROCESSING_DATABOOK_IDS, databookIds);
			if (CommonUtility.isNotEmpty(datasheetIds)) {
				defaultExecutionContext.put(OSXConstants.PROCESSING_WORKSHEET_IDS, datasheetIds);
			}
			osxBucketContainer = OfficeSuiteServiceProvider.builder().build().extractOfficeDataFromZip(defaultExecutionContext);
		} catch (Exception e) {
			 throw new CerberusException(e);
		}
		return osxBucketContainer;
	}

  public OsxBucketContainer marshallArchivedOfficeData(Context context) throws CerberusException {
    OsxBucketContainer osxBucketContainer = null;
    InputStream inputStream = null;
    OSXWorkbook workbook = null;
    String archivedName = null;
    Optional<Attachment> optAttachment = null;
    Context executionContext = null;
    logger.info("Enter marshallArchivedOfficeData");
    try {
      archivedName = (String) context.get(OSXConstants.RESOURCE_NAME);
      optAttachment = this.attachmentService.getByName(archivedName);
      if (!optAttachment.isPresent())
        return null;

      logger.info("Start processing resource: {}", archivedName);
      executionContext = Context.builder().build().putAll(context);
      inputStream = CommonUtility.buildInputStream(archivedName, optAttachment.get().getData());
      if (null == inputStream)
        return null;

      /*
       * optConfig = configurationService.getByName(archivedName); if (optConfig.isPresent()) { defaultExecutionContext
       * = resourcesStorageServiceHelper.syncExecutionContext(optConfig.get(), optAttachment.get().getData()); }
       */
      logger.info("Get input stream");
      executionContext.put(OSXConstants.INPUT_STREAM, inputStream);
      workbook = OfficeSuiteServiceProvider.builder().build().readExcelFile(executionContext);
      if (workbook != null) {
        osxBucketContainer = OsxBucketContainer.builder().build().put(archivedName, workbook);
      }
    } catch (Exception e) {
      throw new CerberusException(e);
    }
    return osxBucketContainer;
  }

  public Context marshallDataFromArchived(Context executionContext) throws CerberusException {
		InputStream inputStream;
		OsxBucketContainer osxBucketContainer = null;
		Context workingExecutionContext = null;
		try {
			inputStream = (InputStream)executionContext.get(OSXConstants.INPUT_STREAM);
			workingExecutionContext = (Context)Context.builder().build().putAll(executionContext);
			workingExecutionContext.put(OSXConstants.MASTER_BUFFER_DATA_BYTES, FileCopyUtils.copyToByteArray(inputStream));
			workingExecutionContext.put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING);
			osxBucketContainer = OfficeSuiteServiceProvider.builder().build().extractOfficeDataFromZip(workingExecutionContext);
			executionContext.put(OSXConstants.MARSHALLED_CONTAINER, osxBucketContainer);
		} catch (Exception e) {
			 throw new CerberusException(e);
		}
		return executionContext;
	}

	public List<Entity> marshallContacts(String archivedResourceName, String dataWorkbookId, List<String> datasheetIdList) throws CerberusException {
		List<Entity> contacts = null;
		DmxWorkbook dataWorkbook = null;
		OsxBucketContainer osxBucketContainer = null;
		List<String> databookIdList = null;
		Map<String, List<String>> datasheetIdMap = null;
		try {
			databookIdList = CollectionsUtility.createDataList(dataWorkbookId);
			datasheetIdMap = CollectionsUtility.createHashMapData(dataWorkbookId, datasheetIdList);
			osxBucketContainer = this.marshallDataFromArchivedInAttachment(archivedResourceName, databookIdList, datasheetIdMap);
			if (null != osxBucketContainer && osxBucketContainer.containsKey(dataWorkbookId)){
				dataWorkbook = (DmxWorkbook)osxBucketContainer.get(dataWorkbookId);
			}

			contacts = contactDmxRepository.unmarshallBusinessObjects(dataWorkbook, datasheetIdList);
		} catch (Exception e) {
			 throw new CerberusException (e);
		}
		return contacts;
	}

	protected List<GeneralCatalogue> marshallItems(){
		List<GeneralCatalogue> marshalledList = CollectionsUtility.createDataList();
		
		return marshalledList;
	}
	/*
	public IOContainer resourceAsIOContainer(String path) throws CerberusException {
		IOContainer ioDataContainer = null;
	  try {
	    Resource resource = this.resourceLoader.getResource(path);
	    if (null==resource){
	      throw new CerberusException("Unable to get resource from path: " + path);
	    }

	    ioDataContainer = IOContainer.builder()
	    		.inStream(resource.getInputStream())
	    		.build();
    } catch (FileNotFoundException fnfe){
    	//logger.error(fnfe);
    } catch (Exception e) {
      throw new CerberusException(e);
    }
	  return ioDataContainer;
	}

	public IOContainer resourcesAsIOContainer(String path) throws CerberusException {
		IOContainer ioContainer = IOContainer.builder().build();
	  try {
	  	Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(path);
	  	for (Resource resource :resources) {
				ioContainer.getInpStreams().put(resource.getFilename(), resource.getInputStream());
	  	}
    } catch (Exception e) {
      throw new CerberusException(e);
    }
	  return ioContainer;
	}

	public Context getRepositoryResourceContext(String path) throws CerberusException {
		Context context = Context.builder().build();
	  XWorksheet worksheet = null;
	  try {
	  	Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(path);
	  	for (Resource resource :resources) {
				worksheet = getRepositoryData(resource.getFilename(), IOContainer.builder().inStream(resource.getInputStream()).build());
				if (null != worksheet){
					context.put(worksheet.getId(), worksheet);
				}
	  	}
    } catch (Exception e) {
      throw new CerberusException(e);
    }
	  return context;
	}

	@SuppressWarnings("unchecked")
	private XWorksheet getRepositoryData(Object key, IOContainer ioDataContainer) throws CerberusException {
		XWorksheet worksheet = XWorksheet.builder()
				.id((String)key)
				.build();
		DataMarshaller dataMarshaller = DataMarshallingFactory.builder().build().getMarshaller(DataMarshallProvider.DATA_PROVIDER_CSV);

		Context context = Context.builder().build()
		.put(CommonConstants.IO_DATA_CONTAINER, ioDataContainer)
		.put(CommonConstants.SEPARATOR, GlobeConstants.PIPELINE)
		.put(CommonConstants.SKIP_LINES, 0);
		Context fectchedContext = dataMarshaller.fetchData(context);
		List<String[]> fetchedData = (List<String[]>)fectchedContext.get(Context.DEFAULT);
		int idx = 0;
		for (String[] values :fetchedData){
			worksheet.put(Integer.valueOf(idx++), CollectionsUtility.arraysAsList(values));
		}
		return worksheet;
	}
	*/
}