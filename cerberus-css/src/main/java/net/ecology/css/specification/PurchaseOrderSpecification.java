/**
 * 
 */
package net.ecology.css.specification;

import org.springframework.data.jpa.domain.Specification;

import lombok.Builder;
import net.ecology.entity.emx.PurchaseOrder;
import net.ecology.framework.model.SearchParameter;
import net.ecology.framework.model.SearchSpec;
import net.ecology.framework.specification.CoreSpecifications;

/**
 * @author bqduc
 *
 */
@Builder
public class PurchaseOrderSpecification extends CoreSpecifications<PurchaseOrder, SearchSpec>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4608364667950258805L;

	public static Specification<PurchaseOrder> buildSpecification(final SearchParameter searchParameter) {
		return PurchaseOrderSpecification
				.builder()
				.build()
				.buildSpecifications(searchParameter);
	}
}
