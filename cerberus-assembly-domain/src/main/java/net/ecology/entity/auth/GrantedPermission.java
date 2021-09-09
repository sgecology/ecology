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
@Table(name = "aux_granted_permission")
public class GrantedPermission extends RepoObject {
	private static final long serialVersionUID = 1573622306678074353L;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "principal_id")
	private UserPrincipal principal;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "authority_id")
	private Authority authority;	
}
