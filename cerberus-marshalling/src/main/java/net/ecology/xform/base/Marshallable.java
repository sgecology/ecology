/**
 * 
 */
package net.ecology.xform.base;

import net.ecology.exceptions.MarshallableException;

/**
 * @author ducbq
 *
 */
public interface Marshallable <TargetType, SourceType>{
	Object marshal(TargetType entity) throws MarshallableException;
	TargetType unmarshal(SourceType data) throws MarshallableException;
}
