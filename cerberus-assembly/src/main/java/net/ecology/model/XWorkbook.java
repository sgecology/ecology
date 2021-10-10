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
public class XWorkbook extends XPackage <XWorksheet> implements IXContainer <XWorksheet>{
	@Setter @Getter
	private String name;
}
