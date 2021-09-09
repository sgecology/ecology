/**
 * 
 */
package net.ecology.xform;

import lombok.Builder;
import net.ecology.common.CommonUtility;
import net.ecology.common.DateTimeUtility;
import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.MarshallableException;
import net.ecology.model.GenderType;
import net.ecology.xform.base.Marshallable;

/**
 * @author ducbq
 *
 */
@Builder
public class ContactMarshallable implements Marshallable<Contact, String[]> {
	public static short IDX_SALUATION = 3;
	public static short IDX_CODE = 0;
	public static short IDX_DISPLAY_NAME = -3;
	public static final short IDX_ASSISTANT = -4;
	public static final short IDX_OWNER = -5;
	public static final short IDX_RECORD_TYPE = 6;
	public static final short IDX_DEPARTMENT = 7;
	public static short IDX_FIRST_NAME = 1;
	public static short IDX_LAST_NAME = 2;
	public static short IDX_EMAIL = 20;
	public static final short IDX_FAX = 11;
	public static short IDX_PHONES = 19;
	public static short IDX_EMERGENCY_PHONE = 18;
	public static final short IDX_LEAD_SOURCE = 14;
	public static short IDX_BIRTH_DATE = 4;
	public static short IDX_BIRTH_PLACE = 5;
	public static short IDX_GENDER = 21;
	public static final short IDX_PARTNER_TYPE = 18;
	public static short IDX_CURRENT_ADDRESS = 14;
	public static short IDX_BILLING_ADDRESS = 15;
	public static short IDX_SHIPPING_ADDRESS = 16;
	public static short IDX_PERMANENT_ADDRESS = 17;
	public static short IDX_COUNTRY = 22;
	public static short IDX_MARITAL_STATUS = 23;
	public static short IDX_QUALIFICATIONS = 24;
	public static short IDX_FOREIGN_LANGUAGES = 25;
	public static short IDX_EXPERIENCE_LEVEL = 26;
	public static short IDX_EXPERIENCE = 27;
	public static short IDX_DESIRED_PROFESSION = 28;
	public static short IDX_ETHNICITY = 30;
	public static short IDX_RELIGION = 31;
	public static final short IDX_SOURCE_SYSTEM = 22;
	public static final short IDX_TITLE = 24;
	public static final short IDX_PROXY = 25;
	public static final short IDX_INTRODUCER = 26;
	public static final short IDX_ISSUE_DATE = 27;
	public static final short IDX_ISSUED_BY = 28;
	public static final short IDX_ACTIVATION_KEY = 29;
	public static final short IDX_RESET_KEY = 30;
	public static final short IDX_RESETD_ATE = 31;
	public static final short IDX_PORTAL_NAME = 32;
	public static final short IDX_PORTAL_SECRET_KEY = 33;
	public static final short IDX_PORTAL_ACTIVE = 34;
	public static final short IDX_SMS_OPT_IN = 35;
	public static final short IDX_BUSINESS_UNIT = 36;
	public static final short IDX_REFERAL = 37;
	public static final short IDX_SYNC_CONTACT = 38;
	public static final short IDX_DO_NOT_CALL = 39;
	public static final short IDX_EMAIL_OPT_OUT = 40;
	public static final short IDX_JOB_INFO = 41;
	public static short IDX_INFO = 29;
	public static short IDX_IDENTITY = 6;
	public static short IDX_IDENTITY_ISSUED_DATE = 7;
	public static short IDX_IDENTITY_ISSUED_PLACE = 8;
	public static short IDX_IDENTITY_EXPIRED_DATE = 9;
	public static short IDX_PASSPORT = 10;
	public static short IDX_PASSPORT_ISSUED_DATE = 11;
	public static short IDX_PASSPORT_ISSUED_PLACE = 12;
	public static short IDX_PASSPORT_EXPIRED_DATE = 13;

	@Override
	public Object marshal(Contact entity) throws MarshallableException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact unmarshal(String[] data) throws MarshallableException {
		if (CommonUtility.isEmpty(data))
			return null;

		return Contact.builder()
		.saluation(data[IDX_SALUATION])
		.code(data[IDX_CODE])
		.firstName(data[IDX_FIRST_NAME])
		.lastName(data[IDX_LAST_NAME])
		.email(data[IDX_EMAIL])
		.phones(data[IDX_PHONES])
		.emergencyPhone(data[IDX_EMERGENCY_PHONE])
		.birthDate(DateTimeUtility.parseDate(data[IDX_BIRTH_DATE]))
		.birthplace(data[IDX_BIRTH_PLACE])
		.gender(GenderType.valueOf(data[IDX_GENDER]))
		.currentAddress(data[IDX_CURRENT_ADDRESS])
		.billingAddress(data[IDX_BILLING_ADDRESS])
		.shippingAddress(data[IDX_SHIPPING_ADDRESS])
		.permanentAddress(data[IDX_PERMANENT_ADDRESS])
		.country(data[IDX_COUNTRY])
		.maritalStatus(data[IDX_MARITAL_STATUS])
		.qualifications(data[IDX_QUALIFICATIONS])
		.foreignLanguages(data[IDX_FOREIGN_LANGUAGES])
		.experienceLevel(data[IDX_EXPERIENCE_LEVEL])
		.experience(data[IDX_EXPERIENCE])
		.desiredProfession(data[IDX_DESIRED_PROFESSION])
		.ethnicity(data[IDX_ETHNICITY])
		.religion(data[IDX_RELIGION])
		.info(data[IDX_INFO])
		.identity(data[IDX_IDENTITY])
		.identityIssuedDate(DateTimeUtility.parseDate(data[IDX_IDENTITY_ISSUED_DATE]))
		.identityIssuedPlace(data[IDX_IDENTITY_ISSUED_PLACE])
		.identityExpiredDate(DateTimeUtility.parseDate(data[IDX_IDENTITY_EXPIRED_DATE]))
		.passport(data[IDX_PASSPORT])
		.passportIssuedDate(DateTimeUtility.parseDate(data[IDX_PASSPORT_ISSUED_DATE]))
		.passportIssuedPlace(data[IDX_PASSPORT_ISSUED_PLACE])
		.passportExpiredDate(DateTimeUtility.parseDate(data[IDX_PASSPORT_EXPIRED_DATE]))
		.build();
	}
}
