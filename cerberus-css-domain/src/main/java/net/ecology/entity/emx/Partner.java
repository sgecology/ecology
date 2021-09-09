
package net.ecology.entity.emx;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import net.ecology.framework.validation.InDateRange;
import net.ecology.global.GlobeConstants;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */

@Entity
@Table(name = "partner")
@NamedQueries({
    @NamedQuery(name = "Partner.findAll", query = "SELECT p FROM Partner p"),
    @NamedQuery(name = "Partner.findById", query = "SELECT p FROM Partner p WHERE p.id = :id"),
    @NamedQuery(name = "Partner.findByName", query = "SELECT p FROM Partner p WHERE p.name = :name"),
    @NamedQuery(name = "Partner.findByCity", query = "SELECT p FROM Partner p WHERE p.city = :city"),
    @NamedQuery(name = "Partner.findByStreet", query = "SELECT p FROM Partner p WHERE p.street = :street"),
    @NamedQuery(name = "Partner.findBySupplier", query = "SELECT p FROM Partner p WHERE p.supplier = 1"),
    @NamedQuery(name = "Partner.findByActiveSupplier", query = "SELECT p FROM Partner p WHERE p.supplier = 1 AND p.active = 1"),
    @NamedQuery(name = "Partner.findByCustomer", query = "SELECT p FROM Partner p WHERE p.customer = 1"),
    @NamedQuery(name = "Partner.findByActiveCustomer", query = "SELECT p FROM Partner p WHERE p.customer = 1 AND p.active = 1"),
    @NamedQuery(name = "Partner.findByEmail", query = "SELECT p FROM Partner p WHERE p.email = :email"),
    @NamedQuery(name = "Partner.findByWebsite", query = "SELECT p FROM Partner p WHERE p.website = :website"),
    @NamedQuery(name = "Partner.findByFax", query = "SELECT p FROM Partner p WHERE p.fax = :fax"),
    @NamedQuery(name = "Partner.findByPhone", query = "SELECT p FROM Partner p WHERE p.phone = :phone"),
    @NamedQuery(name = "Partner.findByCredit", query = "SELECT p FROM Partner p WHERE p.credit = :credit"),
    @NamedQuery(name = "Partner.findByDebit", query = "SELECT p FROM Partner p WHERE p.debit = :debit"),
    @NamedQuery(name = "Partner.findByMobile", query = "SELECT p FROM Partner p WHERE p.mobile = :mobile"),
    @NamedQuery(name = "Partner.findByIsCompany", query = "SELECT p FROM Partner p WHERE p.isCompany = :isCompany"),
    @NamedQuery(name = "Partner.findByPurchaseDeals", query = "SELECT p FROM Partner p WHERE p.purchaseDeals = :purchaseDeals"),
    @NamedQuery(name = "Partner.findBySaleDeals", query = "SELECT p FROM Partner p WHERE p.saleDeals = :saleDeals"),
    @NamedQuery(name = "Partner.findByActive", query = "SELECT p FROM Partner p WHERE p.active = :active")})
