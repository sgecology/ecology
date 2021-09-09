/**
 * 
 */
package net.ecology.framework.entity.audit;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;
import net.ecology.framework.entity.RepoObject;

/**
 * @author ducbq
 *
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class RepoAudit<T> extends RepoObject {
	/**
   * 
   */
  private static final long serialVersionUID = 833582405761353221L;

  @Setter @Getter @CreatedBy
  @Column(name="created_by")
  protected T createdBy;

  @Setter @Getter @CreatedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="created_date")
  protected Date createdDate;

  @Setter @Getter @LastModifiedBy
  @Column(name="last_modified_by")
  private T lastModifiedBy;

  @Setter @Getter @LastModifiedDate
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="last_modified_date")
  protected Date lastModifiedDate;

  @Setter @Getter
  @Column(name = "visible")
  private Boolean visible = Boolean.FALSE;
}
