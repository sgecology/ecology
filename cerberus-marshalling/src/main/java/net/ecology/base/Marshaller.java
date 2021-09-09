/**
 * 
 */
package net.ecology.base;

/**
 * @author ducbq
 *
 */
public interface Marshaller<TargetType, SourceType> {
	TargetType marshal(SourceType dataElements);
	SourceType unmarshal(TargetType repoObject);
}
