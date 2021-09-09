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

package net.ecology.entity.doc;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;
import net.ecology.model.GeneralStatus;

/**
 * Base Document fields. 
 *
 * @author haky
 */
@MappedSuperclass
public abstract class DocumentBase extends RepoObject implements Document {
	private static final long serialVersionUID = 1L;

		@Column(name="SERIAL", length=10, nullable=false, unique=true)
    @NotNull
    @Size(max=10, min=1)
    private String serial;
    
    @Column(name="CODE", length=15)
    @Size(max=15)
    private String code;
    
    @Lob
    @Column(name="info", columnDefinition="TEXT")
    private String info;
    
    @Column(name="REFERENCE", length=10)
    @Size(max=10)
    private String reference;
    
    //Belgenin Düzenlenme Tarihi
    @Column(name="TXNDATE")
    @Temporal(value = TemporalType.DATE)
    private Date date;
    
    //Belgenin Düzenlenme Saati
    @Column(name="TXNTIME")
    @Temporal(value = TemporalType.TIME)
    private Date time;
    
    //Belgeyi teslim alan kisiyi tutar
    @Column(name="RECEPIENT", length=92)
    @Size(max=92)
    private String recepient;
    
  //Belgeyi teslim eden kisiyi tutar
    @Column(name="DELIVERER", length=92)
    @Size(max=92)
    private String deliverer;
    
    //Integration Section
    /**
     * Entegrasyon un yapıldığı Tarihi
     */
    @Column(name="INTEGRATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date integrationDate;

    /**
     * Entegrasyon Fiş ID si
     */
    @Column(name="INTEGRATION_DOCUMENT_ID")
    private Long integrationDocumentId;

    /**
     * Entegrasyon Fiş Seri No su
     */
    @Column(name="INTEGRATION_SERIAL", length=10)
    @Size(max=10)
    private String integrationSerial;
    
    /**
     * Entegrasyon Şablon Adı
     */
    @Column(name="INTEGRATION_TEMPLATE_NAME")
    private String integrationTemplateName;

  	@Column(name=GlobeConstants.PROP_NAME, length=20)
  	private String name;

  	@Column(name="issue_date")
  	private Date issueDate;

  	@Column(name="issue_place", length=100)
  	private String issuePlace;

  	@Column(name="expired_date")
  	private Date expiredDate;

  	@Column(name="status")
    @Enumerated(EnumType.ORDINAL)
  	private GeneralStatus status;

    public abstract DocumentType getDocumentType();

    //public abstract Long getId();

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append( getClass().getCanonicalName() )
			  .append("[id=")
			  .append( getId() )
			  .append("]");
		return result.toString();
	}

    /** Creates a new instance of DocumentBase */
    public DocumentBase() {
    
    }

    /**
     * Returns Document Serial Number
     * @return Serial Number as String
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Sets document serial number
     * @param serial Serial number
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Returns document special code. Use for reporting.
     * @return Document Code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets document code.
     * @param code Document code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Resturns document description.
     * @return Document descrition
     */
    public String getInfo() {
        return info;
    }

    /**
     * Set description
     * @param info Description
     */
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * Returns Document external reference. Usualy legal references, like Invoice Number.
     * @return Document external reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * Sets reference number. Usualy legal references, like Invoice Number.
     * @param reference Document external reference number.
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Returns document date. Transaction date.
     * @return Document date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets document date.
     * @param date document date
     */
    public void setDate(Date date) {
        this.date = date;
    }

	public Date getIntegrationDate() {
		return integrationDate;
	}

	public void setIntegrationDate(Date integrationDate) {
		this.integrationDate = integrationDate;
	}

	public Long getIntegrationDocumentId() {
		return integrationDocumentId;
	}

	public void setIntegrationDocumentId(Long integrationDocumentId) {
		this.integrationDocumentId = integrationDocumentId;
	}

	public String getIntegrationSerial() {
		return integrationSerial;
	}

	public void setIntegrationSerial(String integrationSerial) {
		this.integrationSerial = integrationSerial;
	}

	public String getIntegrationTemplateName() {
		return integrationTemplateName;
	}

	public void setIntegrationTemplateName(String integrationTemplateName) {
		this.integrationTemplateName = integrationTemplateName;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}

	public String getRecepient() {
		return recepient;
	}

	public void setDeliverer(String deliverer) {
		this.deliverer = deliverer;
	}

	public String getDeliverer() {
		return deliverer;
	}
	//--------------------------------------------------------------------

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public String getIssuePlace() {
		return issuePlace;
	}

	public void setIssuePlace(String issuePlace) {
		this.issuePlace = issuePlace;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public GeneralStatus getStatus() {
		return status;
	}

	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

}
