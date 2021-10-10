/**
 * 
 */
package net.ecology.marshalling;

import lombok.Builder;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.config.ContactMarshallConfig;
import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.CerberusException;
import net.ecology.utility.GenderTypeUtility;

/**
 * @author ducbq
 *
 */
@Builder
public class ContactMarshallabler extends MarshallerBasis<Contact, String[]> {
	@Override
	public Contact unmarshal(String[] data) throws CerberusException {
		if (CommonUtility.isEmpty(data))
			return null;

		Contact result = null;
		try {
			result = Contact.builder()
			.saluation(data[ContactMarshallConfig.idxSaluation.index()])
			.code(data[ContactMarshallConfig.idxCode.index()])
			.firstName(data[ContactMarshallConfig.idxFirstName.index()])
			.lastName(data[ContactMarshallConfig.idxLastName.index()])
			.email(data[ContactMarshallConfig.idxEmail.index()])
			.phones(data[ContactMarshallConfig.idxPhones.index()])
			.emergencyPhone(data[ContactMarshallConfig.idxEmergencyPhone.index()])
			.birthDate(DateTimeUtility.parseDate(data[ContactMarshallConfig.idxBirthDate.index()]))
			.birthplace(data[ContactMarshallConfig.idxBirthPlace.index()])
			.gender(GenderTypeUtility.getGenderType(data[ContactMarshallConfig.idxGender.index()]))
			.currentAddress(data[ContactMarshallConfig.idxCurrentAddress.index()])
			.billingAddress(data[ContactMarshallConfig.idxBillingAddress.index()])
			.shippingAddress(data[ContactMarshallConfig.idxShippingAddress.index()])
			.permanentAddress(data[ContactMarshallConfig.idxPermanentAddress.index()])
			.country(data[ContactMarshallConfig.idxCountry.index()])
			.maritalStatus(data[ContactMarshallConfig.idxMaritalStatus.index()])
			.qualifications(data[ContactMarshallConfig.idxQualifications.index()])
			.foreignLanguages(data[ContactMarshallConfig.idxForeignLanguages.index()])
			.experienceLevel(data[ContactMarshallConfig.idxExperienceLevel.index()])
			.experience(data[ContactMarshallConfig.idxExperience.index()])
			.desiredProfession(data[ContactMarshallConfig.idxDesiredProfession.index()])
			.ethnicity(data[ContactMarshallConfig.idxEthnicity.index()])
			.religion(data[ContactMarshallConfig.idxReligion.index()])
			.info(data[ContactMarshallConfig.idxInfo.index()])
			.identity(data[ContactMarshallConfig.idxIdentity.index()])
			.identityIssuedDate(DateTimeUtility.parseDate(data[ContactMarshallConfig.idxIdentityIssuedDate.index()]))
			.identityIssuedPlace(data[ContactMarshallConfig.idxIdentityIssuedPlace.index()])
			.identityExpiredDate(DateTimeUtility.parseDate(data[ContactMarshallConfig.idxIdentityExpiredDate.index()]))
			.passport(data[ContactMarshallConfig.idxPassport.index()])
			.passportIssuedDate(DateTimeUtility.parseDate(data[ContactMarshallConfig.idxPassportIssuedDate.index()]))
			.passportIssuedPlace(data[ContactMarshallConfig.idxPassportIssuedPlace.index()])
			.passportExpiredDate(DateTimeUtility.parseDate(data[ContactMarshallConfig.idxPassportExpiredDate.index()]))
			.build();
		} catch (Exception e) {
			 throw new CerberusException(e);
		}
		return result;
		//ContactMarshallConfig.idxSaluation
	}
}
