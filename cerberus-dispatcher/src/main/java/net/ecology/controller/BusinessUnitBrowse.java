package net.ecology.controller;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.ecology.css.service.org.BusinessUnitService;
import net.ecology.entity.business.BusinessUnit;
import net.ecology.framework.controller.BrowserHome;
import net.ecology.framework.model.CodeNameFilterBase;

/**
 * @author ducbq
 */
@Named
@ViewScoped
public class BusinessUnitBrowse extends BrowserHome<BusinessUnit, CodeNameFilterBase> /*extends CompCore*/ {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2318856342073765984L;

	private static final String cachedDataProp = "cacheBusinessUnits";
	@Inject
	private BusinessUnitService businessService;

	@Override
	protected List<BusinessUnit> requestBusinessObjects() {
		Object workingData = this.fetchCachedData(cachedDataProp);
		if (null == workingData) {
			workingData = businessService.getObjects();
			this.cachePut(cachedDataProp, workingData);
		}
		return (List<BusinessUnit>)workingData;
	}

	/*@Setter @Getter
	private BusinessUnit selectedObject;

	@Setter @Getter
	private List<BusinessUnit> selectedObjects;

	@Setter @Getter
	private List<BusinessUnit> businessObjects;
	@Setter @Getter
	private List<BusinessUnit> filteredObjects;

	@Setter @Getter
	private String instantSearch;

	@Setter @Getter
	private Long id;

	@Setter @Getter
	private Filter<BusinessUnit> filter = new Filter<>(new BusinessUnit());

	@Setter @Getter
	private List<BusinessUnit> filteredValue;// data table filteredValue attribute (column filters)

	@PostConstruct
	public void initDataModel() {
		try {
			this.businessObjects = businessService.getObjects();
		} catch (Exception e) {
			log.error(e);
		}
	}

	public void clear() {
		filter = new Filter<BusinessUnit>(new BusinessUnit());
	}

	public List<String> completeModel(String query) {
		List<String> result = CollectionsUtility.createDataList();
		return result;
	}

	public void search(String parameter) {
		System.out.println("Searching parameter: " + parameter);
		
		 * if (id == null) { throw new BusinessException("Provide Car ID to load"); }
		 * selectedCars.add(carService.findById(id));
		 
	}

	public void delete() {
		if (CommonUtility.isNotEmpty(this.selectedObjects)) {
			for (BusinessUnit removalItem : this.selectedObjects) {
				System.out.println("#" + removalItem.getDisplayName());
				this.businessObjects.remove(removalItem);
			}
			this.selectedObjects.clear();
		}
	}

	public void recordsRowSelected(AjaxBehaviorEvent e) {
		System.out.println("recordsRowSelected");
	}*/
}
