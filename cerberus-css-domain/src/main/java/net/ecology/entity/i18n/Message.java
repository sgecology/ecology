/**
 * 
 */
package net.ecology.entity.i18n;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.ecology.framework.entity.RepoObject;

/**
 * @author ducbq
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
		name = "i18n_message", 
		uniqueConstraints={@UniqueConstraint(columnNames = {"key", "locale_id"})
})
public class Message extends RepoObject {
	private static final long serialVersionUID = 5150863975442948556L;
	@Setter @Getter
	@Column(name = "key", length = 150)
	private String key;

	@Setter @Getter
	@Column(name = "label")
	private String label;

	@Setter @Getter
	@ManyToOne
	@JoinColumn(name = "locale_id")
	private I18nLocale locale;

}
