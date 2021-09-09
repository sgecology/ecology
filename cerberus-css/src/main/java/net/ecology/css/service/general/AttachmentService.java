package net.ecology.css.service.general;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import net.ecology.domain.entity.Attachment;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.service.IGenericService;

@Transactional
public interface AttachmentService extends IGenericService<Attachment, Long> {
	Optional<Attachment> getByName(String name);
	/**
	 * Get one Attachments with the provided search parameters.
	 * 
	 * @param searchParameter
	 *            The search parameter
	 * @return The pageable Attachments
	 */
	Page<Attachment> getObjects(SearchParameter searchParameter);
}
