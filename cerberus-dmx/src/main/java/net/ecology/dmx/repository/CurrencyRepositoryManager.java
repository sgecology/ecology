/**
 * 
 */
package net.ecology.dmx.repository;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.general.MeasureUnitService;
import net.ecology.dmx.helper.DmxCollaborator;
import net.ecology.dmx.helper.DmxConfigurationHelper;
import net.ecology.dmx.repository.base.DmxRepositoryBase;
import net.ecology.domain.Context;
import net.ecology.domain.model.ConfigureMarshallObjects;
import net.ecology.domain.model.MarshallingObjects;
import net.ecology.entity.config.Configuration;
import net.ecology.entity.general.MeasureUnit;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.Entity;
import net.ecology.model.XWorkbook;
import net.ecology.model.XWorksheet;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.XContainer;

/**
 * @author ducbui
 *
 */
@Component
public class CurrencyRepositoryManager extends DmxRepositoryBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4238256116048008136L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private MeasureUnitService measureUnitService;
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = CollectionsUtility.newMap();

	@Override
	protected Context doUnmarshallBusinessObjects(Context executionContext) throws CerberusException {
		XWorkbook dataWorkbook = null;
		XContainer osxBucketContainer = (XContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new CerberusException("There is no measure unit data in OSX container!");

		String workingDatabookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
		if (osxBucketContainer.containsKey(workingDatabookId)){
			dataWorkbook = (XWorkbook)osxBucketContainer.get(workingDatabookId);
		}

		List<Entity> marshalledObjects = unmarshallBusinessObjects(dataWorkbook, CollectionsUtility.newList(MarshallingObjects.MEASURE_UNITS.getName()));
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				measureUnitService.saveOrUpdate((MeasureUnit)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(XWorkbook dataWorkbook, List<String> datasheetIds) throws CerberusException {
		Map<String, Configuration> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig(ConfigureMarshallObjects.MEASURE_UNITS.getConfigName());
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> marshallingObjects = CollectionsUtility.newList();
		MeasureUnit marshallingObject = null;
		XWorksheet dataWorksheet = dataWorkbook.get(ConfigureMarshallObjects.MEASURE_UNITS.getName());
		if (CommonUtility.isNotEmpty(dataWorksheet)) {
			for (Object  key :dataWorksheet.keys()) {
				try {
					marshallingObject = (MeasureUnit)unmarshallBusinessObject(dataWorksheet.get(key));
				} catch (CerberusException e) {
					logger.error(e);
				}
				if (null != marshallingObject) {
					marshallingObjects.add(marshallingObject);
				}
			}
		}
		return marshallingObjects;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws CerberusException {
		MeasureUnit marshalledObject = null;
		try {
			if (1 > measureUnitService.count("code", marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))) {
				marshalledObject = MeasureUnit.builder()
						.code((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))
						.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
						.nameLocal((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxNameLocal")))
						.info((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxInfo")))
						.build();
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return marshalledObject;
	}

}