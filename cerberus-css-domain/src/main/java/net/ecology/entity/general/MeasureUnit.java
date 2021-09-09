/**
 * 
 */
package net.ecology.entity.general;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * @author bqduc
 *
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "measure_unit")
@EqualsAndHashCode(callSuper=false)
public class MeasureUnit extends RepoObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4182427455638019608L;

	@Column(name = GlobeConstants.PROP_CODE, unique = true)
	private String code;

	@Column(name = GlobeConstants.PROP_NAME)
	private String name;

	@Column(name = "name_local")
	private String nameLocal;

	@Column(name = "comments")
	private String info;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private MeasureUnit parent;

}
