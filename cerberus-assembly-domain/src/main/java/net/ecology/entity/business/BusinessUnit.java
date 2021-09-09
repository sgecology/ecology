package net.ecology.entity.business;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.embeddable.Address;
import net.ecology.entity.general.Catalogue;
import net.ecology.global.GlobalConstants;
import net.ecology.global.GlobeConstants;

/**
 * An office or business unit.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "business_unit")
@EqualsAndHashCode(callSuper=false)
public class BusinessUnit extends BusinessEntity {
	private static final long serialVersionUID = -1396860561985366652L;

	@Size(min = 3, max = GlobalConstants.SIZE_SERIAL)
	@Column(name = GlobeConstants.PROP_CODE)
	private String code;

	@Size(min = 3, max = 200)
	@Column(name = "name_local")
	private String nameLocal;

	@Size(min = 5, max = 200)
	@Column(name = GlobeConstants.PROP_NAME, nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private BusinessUnit parent;

	@ManyToOne
	@JoinColumn(name = "level_id")
	private Catalogue level;

	@ManyToOne
	@JoinColumn(name = "business_level_id")
	private Catalogue businessLevel;

	@Column(name = "support_level", length=GlobalConstants.SIZE_STRING_SMALL)
	private String supportLevel;

	@Column(name = "support_category", length=GlobalConstants.SIZE_STRING_SMALL)
	private String supportCategory;

	@Column(name = "management_model", length = GlobalConstants.SIZE_STRING_SMALL)
	private String managementModel;

	@Column(name = "operating_model", length = GlobalConstants.SIZE_STRING_SMALL)
	private String operatingModel;

	@Column(name = "activity_status")
	private String activityStatus;

	@Column(name = "organizational_model")
	private String organizationalModel;

	@Column(name = "info", columnDefinition = "TEXT")
	private String info;

	@Size(min = 5, max = 35)
	@Column(name = "phones")
	private String phones;

  @Column(name="firm_title")
  private String firmTitle;
  
  @Column(name="tax_office")
  private String taxOffice;
  
  @Column(name="tax_number")
  private String taxNumber;
	
	
  @Embedded
	@AttributeOverrides({
	  @AttributeOverride(name="address", column=@Column(name="address")),
		@AttributeOverride(name="city", column=@Column(name="city")),
		@AttributeOverride(name="state", column=@Column(name="state")),
		@AttributeOverride(name="postalCode", column=@Column(name="postal_code")),
		@AttributeOverride(name="country", column=@Column(name="country"))
	})
	private Address address;

	@Override
	public String toString() {
		return "Business unit {" + "id:" + getId() + '}';
	}
}
