/**
 * 
 */
package net.ecology.framework.component;

import java.io.Serializable;

import javax.inject.Inject;

import net.ecology.framework.logging.LogService;
import net.ecology.global.GlobalConstants;

/**
 * @author bqduc
 *
 */
public abstract class BasisComp implements Serializable {
	private static final long serialVersionUID = -7003880176947562783L;

	@Inject
	protected LogService logger;

  protected static final String LOG_ENTRY_ENTER = "Enter";
  protected static final String LOG_ENTRY_LEAVE = "Leave";

  protected static final String PACKAGE_PREFIX = GlobalConstants.QNS_PACKAGE + ".";
}
