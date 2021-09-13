/**
 * 
 */
package net.ecology.entity.config;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import net.ecology.common.CommonUtility;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * @author bqduc
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "configuration")
public class Configuration extends RepoObject {
	private static final long serialVersionUID = -7122460417089959400L;

	@Setter @Getter
	@Column(name = GlobeConstants.PROP_NAME, length=50, nullable=false, unique=true)
	private String name;

	@Setter @Getter
	@Column(name = "value", length=200, nullable=false)
	private String value;

	@Setter @Getter
	@Column(name = "value_extended", length=500)
	private String valueExtended;

	@Setter @Getter
	@Column(name = "grouped", length=30)
	private String group;

	@Setter @Getter
	@Column(name = "info", columnDefinition="TEXT")
	private String info;
	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Configuration parent;

	@Builder.Default
	@Getter @Setter
	@OneToMany(mappedBy="parent")
	private Set<Configuration> subordinates = CollectionsUtility.newHashSet();
	
	public Configuration addSubordinates(List<Configuration> configurations) {
		if (CommonUtility.isEmpty(configurations))
			return this;

		configurations.forEach((configDetail)->{
			configDetail.setParent(this);
		});

		this.subordinates.addAll(configurations);
		return this;
	}

	public Configuration addSubordinate(Configuration configuration) {
		if (null==configuration.getParent()) {
			configuration.setParent(this);
		}

		this.subordinates.add(configuration);
		return this;
	}

	public Map<Object, Object> getConfigDetailsMap(){
		Map<Object, Object> fetchedResults = CollectionsUtility.newMap();
		this.subordinates.forEach((configDetail)->{
			fetchedResults.put(configDetail.getName(), configDetail.getValue());
		});
		return fetchedResults;
	}
}
