/**
 * 
 */
package net.ecology.listener;

import javax.inject.Inject;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import net.ecology.common.WebAppConstants;
import net.ecology.framework.component.BasisComp;
import net.ecology.global.GlobeConstants;
import net.ecology.repository.GlobalDataRepository;

/**
 * @author ducbq
 *
 */
@Component
public class SystemEventManager extends BasisComp {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7564662407839189753L;

  @Inject
  private GlobalDataRepository globalDataRepository;

  @EventListener(ApplicationReadyEvent.class)
	public void onApplicationReady() {
    logger.info("Enter onApplicationReady");
		try {
			this.globalDataRepository.dispatch(WebAppConstants.repoDirectory + GlobeConstants.ALL_CSV_FILES);
		} catch (Exception e) {
			logger.error(e);
		}
		logger.info("Leave onApplicationReady");
	}
}
