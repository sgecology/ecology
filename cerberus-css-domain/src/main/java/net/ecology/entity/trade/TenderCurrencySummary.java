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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Teklif üzerinde yer alan tüm döviz toplamlarını gruplu olarak tutar...
 * 
 * @author sinan.yumak
 *
 */
@Entity
@Table(name="TENDER_CURRENCY_SUMMARY")
public class TenderCurrencySummary extends TenderCurrencySummaryBase {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name="OWNER_ID")
    private Tender owner;
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TenderCurrencySummary)) {
            return false;
        }
        TenderCurrencySummary other = (TenderCurrencySummary)object;
        if (this.getId() != other.getId() && (this.getId() == null || !this.getId().equals(other.getId()))) return false;
        return true;
    }

    @Override
    public String toString() {
        return "com.ut.tekir.entities.TenderCurrencySummary[id=" + getId() + "]";
    }

	public Tender getOwner() {
		return owner;
	}

	public void setOwner(Tender owner) {
		this.owner = owner;
	}
}
