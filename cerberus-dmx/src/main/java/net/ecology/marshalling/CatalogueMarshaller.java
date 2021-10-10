/**
 * 
 */
package net.ecology.marshalling;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.entity.general.Catalogue;
import net.ecology.instruments.base.Marshaller;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class CatalogueMarshaller extends MarshallerBasis<Catalogue, String[]>  implements Marshaller<Catalogue, String[]> {
	public static short idx_subtype = 0;
	public static short idx_code = 1;
	public static short idx_name = 2;

	@Override
	public Catalogue unmarshal(String[] elements) {
		return Catalogue.builder()
  			.subtype(elements[idx_subtype])
  			.code(elements[idx_code])
  			.name(elements[idx_name])
  			.build();
	}
}
