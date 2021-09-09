/**
 * 
 */
package net.ecology.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bqduc
 *
 */
@Builder
public class DataWorkbook extends XPackage <DataWorksheet> implements DataContainer <DataWorksheet>{
	@Setter @Getter
	private String name;
}
