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
public class DataWorksheet extends XPackage <List<?>> implements DataContainer <List<?>> {
	@Setter @Getter
	private String id;
}
