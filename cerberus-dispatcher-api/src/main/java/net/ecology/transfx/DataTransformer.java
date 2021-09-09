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
public interface DataTransformer {
  /**
  * This is the marshaling method which transforms all data from proxy object to an entity of a business object.
  * @param targetBusinessObject The target business object that will be transformed to. 
  * @param proxyObject The source proxy object which contained data for transforming from. 
  * @return Transformed business object.
  * @exception CerberusException On input error.
  * @see CerberusException
  */
	RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject) throws CerberusException;

  /**
  * This is the marshaling method which transforms all data from proxy object to an entity of a business object.
  * @param targetBusinessObject The target business object that will be transformed to. 
  * @param proxyObject The source proxy object which contained data for transforming from. 
  * @param excludedAttributes The list of attributes not marshaling
  * @return Transformed business object.
  * @exception CerberusException On input error.
  * @see CerberusException
  */
	RepoObject transformToBusiness(final Repository proxyObject, RepoObject targetBusinessObject, String[] excludedAttributes) throws CerberusException;

	/**
  * This is the un-marshaling method which transforms all data from an entity of a business object to a proxy object.
  * @param targetBusinessObject The target business object that will be transformed to. 
  * @param proxyObject The source proxy object which contained data for transforming from. 
  * @return Transformed business object.
  * @exception CerberusException On input error.
  * @see CerberusException
  */
	Repository transformToProxy(final RepoObject businessObject, Repository targetProxyObject) throws CerberusException;
}
