/**
 * 
 */
package net.ecology.transfx;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Component;

import net.ecology.entity.trade.Tax;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.RepoObject;
import net.ecology.service.trade.TaxGroupService;
import net.ecology.service.trade.TaxService;
import net.peaga.domain.base.Repository;
import net.peaga.domain.trade.TaxProxy;

/**
 * @author ducbq
 *
 */
@Named
@Component
public class TaxDataObjectTransformer extends DataTransformerBase implements DataTransformer {
	@Inject 
	private DataTransformer simpleDataObjectTransformer;

	@Inject 
	private TaxGroupService taxGroupService;

	@Inject 
	private TaxService businessService;

	protected RepoObject transformToBusinessObject(Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes)  throws CerberusException {
		TaxProxy taxProxy = null;
		Tax taxObject = null;
		try {
			this.simpleDataObjectTransformer.transformToBusiness(proxyObject, targetBusinessObject, excludedAttributes);
			taxProxy = (TaxProxy)proxyObject;
			taxObject = (Tax)targetBusinessObject;
			if (taxProxy.getGroup() != null) {
				taxObject.setGroup(taxGroupService.getObject(taxProxy.getGroup().getId()));
			}

			if (null != taxProxy.getParent()) {
				taxObject.setParent(this.businessService.getObject(taxProxy.getParent().getId()));
			}
		} catch (Exception e) {
			throw new CerberusException(e);
		}
		return targetBusinessObject;
	}

	@Override
	protected RepoObject doTransformToBusiness(Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException {
		return transformToBusinessObject(proxyObject, targetBusinessObject, new String[] {});
	}

	@Override
	protected RepoObject doTransformToBusiness(Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException {
		return transformToBusinessObject(proxyObject, targetBusinessObject, excludedAttributes);
	}

	@Override
	protected Repository doTransformToProxy(RepoObject businessObject, Repository targetProxyObject) throws CerberusException {
		return this.simpleDataObjectTransformer.transformToProxy(businessObject, targetProxyObject);
	}
}
