/**
 * 
 */
package net.ecology.base;

import net.ecology.model.Context;

/**
 * @author ducbq
 *
 */
public interface DataAdapter {
	Context marshal(Context context);
	Context unmarshal(Context context);
}
