package net.ecology.service.impl;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.config.ConfigurationPersistence;
import net.ecology.css.service.config.ConfigurationService;
import net.ecology.css.specification.ConfigurationRepoSpecification;
import net.ecology.entity.config.Configuration;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;

@Service
public class ConfigurationServiceImpl extends GenericService<Configuration, Long> implements ConfigurationService{
	private static final long serialVersionUID = -1435351574637430464L;

	@Inject 
	private ConfigurationPersistence repository;
	
	protected IPersistence<Configuration, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Optional<Configuration> getByName(String name) throws ObjectNotFoundException {
		return repository.findByName(name);
	}

	@Override
	protected Page<Configuration> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Configuration> getObjects(SearchParameter searchParameter) {
		return this.repository.findAll(ConfigurationRepoSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
	}

	@Override
	public List<Configuration> getByGroup(String group) {
		return this.repository.findByGroup(group);
	}

	@Override
	protected Optional<Configuration> fetchBusinessObject(Object key) throws CerberusException {
		return super.getBizObject("findByName", key);
	}

	@Override
	public boolean isExistsByGroup(String group) {
		return this.repository.existsByGroup(group);
	}

	@Override
	public boolean isExistsByName(String name) {
		return this.repository.existsByName(name);
	}
}
