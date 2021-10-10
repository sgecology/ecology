/**
 * 
 */
package net.ecology.config;

/**
 * @author ducbq
 *
 */
public enum ContactMarshallConfig {
	idxCode((short)0),
	idxFirstName((short)1),
	idxLastName((short)2),
	idxSaluation((short)3),
	idxBirthDate((short)4),
	idxBirthPlace((short)5),
	idxIdentity((short)6),
	idxIdentityIssuedDate((short)7),
	idxIdentityIssuedPlace((short)8),
	idxIdentityExpiredDate((short)9),
	idxPassport((short)10),
	idxPassportIssuedDate((short)11),
	idxPassportIssuedPlace((short)12),
	idxPassportExpiredDate((short)13),
	idxCurrentAddress((short)14),
	idxBillingAddress((short)15),
	idxShippingAddress((short)16),
	idxPermanentAddress((short)17),
	idxEmergencyPhone((short)18),
	idxPhones((short)19),
	idxEmail((short)20),
	idxGender((short)21),
	idxCountry((short)22),
	idxMaritalStatus((short)23),
	idxQualifications((short)24),
	idxForeignLanguages((short)25),
	idxExperienceLevel((short)26),
	idxExperience((short)27),
	idxDesiredProfession((short)28),
	idxInfo((short)29),
	idxEthnicity((short)30),
	idxReligion((short)31),

	IDX_DISPLAY_NAME((short)-3),
	IDX_ASSISTANT((short)-4),
	IDX_OWNER((short)-5),
	IDX_RECORD_TYPE((short)-6),
	IDX_DEPARTMENT((short)-7),
	IDX_FAX((short)-11),
	IDX_LEAD_SOURCE((short)-14),
	IDX_PARTNER_TYPE((short)-18),
	IDX_SOURCE_SYSTEM((short)-22),
	IDX_TITLE((short)-24),
	IDX_PROXY((short)-25),
	IDX_INTRODUCER((short)-26),
	IDX_ISSUE_DATE((short)-27),
	IDX_ISSUED_BY((short)-28),
	IDX_ACTIVATION_KEY((short)-29),
	IDX_RESET_KEY((short)-30),
	IDX_RESETD_ATE((short)-31),
	IDX_PORTAL_NAME((short)-32),
	IDX_PORTAL_SECRET_KEY((short)-33),
	IDX_PORTAL_ACTIVE((short)-34),
	IDX_SMS_OPT_IN((short)-35),
	IDX_BUSINESS_UNIT((short)-36),
	IDX_REFERAL((short)-37),
	IDX_SYNC_CONTACT((short)-38),
	IDX_DO_NOT_CALL((short)-39),
	IDX_EMAIL_OPT_OUT((short)-40),
	IDX_JOB_INFO((short)-41),
	;

	private short index;
	public short index() {
		return index;
	}

	public void index(short index) {
		this.index = index;
	}
	private ContactMarshallConfig(short index) {
		this.index = index;
	}
}
