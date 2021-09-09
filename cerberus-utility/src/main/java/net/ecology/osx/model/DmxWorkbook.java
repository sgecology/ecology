/**
 * 
 */
package net.ecology.osx.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bqduc
 *
 */
@Builder
public class DmxWorkbook extends DmxPackage <DmxWorksheet> implements DmxContainer <DmxWorksheet>{
	@Setter @Getter
	private String name;
}
