package net.ecology.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.ecology.framework.entity.RepoObject;
import net.ecology.global.GlobeConstants;

/**
 * An attachment.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attachment")
@EqualsAndHashCode(callSuper=false)
public class Attachment extends RepoObject {
	private static final long serialVersionUID = 5803112544828198887L;

	@Column(name = GlobeConstants.PROP_NAME, nullable = false, length=200)
	private String name;

  @Column(name = "mimetype", length=200)
  private String mimetype;
  
	@Lob
  private byte[] data;

	@Column(name = "encryption_key", length=50)
	private String encryptionKey;
}
