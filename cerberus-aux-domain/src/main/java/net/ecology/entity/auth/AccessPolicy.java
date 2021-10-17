/**
 * 
 */
package net.ecology.entity.auth;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.common.CollectionsUtility;
import net.ecology.domain.AccessDecision;
import net.ecology.framework.entity.RepoObject;

/**
 * @author ducbq
 *
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aut_access_policy")
public class AccessPolicy extends RepoObject {
	private static final long serialVersionUID = 2662390506129023429L;

	@Setter @Getter
	@Column(name = "access_pattern", length=120, nullable=false)
	private String accessPattern;

	@Setter @Getter
	@Builder.Default
	@Column(name="access_decision")
  @Enumerated(EnumType.ORDINAL)
	private AccessDecision accessDecision = AccessDecision.ACCESS_ABSTAIN;

	/*@Setter
	@Getter
	@ManyToOne
	@JoinColumn(name = "authority_id")
	private Authority authority;*/

	@Setter
	@Getter
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private AccessPolicy parent;

	@Setter
	@Getter
	@Column(name = "info", columnDefinition="text")
	private String info;

	@Setter
	@Getter
	@Builder.Default
  @OneToMany(mappedBy="accessPolicy"
      , cascade = CascadeType.ALL
      , orphanRemoval = true
      , fetch = FetchType.EAGER)
  private List<GrantedAccessPolicy> accessDecisionAuthorities = CollectionsUtility.newList();

	public AccessPolicy addAccessDecisionAuthority(Authority authority) {
		accessDecisionAuthorities.add(
				GrantedAccessPolicy.builder()
				.accessPolicy(this)
				.authority(authority)
				.build()
			);
		return this;
	}
}
