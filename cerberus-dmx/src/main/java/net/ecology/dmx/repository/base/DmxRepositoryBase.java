/**
 * 
 */
package net.ecology.dmx.repository.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.GUUISequenceGenerator;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.service.config.IGeneralItemService;
import net.ecology.css.service.org.BusinessUnitService;
import net.ecology.dmx.helper.ResourcesStorageServiceHelper;
import net.ecology.embeddable.Phone;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.general.Catalogue;
import net.ecology.entity.general.Currency;
import net.ecology.entity.general.GeneralCatalogue;
import net.ecology.entity.general.Money;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.framework.entity.Entity;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SequenceType;
import net.ecology.global.GlobeConstants;
import net.ecology.model.Context;
import net.ecology.osx.model.DmxWorkbook;
import net.ecology.service.general.CatalogueService;

/**
 * @author ducbui
 *
 */
public abstract class DmxRepositoryBase extends ComponentRoot {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5074736014633924681L;

	public static final int IDX_BUSINESS_DIVISION_NAME = 2;
	public static final int IDX_BUSINESS_UNIT_CODE = 3;
	public static final int IDX_BUSINESS_UNIT_NAME = 4;
	public static final int IDX_GENDER = 6;
	public static final int IDX_STATUS = 7;
	public static final int IDX_JOB_CODE = 8;
	public static final int IDX_JOB_NAME = 9;
	public static final int IDX_PHONE_PRIORITY = 11;
	public static final int IDX_PHONE_OFFICE = 12;
	public static final int IDX_PHONE_HOME = 13;
	public static final int IDX_PHONE_MOBILE = 14;
	public static final int IDX_FAX = 19;
	public static final int IDX_PHONE_OTHER = 20;
	public static final int IDX_EMAIL_WORK = 21;
	public static final int IDX_EMAIL_PERSONAL = 23;
	public static final int NUMBER_OF_CATALOGUE_SUBTYPES_GENERATE = 500;
	public static final int NUMBER_TO_GENERATE = 15000;
	public static final String DEFAULT_COUNTRY = "Việt Nam";

	/*@Inject
	protected OfficeSuiteServiceProvider officeSuiteServiceProvider;

	@Inject
	protected OfficeSuiteServicesHelper officeSuiteServicesHelper;*/
	
	@Inject
	protected BusinessUnitService businessUnitService;

	@Inject
	protected ConfigurationService configurationService;
	
	@Inject
	protected CatalogueService catalogueService;

	@Inject
	protected IGeneralItemService itemService;

	@Inject
	protected ResourcesStorageServiceHelper resourcesStorageServiceHelper;

	protected Map<String, BusinessUnit> businessUnitMap = CollectionsUtility.createMap();

	protected Map<String, GeneralCatalogue> itemMap = CollectionsUtility.createMap();
	protected Map<String, GeneralCatalogue> itemNameMap = CollectionsUtility.createMap();

	protected Map<String, Catalogue> catalogueMap = CollectionsUtility.createMap();
	protected Map<String, Catalogue> catalogueNameMap = CollectionsUtility.createMap();

