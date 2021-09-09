/**
 * 
 */
package net.ecology.framework.logging;

import java.io.Serializable;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import net.ecology.common.CommonUtility;

/**
 * @author bqduc
 *
 */
@Slf4j
@Component
public class LogService implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8926765774281312232L;

	public void info(Logger logger, Throwable throwable) {
		log.info(CommonUtility.getStackTrace(throwable));
	}

	public void error(Logger logger, Throwable throwable) {
		log.error(CommonUtility.getStackTrace(throwable));
	}

	public void debug(Logger logger, Throwable throwable) {
		log.debug(CommonUtility.getStackTrace(throwable));
	}

	public void info(Throwable throwable) {
		log.info(CommonUtility.getStackTrace(throwable));
	}

	public void error(Throwable throwable) {
		log.error(CommonUtility.getStackTrace(throwable));
	}

	public void debug(Throwable throwable) {
		log.debug(CommonUtility.getStackTrace(throwable));
	}

	public void info(String msg, Object...objects) {
		log.info(msg, objects);
	}

	public void info(String msg, Throwable throwable) {
		log.info(msg, throwable);
	}

	public void error(String msg, Throwable throwable) {
		log.error(msg, throwable);
	}

	public void debug(String msg, Throwable throwable) {
		log.debug(msg, throwable);
	}

	public void debug(String msg, Object... objects) {
		log.debug(msg, objects);
	}

	public void info(String msg) {
		log.info(msg);
	}

	public void error(String msg) {
		log.error(msg);
	}

	public void debug(String msg) {
		log.debug(msg);
	}

	public void warn(String msg) {
		log.warn(msg);
	}
}
