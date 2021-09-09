/**
 * 
 */
package net.ecology.transfx;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

import org.springframework.stereotype.Component;

import net.ecology.common.BeanUtility;
import net.ecology.exceptions.CerberusException;
import net.ecology.framework.entity.RepoObject;
import net.peaga.domain.base.Repository;

/**
 * @author ducbq
 *
 */
@Named
@Component
public class SimpleDataObjectTransformer implements DataTransformer {
	@Override
	public RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException {
		try {
			BeanUtility.copyBean(proxyObject, targetBusinessObject);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new CerberusException(e);
		}
		return targetBusinessObject;
	}

	@Override
	public Repository transformToProxy(final RepoObject businessObject, Repository targetProxyObject) throws CerberusException {
		try {
			BeanUtility.copyBean(businessObject, targetProxyObject);
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			throw new CerberusException(e);
		}
		return targetProxyObject;
	}

	@Override
	public RepoObject transformToBusiness(Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException {
		return (RepoObject)BeanUtility.getInstance().copyBeanData(proxyObject, targetBusinessObject, excludedAttributes);
	}
}
