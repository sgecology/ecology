package net.ecology.entity.general;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.entity.auth.UserPrincipal;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.entity.contact.Contact;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobalConstants;

/**
 * An office or business unit.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_unit_profile")
@EqualsAndHashCode(callSuper=false)
public class BusinessUnitProfile extends RepoObject {
	private static final long serialVersionUID = 4620056154967660900L;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "business_unit_id")
	private BusinessUnit businessUnit;

	@Setter @Getter
	@Column(name = "issued_date")
	private Date issuedDate;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "issuer_id")
	private UserPrincipal issuer;

	@Setter @Getter
	@Column(name = "issue_number", length=GlobalConstants.SIZE_STRING_TINY)
	private String issueNumber;

	@Setter @Getter
	@Column(name = "effective_date")
	private Date effectiveDate;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "approve_user_id")
	private UserPrincipal approver;

	@Setter @Getter
	@Column(name = "expiration_policy", length=GlobalConstants.SIZE_STRING_TINY)
	private String expirationPolicy;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "contact_id")
	private Contact contact;

	@ManyToOne
	@JoinColumn(name = "accountable_contact_id")
	private Contact accountableContact;

	@ManyToOne
	@JoinColumn(name = "responsible_contact_id")
	private Contact responsibleContact;

	@Column(name = "info", columnDefinition = "TEXT")
	private String info;
}