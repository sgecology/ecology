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

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Size;

import net.ecology.entity.doc.DocumentType;
import net.ecology.entity.general.MoneySet;
import net.ecology.entity.general.WorkBunch;
import net.ecology.framework.entity.RepoObject;

/**
 * Vergi kayitlarinin atilacagi entity
 * @author yigit
 */

@Entity
@Table(name="TAX_TXN")
public class TaxTxn extends RepoObject implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(name="DOCUMENT_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private DocumentType documentType;

    @Column(name="DOCUMENT_ID")
    private Long documentId;

    @Column(name="ACTION")
    @Enumerated(EnumType.ORDINAL)
    private FinanceAction action;

    @Column(name="TXN_DATE")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name="CODE", length=15)
    @Size(max=15)
    private String code;

    @Column(name="INFO")
    private String info;

    @Column(name="SERIAL")
    private String serial;

    @Column(name="REFERENCE")
    private String reference;

    @Embedded
    @Valid
    @AttributeOverrides({
        @AttributeOverride(name="value", column=@Column(name="AMT_VAL")),
        @AttributeOverride(name="currency", column=@Column(name="AMT_CCY")),
        @AttributeOverride(name="localAmount", column=@Column(name="AMT_LCYVAL"))
    })
    private MoneySet amount = new MoneySet();

    @Embedded
    @Valid
    @AttributeOverrides({
        @AttributeOverride(name="value", column=@Column(name="BASIS_VAL")),
        @AttributeOverride(name="currency", column=@Column(name="BASIS_CCY")),
        @AttributeOverride(name="localAmount", column=@Column(name="BASIS_LCYVAL"))
    })
    private MoneySet basisValue = new MoneySet();

    @Column(name="TAX_TYPE")
    @Enumerated(EnumType.ORDINAL)
    private TaxType taxType;

    /**
     * vergi oran bilgisidir.
     */
    @Column(name="RATE",precision=10,scale=2)
    private BigDecimal rate = BigDecimal.ZERO;
    
    @ManyToOne
    @JoinColumn(name="WORK_BUNCH_ID", foreignKey = @ForeignKey(name = "FK_TAXTXN_WORKBUNCHID"))
    private WorkBunch workBunch;

    /**
     * @return the documentType
     */
    public DocumentType getDocumentType() {
        return documentType;
    }

    /**
     * @param documentType the documentType to set
     */
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    /**
     * @return the documentId
     */
    public Long getDocumentId() {
        return documentId;
    }

    /**
     * @param documentId the documentId to set
     */
    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    /**
     * @return the action
     */
    public FinanceAction getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(FinanceAction action) {
        this.action = action;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * @return the amount
     */
    public MoneySet getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(MoneySet amount) {
        this.amount = amount;
    }

    /**
     * @return the basisValue
     */
    public MoneySet getBasisValue() {
        return basisValue;
    }

    /**
     * @param basisValue the basisValue to set
     */
    public void setBasisValue(MoneySet basisValue) {
        this.basisValue = basisValue;
    }

    /**
     * @return the taxType
     */
    public TaxType getTaxType() {
        return taxType;
    }

    /**
     * @param taxType the taxType to set
     */
    public void setTaxType(TaxType taxType) {
        this.taxType = taxType;
    }

	@Override
    public String toString() {
        return "TaxTxn[id=" + getId() + "]";
    }

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public WorkBunch getWorkBunch() {
		return workBunch;
	}

	public void setWorkBunch(WorkBunch workBunch) {
		this.workBunch = workBunch;
	}

}
