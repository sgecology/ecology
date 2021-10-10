/**
 * 
 */
package net.ecology.dmx.repository;

import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.css.service.contact.ContactService;
import net.ecology.dmx.helper.DmxCollaborator;
import net.ecology.dmx.repository.base.DmxRepositoryBase;
import net.ecology.domain.Context;
import net.ecology.embeddable.Address;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.Entity;
import net.ecology.global.GlobeConstants;
import net.ecology.model.XWorkbook;
import net.ecology.model.XWorksheet;
import net.ecology.model.osx.OSXConstants;
import net.ecology.model.osx.XContainer;
import net.ecology.utility.GenderTypeUtility;

/**
 * @author ducbui
 *
 */
@Component
public class ContactRepositoryManager extends DmxRepositoryBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5362325078265755318L;

	@Inject 
	private DmxCollaborator dmxCollaborator;
	
	@Inject 
	private ContactService contactService;

	@Override
	protected Context doUnmarshallBusinessObjects(Context executionContext) throws CerberusException {
		XWorkbook dataWorkbook = null;
		XContainer osxBucketContainer = (XContainer)executionContext.get(OSXConstants.MARSHALLED_CONTAINER);
		if (CommonUtility.isEmpty(osxBucketContainer))
			throw new CerberusException("There is no data in OSX container!");

		if (osxBucketContainer.containsKey(dmxCollaborator.getConfiguredContactWorkbookId())){
			dataWorkbook = (XWorkbook)osxBucketContainer.get(dmxCollaborator.getConfiguredContactWorkbookId());
		}

		List<Entity> marshalledObjects = unmarshallBusinessObjects(dataWorkbook, CollectionsUtility.newList(dmxCollaborator.getConfiguredContactWorksheetIds()));
		if (CommonUtility.isNotEmpty(marshalledObjects)) {
			for (Entity entityBase :marshalledObjects) {
				contactService.save((Contact)entityBase);
			}
		}
		return executionContext;
	}

	@Override
	protected List<Entity> doUnmarshallBusinessObjects(XWorkbook dataWorkbook, List<String> datasheetIds) throws CerberusException {
		List<Entity> results = CollectionsUtility.newList();
		Contact currentContact = null;
		if (null != datasheetIds) {
			for (XWorksheet dataWorksheet :dataWorkbook.values()) {
				if (!datasheetIds.contains(dataWorksheet.getId()))
					continue;

				System.out.println("Processing sheet: " + dataWorksheet.getId());
				for (Object key :dataWorksheet.keys()) {
					try {
						currentContact = (Contact)unmarshallBusinessObject(dataWorksheet.get(key));
					} catch (CerberusException e) {
						e.printStackTrace();
					}
					if (null != currentContact) {
						results.add(currentContact);
					}
				}
			}
		} else {
			for (XWorksheet dataWorksheet :dataWorkbook.values()) {
				System.out.println("Processing sheet: " + dataWorksheet.getId());
				for (Object key :dataWorksheet.keys()) {
					try {
						currentContact = (Contact)unmarshallBusinessObject(dataWorksheet.get(key));
					} catch (CerberusException e) {
						e.printStackTrace();
					}
					results.add(currentContact);
				}
			}
		}
		return results;
	}

	@Override
	protected Entity doUnmarshallBusinessObject(List<?> marshallingDataRow) throws CerberusException {
		String firstName = "";
		String lastName = "";
		String fullName = (String)marshallingDataRow.get(1);
		int lastStringSpacePos = fullName.lastIndexOf(GlobeConstants.STRING_SPACE);
		if (lastStringSpacePos != -1) {
			firstName = fullName.substring(lastStringSpacePos+1);
			lastName = fullName.substring(0, lastStringSpacePos);
		} else {
			firstName = fullName;
		}

		Contact marshalledObject = null;
		try {
			marshalledObject = Contact.builder()
					.code((String)marshallingDataRow.get(0))
					.firstName(firstName)
					.lastName(lastName)
					.birthDate((Date)marshallingDataRow.get(5))
					.email((String)marshallingDataRow.get(IDX_EMAIL_WORK))
					.businessUnit(getBusinessUnit(marshallingDataRow))
					.jobInfo(this.parseJobInfo(marshallingDataRow))
					.gender(GenderTypeUtility.getGenderType((String)marshallingDataRow.get(IDX_GENDER)))
					.phones((String)marshallingDataRow.get(IDX_PHONE_OFFICE) + ";" + (String)marshallingDataRow.get(IDX_PHONE_HOME))
					.emergencyPhone((String)marshallingDataRow.get(IDX_PHONE_HOME))
					//.phone(this.parsePhone(GlobeConstants.STRING_BLANK, GlobeConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_OFFICE), GlobeConstants.STRING_BLANK))
					//.mobilePhone(this.parsePhone(GlobeConstants.STRING_BLANK, GlobeConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_MOBILE), GlobeConstants.STRING_BLANK))
//					.homePhone(this.parsePhone(GlobeConstants.STRING_BLANK, GlobeConstants.STRING_BLANK, (String)marshallingDataRow.get(IDX_PHONE_HOME), GlobeConstants.STRING_BLANK))
					.fax((String)marshallingDataRow.get(IDX_FAX))
					.build();
		} catch (Exception e) {
			logger.error(e);
		}

		return marshalledObject;
	}
	
	public Address[] buildAddresses() {
		List<Address> addresses = CollectionsUtility.newList();
		Random randomGenerator = new Random();
		Faker faker = new Faker();
		for (int i = 0; i < NUMBER_TO_GENERATE; ++i) {
			addresses.add(
					Address.builder()
					.country(DEFAULT_COUNTRY)
					.city(DmxRepositoryConstants.cities[randomGenerator.nextInt(DmxRepositoryConstants.cities.length)])
					.address(faker.address().fullAddress())
					.state(faker.address().cityName())
					.postalCode(faker.address().zipCode()).build());
		}
		return addresses.toArray(new Address[0]);
	}

	public List<BusinessUnit> generateFakeOfficeData(){
		List<BusinessUnit> results = CollectionsUtility.newList();
		BusinessUnit currentObject = null;
		Faker faker = new Faker();
		Address[] addresses = this.buildAddresses();
		for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
			try {
				currentObject = BusinessUnit.builder()
						.code(faker.code().ean13())
						.name(CommonUtility.stringTruncate(faker.company().name(), 200))
						.phones(faker.phoneNumber().phoneNumber())
						.info(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
						.address(addresses[i])
						.build();
				results.add(currentObject);
				//bizServiceManager.saveOrUpdate(currentObject);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return results;
	}

	public List<Contact> generateFakeContactProfiles(){
		List<Contact> results = CollectionsUtility.newList();
		Contact currentObject = null;
		Faker faker = new Faker();
		for (int i = 0; i < NUMBER_TO_GENERATE; i++) {
			try {
				currentObject = Contact.builder()
						.code(faker.code().ean13())
						.firstName(CommonUtility.stringTruncate(faker.name().firstName(), 50))
						.lastName(CommonUtility.stringTruncate(faker.name().lastName(), 150))
						.code(CommonUtility.stringTruncate(faker.code().ean13(), 200))
						.info(faker.company().industry() + "\n" + faker.commerce().department() + "\n" + faker.company().profession())
						.birthDate(faker.date().birthday())
						.build();
				currentObject.setId(i+28192L);
				results.add(currentObject);
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		return results;
	}	
}
