/**
 * 
 */
package net.ecology.model;

/**
 * @author bqduc
 *
 */
public enum GenderType {
	Unknown,
	Male,
	Female;

	public GenderType evaluate(String value){
		if ("NAM".equalsIgnoreCase(value)) return Male;
		if ("NỮ".equalsIgnoreCase(value)) return Female;
		return GenderType.valueOf(value);
	}
}