	protected BusinessUnit getBusinessUnit(List<?> contactDataRow) {
		if (this.businessUnitMap.containsKey(contactDataRow.get(IDX_BUSINESS_UNIT_CODE))) {
			return this.businessUnitMap.get(contactDataRow.get(IDX_BUSINESS_UNIT_CODE));
		}

		BusinessUnit businessUnit = this.businessUnitService.getObjectByCode((String)contactDataRow.get(IDX_BUSINESS_UNIT_CODE));
		if (null != businessUnit) {
			this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
			return businessUnit;
		}

		SearchParameter searchParameter = SearchParameter.builder()
				.pageable(PageRequest.of(GlobeConstants.DEFAULT_PAGE_BEGIN, GlobeConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id"))
				.build()
				.put("name", (String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME));
		Page<BusinessUnit> fetchedObjects = this.businessUnitService.getObjects(searchParameter);
		if (fetchedObjects.hasContent()) {
			businessUnit = fetchedObjects.getContent().get(0);
			this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
			return businessUnit;
		}
		
		BusinessUnit businessDivision = getBusinessDivision(contactDataRow);
		businessUnit = BusinessUnit.builder()
				.parent(businessDivision)
				.code((String)contactDataRow.get(IDX_BUSINESS_UNIT_CODE))
				.name((String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME))
				.nameLocal((String)contactDataRow.get(IDX_BUSINESS_UNIT_NAME))
				.build();

		this.businessUnitService.save(businessUnit);
		this.businessUnitMap.put(businessUnit.getCode(), businessUnit);
		return businessUnit;
	}

	protected BusinessUnit getBusinessDivision(List<?> contactDataRow) {
		if (CommonUtility.isEmpty(contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))) 
			return null;

		BusinessUnit businessDivision = null;
		for (BusinessUnit businessUnit :this.businessUnitMap.values()) {
			if (businessUnit.getName().equals(contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))) {
				return businessUnit;
			}
		}

		SearchParameter searchParameter = SearchParameter.builder()
				.pageable(PageRequest.of(GlobeConstants.DEFAULT_PAGE_BEGIN, GlobeConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id"))
				.build()
				.put("name", (String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME));
		Page<BusinessUnit> fetchedObjects = this.businessUnitService.getObjects(searchParameter);
		if (fetchedObjects.hasContent()) {
			businessDivision = fetchedObjects.getContent().get(0);
			this.businessUnitMap.put(businessDivision.getCode(), businessDivision);
			return businessDivision;
		}

		String guuId = GUUISequenceGenerator.getInstance().nextGUUIdString(SequenceType.BUSINESS_DIVISION.getType());
		businessDivision = BusinessUnit.builder()
				.code(guuId)
				.name((String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))
				.nameLocal((String)contactDataRow.get(IDX_BUSINESS_DIVISION_NAME))
				.build();
		this.businessUnitService.save(businessDivision);
		this.businessUnitMap.put(businessDivision.getCode(), businessDivision);
		return businessDivision;
	}

	protected Catalogue parseJobInfo(List<?> contactDataRow) {
		return unmarshallCatalogue((String)contactDataRow.get(IDX_JOB_CODE), (String)contactDataRow.get(IDX_JOB_NAME), null, null);
	}

	protected GeneralCatalogue unmarshallItem(String code, String name, String nameExtend, String subtype) {
		if (CommonUtility.isEmpty(code) || CommonUtility.isEmpty(name))
			return null;

		if (CommonUtility.isNotEmpty(code) && itemMap.containsKey(code))
			return itemMap.get(code);

		GeneralCatalogue fetchedObject = this.itemService.getObjectByCode(code);
		if (null != fetchedObject) {
			this.itemMap.put(fetchedObject.getCode(), fetchedObject);
			this.itemNameMap.put(fetchedObject.getName(), fetchedObject);
			return fetchedObject;
		}

		if (CommonUtility.isNotEmpty(name) && itemNameMap.containsKey(name))
			return itemNameMap.get(code);

		fetchedObject = this.itemService.getByName(name);
		if (null != fetchedObject) {
			this.itemMap.put(fetchedObject.getCode(), fetchedObject);
			this.itemNameMap.put(fetchedObject.getName(), fetchedObject);
			return fetchedObject;
		}

		fetchedObject = GeneralCatalogue.builder()
				.code(code)
				.name(name)
				//.nameLocal(nameExtend)
				.subtype(subtype)
				.build();
		this.itemService.save(fetchedObject);
		this.itemMap.put(fetchedObject.getCode(), fetchedObject);
		this.itemNameMap.put(fetchedObject.getName(), fetchedObject);
		return fetchedObject;
	}

	protected Catalogue unmarshallCatalogue(String code, String name, String nameExtend, String subtype) {
		if (CommonUtility.isEmpty(code) || CommonUtility.isEmpty(name))
			return null;

		if (CommonUtility.isNotEmpty(code) && catalogueMap.containsKey(code))
			return catalogueMap.get(code);

		Catalogue fetchedObject = this.catalogueService.getByCode(code).orElse(null);
		if (null != fetchedObject) {
			this.catalogueMap.put(fetchedObject.getCode(), fetchedObject);
			this.catalogueNameMap.put(fetchedObject.getName(), fetchedObject);
			return fetchedObject;
		}

		if (CommonUtility.isNotEmpty(name) && catalogueNameMap.containsKey(name))
			return catalogueNameMap.get(code);

		fetchedObject = this.catalogueService.getByName(name).orElse(null);
		if (null != fetchedObject) {
			this.catalogueMap.put(fetchedObject.getCode(), fetchedObject);
			this.catalogueNameMap.put(fetchedObject.getName(), fetchedObject);
			return fetchedObject;
		}

		fetchedObject = Catalogue.builder()
				.code(code)
				.name(name)
				//.nameLocal(nameExtend)
				.subtype(subtype)
				.build();
		this.catalogueService.save(fetchedObject);
		this.catalogueMap.put(fetchedObject.getCode(), fetchedObject);
		this.catalogueNameMap.put(fetchedObject.getName(), fetchedObject);
		return fetchedObject;
	}

	protected Phone parsePhone(String countryCode, String areaCode, String number, String extention) {
		Phone phone = new Phone();
		phone.setCountryCode(countryCode);
		phone.setAreaCode(areaCode);
		phone.setExtention(extention);
		phone.setNumber(number);
		return phone;
	}

	protected Money parseMoney(Currency currency, BigDecimal value) {
		Money parsedObject = new Money();
		parsedObject.setCurrency(currency);
		parsedObject.setValue(value);
		return parsedObject;
	}

	public Context unmarshallBusinessObjects(Context executionContext) throws CerberusException {
		return doUnmarshallBusinessObjects(executionContext);
	}

	public List<Entity> unmarshallBusinessObjects(DmxWorkbook dataWorkbook, List<String> datasheetIds) throws CerberusException {
		return doUnmarshallBusinessObjects(dataWorkbook, datasheetIds);
	}

	public Entity unmarshallBusinessObject(List<?> marshallingDataRow) throws CerberusException {
		return doUnmarshallBusinessObject(marshallingDataRow);
	}

	protected List<Entity> doUnmarshallBusinessObjects(DmxWorkbook dataWorkbook, List<String> datasheetIds) throws CerberusException {
		throw new CerberusException("Not implemented yet");
	}

	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws CerberusException {
		throw new CerberusException("Not implemented yet");
	}

	protected Context doUnmarshallBusinessObjects(Context executionContext) throws CerberusException {
		throw new CerberusException("Not implemented yet");
	}
}
