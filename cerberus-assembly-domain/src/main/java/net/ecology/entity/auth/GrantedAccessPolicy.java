package net.ecology.entity.auth;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.framework.entity.RepoObject;

/**
 * 
 * @author ducbq
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "aux_granted_access_policy")
public class GrantedAccessPolicy extends RepoObject {
	private static final long serialVersionUID = -8568792387287150298L;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "access_policy_id")
	private AccessPolicy accessPolicy;

	@Setter
	@Getter
	@ManyToOne
	@JoinColumn(name = "authority_id")
	private Authority authority;

}
