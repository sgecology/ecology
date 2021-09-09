
package net.ecology.entity.emx;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.ecology.framework.validation.StrictlyPositiveNumber;
import net.ecology.global.GlobeConstants;

/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */

@Entity
@Table(name = "enterprise_tax")
@NamedQueries({
    @NamedQuery(name = "Tax.findByType", query = "SELECT t FROM EnterpriseTax t WHERE t.typeTaxUse = :typeTaxUse"),
    @NamedQuery(name = "Tax.findAll", query = "SELECT t FROM EnterpriseTax t"),
    @NamedQuery(name = "Tax.findById", query = "SELECT t FROM EnterpriseTax t WHERE t.id = :id"),
    @NamedQuery(name = "Tax.findByName", query = "SELECT t FROM EnterpriseTax t WHERE t.name = :name"),
    @NamedQuery(name = "Tax.findByAmount", query = "SELECT t FROM EnterpriseTax t WHERE t.amount = :amount"),
    @NamedQuery(name = "Tax.findByActive", query = "SELECT t FROM EnterpriseTax t WHERE t.active = :active")})
public class EnterpriseTax implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64, message = "{LongString}")
    @Column(name = GlobeConstants.PROP_NAME)
    private String name;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "amount")
    @StrictlyPositiveNumber(message = "{PositiveTaxAmount}")
    private Double amount;
    
    @Basic(optional = false)
    @NotNull
    @Max(value=100, message="{MaxTaxAmount}") 
    @StrictlyPositiveNumber(message = "{PositiveTaxAmount}")
    @Column(name = "percent")
    private Double percent;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40, message = "{LongString}")
    @Column(name = "type_tax_use")
    private String typeTaxUse;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;

    @OneToMany(mappedBy = "tax")
    private List<InvoiceLine> invoiceLines;

    @OneToMany(mappedBy = "tax")
    private List<PurchaseOrderLine> purchaseOrderLines;

    @OneToMany(mappedBy = "tax")
    private List<InvoiceTax> invoiceTaxes;
    
    @OneToMany(mappedBy = "tax")
    private List<SaleOrderLine> saleOrderLines;
    
    @OneToMany(mappedBy = "tax")
    private List<JournalItem> journalItems;

    public EnterpriseTax() {
    }

    public EnterpriseTax(Integer id) {
        this.id = id;
    }

    public EnterpriseTax(Integer id, String name, double amount, String typeTaxUse, Boolean active) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.typeTaxUse = typeTaxUse;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public String getTypeTaxUse() {
        return typeTaxUse;
    }

    public void setTypeTaxUse(String typeTaxUse) {
        this.typeTaxUse = typeTaxUse;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<InvoiceLine> getInvoiceLines() {
        return invoiceLines;
    }

    public void setInvoiceLines(List<InvoiceLine> invoiceLines) {
        this.invoiceLines = invoiceLines;
    }

    public List<PurchaseOrderLine> getPurchaseOrderLines() {
        return purchaseOrderLines;
    }

    public void setPurchaseOrderLines(List<PurchaseOrderLine> purchaseOrderLines) {
        this.purchaseOrderLines = purchaseOrderLines;
    }

    public List<InvoiceTax> getInvoiceTaxes() {
        return invoiceTaxes;
    }

    public void setInvoiceTaxes(List<InvoiceTax> invoiceTaxes) {
        this.invoiceTaxes = invoiceTaxes;
    }

    public List<SaleOrderLine> getSaleOrderLines() {
        return saleOrderLines;
    }

    public void setSaleOrderLines(List<SaleOrderLine> saleOrderLines) {
        this.saleOrderLines = saleOrderLines;
    }

    public List<JournalItem> getJournalItems() {
        return journalItems;
    }

    public void setJournalItems(List<JournalItem> journalItems) {
        this.journalItems = journalItems;
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
        if (!(object instanceof EnterpriseTax)) {
            return false;
        }
        EnterpriseTax other = (EnterpriseTax) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tax[ id=" + id + " ]";
    }
    
}
