package net.ecology.framework.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.common.BeanUtility;
import net.ecology.common.CollectionsUtility;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.framework.component.ComponentRoot;
import net.ecology.framework.entity.Repo;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.IService;
import net.ecology.global.GlobeConstants;


@Service
public abstract class ServiceCore<EntityType extends Repo, Key extends Serializable> extends ComponentRoot implements IService<EntityType, Key>{
	private static final long serialVersionUID = 7920908481607510076L;

	protected abstract IPersistence<EntityType, Key> getPersistence();

	protected EntityType getOptionalObject(Optional<EntityType> optObject) {
		return getOptional(optObject);
	}

	//////////////////////////Revise and exclude as soon as possible
	protected final Page<EntityType> DUMMY_PAGEABLE = new PageImpl<EntityType>(new ArrayList<EntityType>());
	protected final List<EntityType> DUMMY_LIST = CollectionsUtility.createDataList();

	protected Pageable createDefaultPageable() {
    PageRequest pageRequest = PageRequest.of(GlobeConstants.DEFAULT_PAGE_BEGIN, GlobeConstants.DEFAULT_PAGE_SIZE, Sort.Direction.ASC, "id");
		return pageRequest;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<EntityType> searchObjects(String keyword, Pageable pageable) {
		return performSearch(keyword, pageable);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<EntityType> search(String keyword){
		return performSearch(keyword);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<EntityType> search(String keyword, Pageable pageable){
		return performSearch(keyword, pageable);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Page<EntityType> search(Map<String, Object> parameters) {
		String keyword = (String)parameters.get(GlobeConstants.PARAM_KEYWORD);
		Pageable pageable = (Pageable)parameters.get(GlobeConstants.PARAM_PAGEABLE);
		return performSearch(keyword, pageable);
	}

	protected Page<EntityType> performSearch(String keyword, Pageable pageable){
		throw new CerberusException("Not implemented yet!!!");//DUMMY_PAGEABLE;
	}

	@SuppressWarnings("unchecked")
	protected List<EntityType> performSearch(Object parameter){
		Object findingResult = null;
		List<EntityType> searchResult = null;
		try {
			findingResult = BeanUtility.callMethod(this.getPersistence(), "find", CollectionsUtility.createHashMapData("keyword", parameter), PACKAGE_PREFIX);
			if (findingResult instanceof List) {
				searchResult = (List<EntityType>)findingResult;
			}
		} catch (ContextExecutionException e) {
			logger.error(e);
		}
		return (null==searchResult)?DUMMY_LIST:searchResult;
	}

	@Override
	public List<EntityType> search(Object searchParam) {
		return performSearch(searchParam);
	}

	protected EntityType getOptional(Optional<EntityType> optional) {
		return optional.orElse(null);
	}
}