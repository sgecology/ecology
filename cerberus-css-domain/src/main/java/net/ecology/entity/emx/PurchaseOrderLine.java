
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
import javax.persistence.Transient;
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
@Table(name = "purchase_order_line")
@NamedQueries({
    @NamedQuery(name = "PurchaseOrderLine.findByProduct", query = "SELECT p FROM PurchaseOrderLine p WHERE p.product.id = :productId"),  
    @NamedQuery(name = "PurchaseOrderLine.countByProduct", query = "SELECT SUM(p.quantity) FROM PurchaseOrderLine p WHERE p.product.id = :productId "),
    @NamedQuery(name = "PurchaseOrderLine.findAll", query = "SELECT p FROM PurchaseOrderLine p"),
    @NamedQuery(name = "PurchaseOrderLine.findById", query = "SELECT p FROM PurchaseOrderLine p WHERE p.id = :id"),
    @NamedQuery(name = "PurchaseOrderLine.findByDate", query = "SELECT p FROM PurchaseOrderLine p WHERE p.date = :date"),
    @NamedQuery(name = "PurchaseOrderLine.findByPrice", query = "SELECT p FROM PurchaseOrderLine p WHERE p.price = :price"),
    @NamedQuery(name = "PurchaseOrderLine.findByQuantity", query = "SELECT p FROM PurchaseOrderLine p WHERE p.quantity = :quantity"),
    @NamedQuery(name = "PurchaseOrderLine.findBySubTotal", query = "SELECT p FROM PurchaseOrderLine p WHERE p.subTotal = :subTotal"),
    @NamedQuery(name = "PurchaseOrderLine.findByActive", query = "SELECT p FROM PurchaseOrderLine p WHERE p.active = :active"),
     @NamedQuery(name = "PurchaseOrderLine.findByUom", query = "SELECT p FROM PurchaseOrderLine p WHERE p.uom = :uom")})
public class PurchaseOrderLine implements Serializable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "price")
    private double price;
    @Basic(optional = false)
    @NotNull
    @StrictlyPositiveNumber(message = "{PositiveQuantity}")
    @Column(name = "quantity")
    private Double quantity = 1d;
    @Size(max = 64, message = "{LongString}")
    @Column(name = "state")
    private String state;
    @Size(max = 64, message = "{LongString}")
    @Column(name = GlobeConstants.PROP_NAME)
    private String name;
    @Size(max = 40, message = "{LongString}")
    @Column(name = "uom")
    private String uom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sub_total")
    private double subTotal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;
    @NotNull
    @Column(name = "invoiced")
    private Boolean invoiced;
    @Transient
    private String productName;
    @Transient
    private String taxName;
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private PurchaseOrder purchaseOrder;
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private EnterpriseProduct product;
    @JoinColumn(name = "tax_id", referencedColumnName = "id")
    @ManyToOne
    private EnterpriseTax tax;

    public PurchaseOrderLine() {
    }

    public PurchaseOrderLine(Integer id) {
        this.id = id;
    }

    public PurchaseOrderLine(Integer id, double price, Double quantity, double subTotal, Boolean active) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public EnterpriseProduct getProduct() {
        return product;
    }

    public void setProduct(EnterpriseProduct product) {
        this.product = product;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }
    
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Boolean getInvoiced() {
        return invoiced;
    }

    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    public EnterpriseTax getTax() {
        return tax;
    }

    public void setTax(EnterpriseTax tax) {
        this.tax = tax;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : super.hashCode());
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PurchaseOrderLine)) {
            return false;
        }
        PurchaseOrderLine other = (PurchaseOrderLine) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PurchaseOrderLine[ id=" + id + " ]";
    }
    
}
