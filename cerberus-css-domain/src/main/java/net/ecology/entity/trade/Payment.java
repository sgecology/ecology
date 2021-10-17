package net.ecology.entity.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.entity.contact.Team;
import net.ecology.entity.crx.CRXGeneralType;
import net.ecology.entity.crx.CustomerAccount;
import net.ecology.entity.general.Currency;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;
import net.ecology.model.GeneralStatus;

/**
 * A Payment for CRX.
 */
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@Entity
@Table(name = "payment")
@EqualsAndHashCode(callSuper=false)
public class Payment extends RepoObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8271976867916463136L;

	@Column(name = GlobeConstants.PROP_NAME, nullable = false, unique=true, length=200)
	private String name;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "account_id")
	private CustomerAccount account;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "currency_id")
	private UserAccountProfile assignedTo;

	@Column(name="status")
  @Enumerated(EnumType.ORDINAL)
	private GeneralStatus status;

	@ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinTable(
			name = "payment_team", 
			inverseJoinColumns = {@JoinColumn(name = "team_id")},
			joinColumns = {@JoinColumn(name = "payment_id")}
	)
	private Set<Team> paymentTeams;

  @Column(name="payment_date")
	@DateTimeFormat(iso = ISO.DATE)
  private Date paymentDate;

	@Column(name="payment_type_id")
  @Enumerated(EnumType.ORDINAL)
	private CRXGeneralType paymentType;

	@ManyToOne(targetEntity=Currency.class, fetch=FetchType.EAGER)
	@JoinColumn(name = "currency_id", insertable=false, updatable=false)
	private Currency currency;

	@Digits(integer=12, fraction=2)
	@Column(name = "amount")
	private BigDecimal amount;

	@Digits(integer=12, fraction=2)
	@Column(name = "bank_fee")
	private BigDecimal bankFee;

	@Column(name = "customer_reference", length=120)
	private String customerReference;

	@Column(name = "info", columnDefinition="TEXT")
	private String info;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public UserAccountProfile getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(UserAccountProfile assignedTo) {
		this.assignedTo = assignedTo;
	}

	public GeneralStatus getStatus() {
		return status;
	}

	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public CustomerAccount getAccount() {
		return account;
	}

	public void setAccount(CustomerAccount account) {
		this.account = account;
	}

	public Set<Team> getPaymentTeams() {
		return paymentTeams;
	}

	public void setPaymentTeams(Set<Team> paymentTeams) {
		this.paymentTeams = paymentTeams;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public CRXGeneralType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(CRXGeneralType paymentType) {
		this.paymentType = paymentType;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBankFee() {
		return bankFee;
	}

	public void setBankFee(BigDecimal bankFee) {
		this.bankFee = bankFee;
	}

	public String getCustomerReference() {
		return customerReference;
	}

	public void setCustomerReference(String customerReference) {
		this.customerReference = customerReference;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

}
