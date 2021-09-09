/*
 * Copyleft 2007-2011 Ozgur Yazilim A.S.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 * http://www.gnu.org/licenses/lgpl.html
 *
 * www.tekir.com.tr
 * www.ozguryazilim.com.tr
 *
 */

package net.ecology.entity.trade;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.annotations.ForeignKey;

import net.ecology.entity.general.MoneySet;
import net.ecology.framework.entity.RepoObject;
import net.ecology.model.PaymentType;

/**
 * @author sinan.yumak
 *
 */
@Entity
@Table(name="PAYMENT_TABLE_DETAIL")
public class PaymentTableDetail extends RepoObject {
    
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name="OWNER_ID")
	private PaymentTable owner;
	
	@Column(name="PAYMENT_TYPE")
	@Enumerated(EnumType.ORDINAL)
	private PaymentType paymentType;
	
    //Ödeme tutar bilgileri. 
    @Embedded
    @Valid
    @AttributeOverrides( {
        @AttributeOverride(name="currency", column=@Column(name="AMOUNT_CCY")),
        @AttributeOverride(name="value",    column=@Column(name="AMOUNT_VALUE")),
        @AttributeOverride(name="localAmount", column=@Column(name="AMOUNT_LCYVAL"))
    })
    private MoneySet amount = new MoneySet();

    /**
     * eğer pos kullanılırsa, pos bilgilerini tutar.
     */
    @ManyToOne
    @JoinColumn(name="POS_ID")
    private Pos pos;

    @Column(name="CREDIT_CARD_NUMBER",length=16)
    private String creditCardNumber;
    
    /**
     * Bu satırın herhangi bir faturanın ödeme tablosuna aktarılıp 
     * aktarılmadığı bilgisini tutar.
     */
    @Column(name="PROCESSED")
    private boolean processed = false;

    /**
     * Hangi siparişten geldiği bilgisini tutar.
     */
    @Column(name="REFERENCE_ID")
    private Long referenceId;
    
    /**
     * Pos tan hangi bankanın kartı geçirildiği bilgisi tutar.
     */
    @ManyToOne
    @JoinColumn(name="BANK_ID")
    @ForeignKey(name="FK_PAYMENTTABLEDETAIL_BANKID")
    private Bank bank;

	@Column(name="PERIOD")
	private int period = 0;
    
	/**
	 * Alınan ödeme ile ilgili fatura kesilip kesilmediği bilgisini tutar.
	 * Kapora adımında fatura kesildi ise satış faturasında bakiyeden
	 * düşülmesini takip etmek için kullanılacak.
	 */
	@Column(name="INVOICED")
	private boolean invoiced = false;

    @Override
    public String toString() {
        return "PaymentTableDetail[id=" + getId() + "]";
    }

    public PaymentTableDetail clone() {
    	PaymentTableDetail clonedptd = new PaymentTableDetail();
    	clonedptd.setAmount(amount);
    	clonedptd.setBank(bank);
    	clonedptd.setCreditCardNumber(creditCardNumber);
    	clonedptd.setInvoiced(invoiced);
    	clonedptd.setPaymentType(paymentType);
    	clonedptd.setPeriod(period);
    	clonedptd.setPos(pos);
    	clonedptd.setProcessed(processed);
    	clonedptd.setReferenceId(referenceId);
    	return clonedptd;
    }
    
	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public MoneySet getAmount() {
		if (amount == null ) {
			amount = new MoneySet();
		}
		return amount;
	}

	public void setAmount(MoneySet amount) {
		this.amount = amount;
	}

	public PaymentTable getOwner() {
		return owner;
	}

	public void setOwner(PaymentTable owner) {
		this.owner = owner;
	}

	public Pos getPos() {
		return pos;
	}

	public void setPos(Pos pos) {
		this.pos = pos;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public boolean isProcessed() {
		return processed;
	}

	public void setProcessed(boolean processed) {
		this.processed = processed;
	}

	public Long getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(Long referenceId) {
		this.referenceId = referenceId;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public boolean isInvoiced() {
		return invoiced;
	}

	public void setInvoiced(boolean invoiced) {
		this.invoiced = invoiced;
	}
	
}
