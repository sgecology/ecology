package net.ecology.service.impl;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.ecology.css.persistence.contact.ContactPersistence;
import net.ecology.css.service.contact.ContactService;
import net.ecology.css.specification.ContactSpecification;
import net.ecology.entity.contact.Contact;
import net.ecology.exceptions.ContextExecutionException;
import net.ecology.exceptions.ObjectNotFoundException;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.persistence.IPersistence;
import net.ecology.framework.service.impl.GenericService;
import net.ecology.model.Context;


@Service
public class ContactServiceImpl extends GenericService<Contact, Long> implements ContactService {
	private static final long serialVersionUID = 7761477574156308888L;

	@Inject 
	private ContactPersistence repository;

	//@Inject 
	//private BusinessAccountRepository businessAccountRepository;

	protected IPersistence<Contact, Long> getPersistence() {
		return this.repository;
	}

	@Override
	public Contact getObjectByCode(String code) throws ObjectNotFoundException {
		return (Contact)super.getOptionalObject(repository.findByCode(code));
	}

	@Override
	protected Page<Contact> performSearch(String keyword, Pageable pageable) {
		return repository.search(keyword, pageable);
	}

	@Override
	public Page<Contact> getObjects(SearchParameter searchParameter) {
		Page<Contact> pagedProducts = this.repository.findAll(ContactSpecification.buildSpecification(searchParameter), searchParameter.getPageable());
		//Perform additional operations here
		return pagedProducts;
	}

	@Override
	public Context load(Context executionContext) throws ContextExecutionException {
		/*GlobalDataServicesRepository globalDataServicesRepository = null;
		Object projectContextData = null;
		DataInterfaceModel dataInterfaceModel = null;
		IDataContainer<String> dataContainer = null;
		try {
			if (!(executionContext.containKey(DeploymentSpecification.DEPLOYMENT_DATA_KEY) || 
					executionContext.containKey(DeploymentSpecification.DEPLOYMENT_DATA_MODEL_KEY))){
				executionContext.setExecutionStage("There is not enough deployment specification for Contact. ");
				log.info(executionContext.getExecutionStage());
				return executionContext;
			}

			globalDataServicesRepository = GlobalDataServicesRepository.builder().build();
			projectContextData = executionContext.getContextData(DeploymentSpecification.DEPLOYMENT_DATA_KEY);
			dataInterfaceModel = (DataInterfaceModel)executionContext.getContextData(DeploymentSpecification.DEPLOYMENT_DATA_MODEL_KEY);
			if (DataSourceType.CSV.equals(dataInterfaceModel.getDataSourceType())){
				dataContainer = globalDataServicesRepository.readCsvFile(
						(InputStream)projectContextData, 
						dataInterfaceModel.isProcessColumnHeaders(), 
						dataInterfaceModel.getComponentSeparator());
			} else if (DataSourceType.EXCEL.equals(dataInterfaceModel.getDataSourceType())) {
			}

			if (CommonUtility.isEmpty(dataContainer)){
				executionContext.setExecutionStage("The data container is empty. Please recheck data source. ");
				log.info(executionContext.getExecutionStage());
				return executionContext;
			}
			log.info("Start to deploy Contact data ......");
			
		} catch (Exception e) {
			throw new ExecutionContextException(e);
		}

		executionContext.setExecutionStage("The data deployment for project is done. ");
		log.info(executionContext.getExecutionStage());*/
		return executionContext;
	}
}
