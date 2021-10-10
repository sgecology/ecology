package net.ecology.framework.service.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import net.ecology.auxt.SecurityServiceContextHelper;
import net.ecology.common.BeanUtility;
import net.ecology.common.CollectionsUtility;
import net.ecology.common.CommonUtility;
import net.ecology.common.StringUtilities;
import net.ecology.domain.Context;
import net.ecology.exceptions.CerberusException;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.entity.Repo;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.persistence.BasePersistence;
import net.ecology.framework.persistence.CodePersistence;
import net.ecology.framework.persistence.CodeSerialPersistence;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.predicator.BrilliancePredicator;
import net.ecology.framework.service.IGenericService;
import net.ecology.framework.specification.DefaultSpecification;
import net.ecology.global.GlobeConstants;


@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public abstract class GenericService<ClassType extends Repo, Key extends Serializable> extends ServiceMarshall<ClassType, Key> implements IGenericService<ClassType, Key>{

	private static final long serialVersionUID = 7066816485194481124L;

	@Inject 
	private SecurityServiceContextHelper securityServiceContextHelper;

	protected abstract IPersistence<ClassType, Key> getPersistence();

	protected BrilliancePredicator<ClassType> getRepositoryPredicator(){
		return null;
	}

  protected String getLoggedInUsername() {
  	String loggedInUsername = null;
  	Object principal = securityServiceContextHelper.getAuthenticationPrincipal();
  	if (principal instanceof UserDetails) {
  		loggedInUsername = ((UserDetails)principal).getUsername();
  	} else {
  		loggedInUsername = principal.toString();
  	}
  	return loggedInUsername;
  }
  
	protected Specification<ClassType> getRepoSpecification(SearchParameter searchParameter){
  	//return (Specification<EntityType>) DefaultBrilliancePredicator.builder().build().buildSpecification(searchParameter);
  	return null;
  }

	@Override
	public Page<ClassType> getObjects(SearchParameter searchParameter) {
		Specification<ClassType> repoSpec = getRepoSpecification(searchParameter);
		if (null==repoSpec) {
			repoSpec = DefaultSpecification.<ClassType, SearchSpec>builder().build().buildRepoSpecification(searchParameter);
		}
		return getPersistence().findAll(repoSpec, searchParameter.getPageable());
	}

	/**
	 * @param 
	 * contextParams context parameters
	 * 
	 */
	protected Optional<ClassType> getBizObject(Map<?, ?> contextParams) throws ObjectNotFoundException {
		Object operationSpec = contextParams.get(GlobeConstants.PARAM_OPERATION);
		Map<?, ?> operationData = (Map<?, ?>)contextParams.get(GlobeConstants.PARAM_DATA);
		
		Object fetchedObject = null;
		BasePersistence<ClassType, Key> repository = (BasePersistence<ClassType, Key>) this.getPersistence();
		try {
			if (operationSpec instanceof String) {
				fetchedObject = BeanUtility.invokeOperation(repository, (String)operationSpec, operationData, PACKAGE_PREFIX);
			} else if (operationSpec instanceof List) {
				List<String> operationSpecs = (List<String>)operationSpec;
				for (String currentOperationSpec :operationSpecs) {
					fetchedObject = BeanUtility.invokeOperation(repository, currentOperationSpec, operationData, PACKAGE_PREFIX);
					if (null != fetchedObject) {
						break;
					}
				}
			}
		} catch (Exception e) {
			throw new ObjectNotFoundException(e);
		}
		return Optional.of((ClassType)fetchedObject);
	}

	protected Optional<ClassType> getBizObject(String defaultOperationName, Object param) throws ObjectNotFoundException {
		Map<String, Object> paramData = CommonUtility.createParameterMap(GlobeConstants.PROP_NAME, param);
		Map<String, Object> contextParams = CommonUtility.createParameterMap(GlobeConstants.PARAM_OPERATION, defaultOperationName, GlobeConstants.PARAM_DATA, paramData);

		return getBizObject(contextParams);
	}

	protected Optional<ClassType> fetchBusinessObject(Object key) throws CerberusException {
    throw new CerberusException("Not implemented yet. ");
	}

	protected Page<ClassType> getPaginatedObjects(Integer page, Integer size){
		int requestedPageIdx = page-1;
		if (requestedPageIdx < 0)
			requestedPageIdx = 0;

		PageRequest pageRequest = PageRequest.of(requestedPageIdx, size, Sort.Direction.ASC, "id");
    BasePersistence<ClassType, Key> repo = (BasePersistence<ClassType, Key>) getPersistence();
    if (null != repo)
    	return repo.findAll(pageRequest);

    return null;
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
	public void remove(Key id) {
		getPersistence().deleteById(id);
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
	public void remove(ClassType entity) {
		getPersistence().delete(entity);
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void removeAll() {
  	getPersistence().deleteAll();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean exists(String property, Object value) {
		String invokeMethod = "existsBy" + StringUtilities.capitalize(property);
		Map<?, ?> parameters = CollectionsUtility.newHashedMap(property, value);
		boolean isExists = false;
		try {
			isExists = existsEntity(invokeMethod, parameters);
		} catch (CerberusException e) {
			throw new CerberusException(e);
		}
		return isExists;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public boolean exists(String property, String processingClause, Object ...values) {
		String invokeMethod = "existsBy" + StringUtilities.capitalize(property);
		Map<?, ?> parameters = CollectionsUtility.newHashedMap(property, values);
		boolean isExists = false;
		try {
			isExists = existsEntity(invokeMethod, parameters);
		} catch (CerberusException e) {
			throw new CerberusException(e);
		}
		return isExists;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Long count() {
		return getPersistence().count();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Long count(String countByProperty, Object value) {
		String invokeMethod = "countBy" + StringUtilities.capitalize(countByProperty);
		Map<?, ?> parameters = CollectionsUtility.newHashedMap(countByProperty, value);
		return countEntity(invokeMethod, parameters);
		//throw new RuntimeException("Not implemented yet");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public Long count(String countMethodName, Map<?, ?> parameters) {
		return countEntity(countMethodName, parameters);
	}

	private long countEntity(String methodName, Map<?, ?> parameters) {
		Object retData = null;
		long count = 0;
		try {
			retData = BeanUtility.invokeOperation(this.getPersistence(), methodName, parameters, PACKAGE_PREFIX);
			count = (Long)retData;
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			logger.error(e);
		}
		return count;
	}

	private boolean existsEntity(String methodName, Map<?, ?> parameters) throws CerberusException {
		Object retData = null;
		boolean exists = false;
		try {
			retData = BeanUtility.invokeOperation(this.getPersistence(), methodName, parameters, PACKAGE_PREFIX);
			exists = Boolean.TRUE.equals(retData);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| InstantiationException e) {
			//log.error(e);
			throw new CerberusException(e);
		}
		return exists;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ClassType> getObjects() {
		List<ClassType> results = new ArrayList<>();
		getPersistence().findAll().forEach(results::add);
		return results;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
	public List<ClassType> getVisibleObjects() {
		return ((BasePersistence)getPersistence()).findVisible();
	}

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ClassType save(ClassType entity) {
  	return getPersistence().saveAndFlush(entity);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public GenericService<ClassType, Key> saveAndFlush(ClassType entity) {
  	getPersistence().saveAndFlush(entity);
  	return this;
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ClassType saveOrUpdate(ClassType entity) {
  	return this.save(entity);
  }

	public List<ClassType> imports(Map<Object, Object> parameters){
		return null;
	}

	protected Page<ClassType> doGetObjects(SearchParameter searchParameter) {
		BasePersistence<ClassType, Key> repository = (BasePersistence)getPersistence();
		BrilliancePredicator<ClassType> predicator = this.getRepositoryPredicator();
		Page<ClassType> pagedObjects = null;
		if (null != predicator){
			pagedObjects = repository.findAll(predicator.buildSpecification(searchParameter), searchParameter.getPageable());
		}else{
			//pagedObjects = CollectionsUtility.createPageable(repository.findAll(), searchParameter.getPageable());
		}
		return pagedObjects;
	}

	/*public Page<ClassType> getObjects(SearchParameter searchParameter) {
		//Page<EntityType> pagedEntities = doGetObjects(searchParameter);
		Page<ClassType> pagedEntities = performGetObjects(searchParameter);
		//Perform additional operations here
		return pagedEntities;
	}*/

	public Context load(Context executionContext) throws ContextExecutionException {
		return executionContext;
	}

	/*protected Page<ClassType> performGetObjects(SearchParameter searchParameter) {
		return getRepository().findAll(
				DefaultSpecification.<ClassType, SearchSpec>builder().build().buildRepoSpecification(searchParameter),
				searchParameter.getPageable());
	}*/

	public Optional<ClassType> getBusinessObject(Object key) throws CerberusException {
		Optional<ClassType> fetchedBizObject = this.fetchBusinessObject(key);
		if (!fetchedBizObject.isPresent())
			return Optional.empty();

		/*
		if (fetchedBizObject.get() instanceof RepoAuditable && 
				(!Boolean.TRUE.equals(((RepoAuditable)fetchedBizObject.get()).getVisible())||!Boolean.TRUE.equals(((RepoAuditable)fetchedBizObject.get()).getSystem()))) {
			logger.error("The business object with key: [" + key + "] not activated or visible yet. ");
			return Optional.empty();
		}
		*/

		return fetchedBizObject;
	}

	public String nextSerial(String prefix) throws CerberusException {
		throw new CerberusException("Not implemented yet!");
	}

	public Optional<ClassType> getByCode(String code) throws ObjectNotFoundException {
		return ((CodePersistence<ClassType, Key>)getPersistence()).findByCode(code);
	}

	public Optional<ClassType> getBySerial(String serial) throws ObjectNotFoundException {
		return ((CodeSerialPersistence<ClassType, Key>)getPersistence()).findBySerial(serial);
	}
}