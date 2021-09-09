package net.ecology.entity.emx;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import net.ecology.global.GlobeConstants;


/**
 * 
 * @author MOHAMMED BOUNAGA
 * 
 * github.com/medbounaga
 */

@Entity
@Table(name = "invoice_payment")
@NamedQueries({
    @NamedQuery(name = "InvoicePayment.findById", query = "SELECT i FROM InvoicePayment i WHERE i.id = :id")})
public class InvoicePayment implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "paid_amount")
    private Double paidAmount;
    @Size(max = 64, message = "{LongString}")
    @Column(name = GlobeConstants.PROP_NAME)
    private String name;
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "journal_entry_id", referencedColumnName = "id")
    @ManyToOne
    private JournalEntry journalEntry;    
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    @ManyToOne
    private CustomerInvoice invoice;
    

    public InvoicePayment() {
        
    }
    
    public InvoicePayment(CustomerInvoice invoice, JournalEntry journalEntry, Double paidAmount, Date date, String name) {
        this.invoice = invoice;
        this.journalEntry = journalEntry;
        this.paidAmount = paidAmount;  
        this.date = date;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public JournalEntry getJournalEntry() {
        return journalEntry;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    public CustomerInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(CustomerInvoice invoice) {
        this.invoice = invoice;
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
        if (!(object instanceof InvoicePayment)) {
            return false;
        }
        InvoicePayment other = (InvoicePayment) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InvoicePayment[ id=" + id + " ]";
    }
    
}
