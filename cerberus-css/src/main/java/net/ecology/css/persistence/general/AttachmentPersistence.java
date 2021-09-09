/**
 * 
 */
package net.ecology.css.persistence.general;

import org.springframework.stereotype.Repository;

import net.ecology.domain.entity.Attachment;
import net.ecology.framework.persistence.NamePersistence;

/**
 * @author bqduc
 *
 */
@Repository
public interface AttachmentPersistence extends NamePersistence<Attachment, Long> {
}
