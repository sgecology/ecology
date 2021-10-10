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
import net.ecology.css.service.org.BusinessUnitService;
import net.ecology.dmx.helper.DmxCollaborator;
import net.ecology.dmx.helper.DmxConfigurationHelper;
import net.ecology.dmx.repository.base.DmxRepositoryBase;
import net.ecology.domain.Context;
import net.ecology.domain.model.ConfigureUnmarshallObjects;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.config.Configuration;
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
public class BusinessUnitDataManager extends DmxRepositoryBase {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8410155453232492561L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private BusinessUnitService businessService;
	
	@Inject 
	private DmxConfigurationHelper dmxConfigurationHelper;

	private Map<String, Byte> configDetailIndexMap = CollectionsUtility.newMap();

	private Map<String, BusinessUnit> businessObjectsMap = CollectionsUtility.newMap();

	@Override
	protected Context doUnmarshallBusinessObjects(Context executionContext) throws CerberusException {
		XWorkbook dataWorkbook = null;
		XContainer osxBucketContainer = (XContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new CerberusException("There is no business unit data in OSX container!");

		String workingDatabookId = dmxCollaborator.getConfiguredDataCatalogueWorkbookId();
		if (osxBucketContainer.containsKey(workingDatabookId)){
			dataWorkbook = (XWorkbook)osxBucketContainer.get(workingDatabookId);
		}

		unmarshallBusinessObjects(dataWorkbook, CollectionsUtility.newList(ConfigureUnmarshallObjects.BUSINESS_UNITS.getDataSheetId()));
		/*
		List<Entity> marshalledObjects = 
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				businessService.saveOrUpdate((BusinessUnit)entityBase);
			}
		}*/
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(XWorkbook dataWorkbook, List<String> datasheetIds) throws CerberusException {
		Map<String, Configuration> configDetailMap = null;
		if (CommonUtility.isEmpty(configDetailIndexMap)) {
			configDetailMap = dmxConfigurationHelper.fetchInventoryItemConfig(ConfigureUnmarshallObjects.BUSINESS_UNITS.getConfiguredEntryName());
			for (String key :configDetailMap.keySet()) {
				configDetailIndexMap.put(key, Byte.valueOf(configDetailMap.get(key).getValue()));
			}
		}

		List<Entity> marshallingObjects = CollectionsUtility.newList();
		BusinessUnit unmarshalledObject = null;
		XWorksheet dataWorksheet = dataWorkbook.get(ConfigureUnmarshallObjects.BUSINESS_UNITS.getDataSheetId());
		if (CommonUtility.isNotEmpty(dataWorksheet)) {
			for (Object key :dataWorksheet.keys()) {
				try {
					unmarshalledObject = (BusinessUnit)unmarshallBusinessObject(dataWorksheet.get(key));
				} catch (CerberusException e) {
					logger.error(e);
				}

				if (null != unmarshalledObject) {
					this.businessService.saveOrUpdate(unmarshalledObject);
					marshallingObjects.add(unmarshalledObject);
					this.businessObjectsMap.put(unmarshalledObject.getCode(), unmarshalledObject);
				}
			}
		}
		return marshallingObjects;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws CerberusException {
		BusinessUnit marshalledObject = null;
		BusinessUnit parentObject = null;
		try {
			if (1 > businessService.count("code", marshallingDataRow.get(this.configDetailIndexMap.get("idxCode")))) {
				if (CommonUtility.isNotEmpty(marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")))) {
					parentObject = this.businessObjectsMap.get((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")));
					if (null==parentObject) {
						parentObject = this.businessService.getObjectByCode((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxParentCode")));
					}

					if (null != parentObject && !this.businessObjectsMap.containsKey(parentObject.getCode())) {
						this.businessObjectsMap.put(parentObject.getCode(), parentObject);
					}
				}

				marshalledObject = BusinessUnit.builder()
						.code(String.valueOf(marshallingDataRow.get(this.configDetailIndexMap.get("idxCode"))))
						.name((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxName")))
						.supportLevel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxSupportLevel")))
						.supportCategory((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxSupportCategory")))
						.managementModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxManagementModel")))
						.parent(parentObject)
						//.address((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxAddress")))
						.operatingModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxOperatingModel")))
						.activityStatus((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxActivityStatus")))
						.organizationalModel((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxOrganizationalModel")))
						.info((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxInfo")))
						.build();
				marshalledObject.setCertifcate((String)marshallingDataRow.get(this.configDetailIndexMap.get("idxLicense")));
			}
		} catch (Exception e) {
			logger.error(e);
		}

		return marshalledObject;
	}

}