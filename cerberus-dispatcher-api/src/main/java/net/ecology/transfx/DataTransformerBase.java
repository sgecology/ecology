/**
 * 
 */
package net.ecology.transfx;

import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.RepoObject;
import net.peaga.domain.base.Repository;

/**
 * @author ducbq
 *
 */
public abstract class DataTransformerBase {
	protected RepoObject doTransformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException {
		return targetBusinessObject;
	}

	protected RepoObject doTransformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException {
		return targetBusinessObject;
	}

	protected Repository doTransformToProxy(final RepoObject businessObject, Repository targetProxyObject) throws CerberusException {
		return targetProxyObject;
	}

	public final RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException {
		return doTransformToBusiness(proxyObject, targetBusinessObject);
	}

	public final RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException {
		return doTransformToBusiness(proxyObject, targetBusinessObject, excludedAttributes);
	}

	public final Repository transformToProxy(final RepoObject businessObject, Repository targetProxyObject) throws CerberusException {
		return doTransformToProxy(businessObject, targetProxyObject);
	}
}
