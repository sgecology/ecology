/**
 * 
 */
package net.ecology.instruments.base;

import net.ecology.exceptions.CerberusException;

/**
 * @author ducbq
 *
 */
public interface Marshaller<Entity, Marshallable> {
	Entity unmarshal(Marshallable dataElements) throws CerberusException;
	Marshallable marshal(Entity repoObject) throws CerberusException;
}
