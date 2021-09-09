package net.ecology.service.impl;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import net.ecology.css.persistence.general.AttachmentPersistence;
import net.ecology.css.service.general.AttachmentService;
import net.ecology.domain.entity.Attachment;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
@Transactional
public class AttachmentServiceImpl extends GenericService<Attachment, Long> implements AttachmentService{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private AttachmentPersistence repository;
	
	protected IPersistence<Attachment, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Attachment> getByName(String name) {
		return this.repository.findByName(name);
	}
}
