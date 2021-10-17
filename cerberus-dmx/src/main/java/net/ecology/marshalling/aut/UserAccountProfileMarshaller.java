/**
 * 
 */
package net.ecology.marshalling.aut;

import lombok.AllArgsConstructor;
import lombok.Builder;
import net.ecology.domain.Context;
import net.ecology.entity.auth.UserAccountProfile;
import net.ecology.marshalling.MarshallerBasis;

/**
 * @author ducbq
 *
 */
@Builder
@AllArgsConstructor
public class UserAccountProfileMarshaller extends MarshallerBasis<UserAccountProfile, Context> {
	@Override
	public UserAccountProfile unmarshal(Context context) {
		return null;
	}
}
