
package net.ecology.entity.emx;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "product_uom")
@NamedQueries({
    @NamedQuery(name = "ProductUom.findAll", query = "SELECT p FROM ProductUom p"),
    @NamedQuery(name = "ProductUom.findById", query = "SELECT p FROM ProductUom p WHERE p.id = :id"),
    @NamedQuery(name = "ProductUom.findByName", query = "SELECT p FROM ProductUom p WHERE p.name = :name"),
    @NamedQuery(name = "ProductUom.findByActive", query = "SELECT p FROM ProductUom p WHERE p.active = :active")})
public class ProductUom implements Serializable {
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
    @Column(name = "decimals")
    private Integer decimals;       
    @Basic(optional = false)
    @NotNull
    @Column(name = "active")
    private Boolean active;
    @OneToMany(mappedBy = "uom")
    private List<EnterpriseProduct> products;
    @OneToMany(mappedBy = "uom")
    private List<JournalItem> journalItems;
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ProductUomCategory category;
    

    public ProductUom() {
    }

    public ProductUom(Integer id) {
        this.id = id;
    }

    public ProductUom(Integer id, String name, Boolean active) {
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }
 
    public ProductUomCategory getCategory() {
        return category;
    }

    public void setCategory(ProductUomCategory category) {
        this.category = category;
    }

    public List<EnterpriseProduct> getProducts() {
        return products;
    }

    public void setProducts(List<EnterpriseProduct> products) {
        this.products = products;
    }

    public List<JournalItem> getJournalItems() {
        return journalItems;
    }

    public void setJournalItemList(List<JournalItem> journalItems) {
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
        if (!(object instanceof ProductUom)) {
            return false;
        }
        ProductUom other = (ProductUom) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProductUom[ id=" + id + " ]";
    }
    
}
