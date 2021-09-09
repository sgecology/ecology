/**
 * 
 */
package net.ecology.framework.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import lombok.Getter;
import lombok.Setter;
import net.ecology.common.CollectionsUtility;
import net.ecology.framework.model.FilterBase;

/**
 * @author bqduc
 *
 */
public abstract class BrowserHome<E, F extends FilterBase> extends Home <E, F> {
	private static final long serialVersionUID = -2784847685814774630L;

	@Setter
	@Getter
	protected F filterModel; 

	@Setter
	@Getter
	protected String instantSearch;

	private List<E> businessObjects = CollectionsUtility.createList();

	@Setter
	@Getter
	protected List<E> selectedObjects = CollectionsUtility.createList();

	@Setter
	@Getter
  protected List<E> filteredObjects = CollectionsUtility.createList();

	protected abstract List<E> requestBusinessObjects();

	@PostConstruct
  void init() {
  	this.filterModel = createFilterModel();

  	//Loading mandatory business objects
  	this.businessObjects = this.requestBusinessObjects();
  }

  public F createFilterModel() {
  	return null;
  }

	public List<E> getBusinessObjects() {
		return businessObjects;
	}

	public void setBusinessObjects(List<E> businessObjects) {
		this.businessObjects = businessObjects;
	}

}
