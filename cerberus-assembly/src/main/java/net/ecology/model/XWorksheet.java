/**
 * 
 */
package net.ecology.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bqduc
 *
 */
@Builder
public class XWorksheet extends XPackage <List<?>> implements IXContainer <List<?>> {
	@Setter @Getter
	private String id;
}
