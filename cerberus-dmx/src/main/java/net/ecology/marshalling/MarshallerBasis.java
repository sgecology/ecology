/**
 * 
 */
package net.ecology.marshalling;

import net.ecology.exceptions.CerberusException;
import net.ecology.instruments.base.Marshaller;

/**
 * @author ducbq
 *
 */
public abstract class MarshallerBasis<EntityType, MarshallableType> implements Marshaller<EntityType, MarshallableType> {
	@Override
	public EntityType unmarshal(MarshallableType dataElements) throws CerberusException {
		return null;
	}

	@Override
	public MarshallableType marshal(EntityType repoObject) throws CerberusException {
		return null;
	}
}