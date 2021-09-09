
package net.ecology.entity.auth;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * 
 * @author ducbq
 * 
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "aux_authority")
public class Authority extends RepoObject implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8839233159158564921L;

	@Setter @Getter
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 64, message = "{LongString}")
	@Column(name = GlobeConstants.PROP_NAME, unique=true)
	private String name;

	@Setter
	@Getter
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "display_name")
	private String displayName;

	@Setter
	@Getter
	@Column(name = "info")
	private String info;

	@Setter
	@Getter
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Authority parent;

	@Setter
	@Getter
	@Builder.Default
	@Column(name = "administrative")
	private Boolean administrative = Boolean.FALSE;

	@Override
	public String getAuthority() {
		return this.name;
	}
}