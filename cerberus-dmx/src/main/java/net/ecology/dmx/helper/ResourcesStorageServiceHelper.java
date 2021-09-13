/**
 * 
 */
package net.ecology.dmx.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.MimeTypes;
import net.ecology.common.SimpleEncryptionEngine;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.domain.entity.Attachment;
import net.ecology.entity.config.Configuration;
import net.ecology.exceptions.CerberusException;
import net.ecology.global.GlobeConstants;
import net.ecology.helper.ResourcesServicesHelper;
import net.ecology.model.Context;
import net.ecology.model.IOContainer;
import net.ecology.model.osx.OSXConstants;
import net.ecology.osx.model.OfficeMarshalType;

/**
 * @author ducbq
 *
 */
@Component
public class ResourcesStorageServiceHelper {
	@Inject
	private ResourcesServicesHelper resourcesServicesHelper;
	
	@Inject
	private AttachmentService attachmentService;
	
	@Inject
	private ConfigurationService configurationService;

	public Context syncExecutionContext(Configuration config, byte[] dataBytes) throws CerberusException {
		if (null == config) {
			return null;
		}

		Context executionContext = Context.builder().build();

		/*String masterFileName = config.getValue();
		Map<String, String> secretKeyMap = CollectionsUtility.createMap();
		for (ConfigurationDetail configDetail :config.getConfigurationDetails()) {
			if (OSXConstants.ENCRYPTION_KEYS.equalsIgnoreCase(configDetail.getValueExtended())) {
				secretKeyMap.put(configDetail.getName(), SimpleEncryptionEngine.decode(configDetail.getValue()));
			}
		}*/

		executionContext.put(OSXConstants.MASTER_BUFFER_DATA_BYTES, dataBytes);
		//executionContext.put(OSXConstants.PARAM_MASTER_FILE_NAME, masterFileName);
		//executionContext.put(OSXConstants.ENCRYPTION_KEYS, secretKeyMap);
		executionContext.put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING);
		return executionContext;
	}

	public Context buildDefaultDataExecutionContext() throws CerberusException {
		Context executionContext = Context.builder().build();

		String defaultContactsData = "Vietbank_14.000.xlsx", defaultCataloguesData = "data-catalog.xlsx";
		//File zipFile = resourcesServicesHelper.loadClasspathResourceFile("data/marshall/develop_data.zip");
		Map<String, String> secretKeyMap = CollectionsUtility.newHashedMap(defaultContactsData, "thanhcong");
		Map<String, List<String>> sheetIdMap = CollectionsUtility.newMap();
		sheetIdMap.put(defaultContactsData, CollectionsUtility.arraysAsList(new String[] {"File Tổng hợp", "Các trưởng phó phòng", "9"}));

		executionContext.put(OSXConstants.MASTER_BUFFER_DATA_BYTES, resourcesServicesHelper.loadClasspathResourceBytes("data/marshall/develop_data.zip"));
		executionContext.put(OSXConstants.MASTER_ARCHIVED_FILE_NAME, "data/marshall/develop_data.zip");
		executionContext.put(OSXConstants.ENCRYPTED_KEYS, secretKeyMap);
		executionContext.put(OSXConstants.PROCESSING_WORKBOOK_IDS, CollectionsUtility.arraysAsList(new String[] {defaultContactsData, defaultCataloguesData}));
		executionContext.put(OSXConstants.XLSX_MARSHALLING_DATA_METHOD, OfficeMarshalType.STREAMING);
		executionContext.put(OSXConstants.PROCESSING_WORKSHEET_IDS, sheetIdMap);
		return executionContext;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void archiveResourceData(final Context executionContextParams) throws CerberusException {
		Attachment attachment = null;
		Optional<Attachment> attachmentChecker = null;
		Configuration archivedConfig = null;
		Map<String, String> secretKeyMap = null;
		byte[] masterDataBuffer = null;
		String masterDataFileName = null;
		Optional<Configuration> archivedConfigChecker = null;
		try {
			if (!(executionContextParams.containsKey(OSXConstants.MASTER_BUFFER_DATA_BYTES) || executionContextParams.containsKey(OSXConstants.MASTER_ARCHIVED_FILE_NAME)))
				throw new CerberusException("There is no archiving file!");

			masterDataFileName = (String)executionContextParams.get(OSXConstants.MASTER_ARCHIVED_FILE_NAME);
			attachmentChecker = this.attachmentService.getByName(masterDataFileName);
			if (attachmentChecker.isPresent())
				return; //throw new EcosphereException("The archiving file is persisted already!");

			masterDataBuffer = (byte[]) executionContextParams.get(OSXConstants.MASTER_BUFFER_DATA_BYTES);
			attachment = this.buidAttachment(masterDataFileName, masterDataBuffer, (String)executionContextParams.get(OSXConstants.MASTER_FILE_ENCRYPTION_KEY));
			this.attachmentService.save(attachment);
			//Build configuration & dependencies accordingly
			archivedConfigChecker = this.configurationService.getByName(masterDataFileName);
			if (archivedConfigChecker.isPresent())
				return;

			archivedConfig = Configuration.builder()
					.group("ArchivedMasterData")
					.name(masterDataFileName)
					.value(masterDataFileName)
					.build();

			secretKeyMap = (Map)executionContextParams.get(OSXConstants.ENCRYPTED_KEYS);
			for (String key :secretKeyMap.keySet()) {
				archivedConfig.addSubordinate(Configuration.builder()
						.name(OSXConstants.ENCRYPTED_KEYS)
						.value(SimpleEncryptionEngine.encode(secretKeyMap.get(key)))
						.valueExtended(key)
						.build())
				;
			}
			this.configurationService.save(archivedConfig);
		} catch (Exception e) {
			throw new CerberusException(e);
		}
	}

	public static Attachment buidAttachment(final File file) throws CerberusException {
		Attachment attachment = null;
		int lastDot = file.getName().lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = file.getName().substring(lastDot+1);
		try {
			attachment = Attachment.builder()
					.name(file.getName())
					.data(IOUtils.toByteArray(new FileInputStream(file)))
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.build();
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return attachment;
	}

	/*public Attachment buidAttachment(final String fileName, final IOContainer ioDataContainer, String encryptionKey) throws CerberusException {
		Attachment attachment = null;
		int lastDot = fileName.lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = fileName.substring(lastDot+1);
		String procEncyptionKey = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey))
				procEncyptionKey = SimpleEncryptionEngine.encode(encryptionKey);

			attachment = Attachment.builder()
					.name(fileName)
					.data(IOUtils.toByteArray(ioDataContainer.get()))
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.encryptionKey(procEncyptionKey)
					.build();
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return attachment;
	}*/

	public Attachment buildAttachment(final String resourceName, final IOContainer ioContainer, String encryptionKey) throws CerberusException {
		Attachment attachment = null;
		int lastDot = resourceName.lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = resourceName.substring(lastDot+1);
		String procEncyptionKey = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey))
				procEncyptionKey = SimpleEncryptionEngine.encode(encryptionKey);

			attachment = Attachment.builder()
					.name(resourceName)
					.data(IOUtils.toByteArray((null==ioContainer.get(resourceName)?ioContainer.get():ioContainer.get(resourceName))))
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.encryptionKey(procEncyptionKey)
					.build();
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return attachment;
	}

	public Collection<Attachment> buildAttachments(final IOContainer ioContainer) throws CerberusException {
		int fileExtPos;
		String fileExtension = null;
		String procEncyptionKey = null;
		Collection<Attachment> attachments = CollectionsUtility.newCollection(); 
		try {
			if (CommonUtility.isEmpty(ioContainer) || ioContainer.isEmpty())
				throw new CerberusException("The IOContainer is empty or no data within it!");

			for (Object key :ioContainer.keys()) {
				procEncyptionKey = (CommonUtility.isNotEmpty(ioContainer.getSecret(key))) ? SimpleEncryptionEngine.encode(ioContainer.getSecret(key)):null;

				fileExtPos = ((String)key).lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
				fileExtension = ((String)key).substring(fileExtPos+1);

				attachments.add(Attachment.builder()
						.name((String)key)
						.data(IOUtils.toByteArray((null==ioContainer.get(key)?ioContainer.get():ioContainer.get(key))))
						.mimetype(MimeTypes.getMimeType(fileExtension))
						.encryptionKey(procEncyptionKey)
						.build()
				);
			}
		} catch (IOException e) {
			throw new CerberusException(e);
		}
		return attachments;
	}

	public Attachment buidAttachment(final String fileName, final byte[] bytes, String encryptionKey) throws CerberusException {
		Attachment attachment = null;
		int lastDot = fileName.lastIndexOf(GlobeConstants.FILE_EXTENSION_SEPARATOR);
		String fileExtension = fileName.substring(lastDot+1);
		String procEncyptionKey = null;
		try {
			if (CommonUtility.isNotEmpty(encryptionKey))
				procEncyptionKey = SimpleEncryptionEngine.encode(encryptionKey);

			attachment = Attachment.builder()
					.name(fileName)
					.data(bytes)
					.mimetype(MimeTypes.getMimeType(fileExtension))
					.encryptionKey(procEncyptionKey)
					.build();
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return attachment;
	}

	/*public static InputStream buidInputStreamFromAttachment(final Attachment attachment) throws CerberusException {
		InputStream inputStream = null;
		try {
			inputStream = CommonUtility.buildInputStream(attachment.getName(), attachment.getData());
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return inputStream;
	}*/	
}
