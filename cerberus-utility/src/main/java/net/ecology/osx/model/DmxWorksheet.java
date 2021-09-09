/**
 * 
 */
package net.ecology.osx.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bqduc
 *
 */
@Builder
public class DmxWorksheet extends DmxPackage <List<?>> implements DmxContainer <List<?>> {
	@Setter @Getter
	private String id;
}
