/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.stock.InventoryDetail;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class ProductSpecification extends CoreSpecifications<InventoryDetail, SearchSpec>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3133249383334598142L;

	public static Specification<InventoryDetail> buildSpecification(final SearchParameter searchParameter) {
		return ProductSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
