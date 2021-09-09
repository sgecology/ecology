package net.ecology.controller.general;

import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.ecology.entity.business.BusinessUnit;
import net.ecology.framework.controller.BrowserHome;
import net.ecology.framework.model.CodeNameFilterBase;

/**
 * @author ducbq
 */
@Named
@ViewScoped
public class HierarchicalController extends BrowserHome<BusinessUnit, CodeNameFilterBase> {
  private static final long serialVersionUID = 366789998584111652L;
	@Inject
	private HierarchicalService hierarchicalService;

  public HierarchicalService getHierarchicalService() {
    return hierarchicalService;
  }

  public void setHierarchicalService(HierarchicalService hierarchicalService) {
    this.hierarchicalService = hierarchicalService;
  }

  @Override
  protected List<BusinessUnit> requestBusinessObjects() {
    this.hierarchicalService.init();
    return null;
  }
}