public class Partner implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = GlobeConstants.PROP_NAME , nullable = false)
    @NotNull
    @Size(min = 1, max = 128, message = "{LongString}")
    private String name;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Size(max = 128, message = "{LongString}")
    @Column(name = "city")
    private String city;

    @Size(max = 128, message = "{LongString}")
    @Column(name = "street")
    private String street;

    @Column(name = "supplier")
    private Boolean supplier;

    @Column(name = "customer")
    private Boolean customer;

    @Size(max = 240, message = "{LongString}")
    @Column(name = "email")
    private String email;

    @Size(max = 64, message = "{LongString}")
    @Column(name = "website")
    private String website;

    @Size(max = 128, message = "{LongString}")
    @Column(name = "country")
    private String country;

    @Basic(optional = false)
    @NotNull
    @Column(name = "create_date")
    @InDateRange
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 64, message = "{LongString}")
    @Column(name = "fax")
    private String fax;

    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 64, message = "{LongString}")
    @Column(name = "phone")
    private String phone;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @NotNull
    @Column(name = "credit")
    private Double credit;

    @NotNull
    @Column(name = "debit")
    private Double debit;

    @Lob
    @Column(name = "image_medium")
    private byte[] imageMedium;

    @Size(max = 64, message = "{LongString}")
    @Column(name = "mobile")
    private String mobile;

    @Column(name = "is_company")
    private Boolean isCompany;

    @Column(name = "purchase_deals")
    private Integer purchaseDeals;

    @Column(name = "sale_deals")
    private Integer saleDeals;

    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;

    @JoinColumn(name = "accountReceivable_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EnterpriseAccount accountReceivable;

    @JoinColumn(name = "accountPayable_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private EnterpriseAccount accountPayable;   

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<JournalItem> journalItems;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<DeliveryOrderLine> deliveryOrderLines;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<DeliveryOrder> deliveryOrders;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<JournalEntry> journalEntries;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<InvoiceLine> invoiceLines;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PurchaseOrder> purchaseOrderList;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SaleOrder> saleOrders;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<EnterprisePayment> payments;

    @OneToMany(mappedBy = "partner")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<CustomerInvoice> invoices;
    
    

    public Partner() {
    }

    public Partner(Integer id) {
        this.id = id;
    }

    public Partner(Integer id, String name, Boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Boolean getSupplier() {
        return supplier;
    }

    public void setSupplier(Boolean supplier) {
        this.supplier = supplier;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getCredit() {
        if(credit == null){            
            credit = 0d;          
        }          
            return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

    public Double getDebit() {
        if(debit == null){            
            debit = 0d;           
        }  
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public byte[] getImageMedium() {
        return imageMedium;
    }

    public void setImageMedium(byte[] imageMedium) {
        this.imageMedium = imageMedium;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean isCompany) {
        this.isCompany = isCompany;
    }

    public Integer getPurchaseDeals() {
        return purchaseDeals;
    }

    public void setPurchaseDeals(Integer purchaseDeals) {
        this.purchaseDeals = purchaseDeals;
    }

    public Integer getSaleDeals() {
        return saleDeals;
    }

    public void setSaleDeals(Integer saleDeals) {
        this.saleDeals = saleDeals;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public EnterpriseAccount getAccountReceivable() {
        return accountReceivable;
    }

    public void setAccountReceivable(EnterpriseAccount accountReceivable) {
        this.accountReceivable = accountReceivable;
    }

    public EnterpriseAccount getAccountPayable() {
        return accountPayable;
    }

    public void setAccountPayable(EnterpriseAccount accountPayable) {
        this.accountPayable = accountPayable;
    }

    public List<JournalItem> getJournalItems() {
        return journalItems;
    }

    public void setJournalItems(List<JournalItem> journalItems) {
        this.journalItems = journalItems;
    }

    public List<DeliveryOrderLine> getDeliveryOrderLines() {
        return deliveryOrderLines;
    }

    public void setDeliveryOrderLines(List<DeliveryOrderLine> deliveryOrderLines) {
        this.deliveryOrderLines = deliveryOrderLines;
    }

    public List<DeliveryOrder> getDeliveryOrders() {
        return deliveryOrders;
    }

    public void setDeliveryOrders(List<DeliveryOrder> deliveryOrders) {
        this.deliveryOrders = deliveryOrders;
    }

    public List<JournalEntry> getJournalEntries() {
        return journalEntries;
    }

    public void setJournalEntries(List<JournalEntry> journalEntries) {
        this.journalEntries = journalEntries;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public List<PurchaseOrder> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public void setPurchaseOrderList(List<PurchaseOrder> purchaseOrderList) {
        this.purchaseOrderList = purchaseOrderList;
    }

    public List<SaleOrder> getSaleOrders() {
        return saleOrders;
    }

    public void setSaleOrders(List<SaleOrder> saleOrders) {
        this.saleOrders = saleOrders;
    }

    public List<EnterprisePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<EnterprisePayment> payments) {
        this.payments = payments;
    }

    public List<CustomerInvoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<CustomerInvoice> invoices) {
        this.invoices = invoices;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Partner)) {
            return false;
        }
        Partner other = (Partner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Partner[ id=" + id + " ]";
    }
    
}
